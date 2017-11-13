package Bluetooth;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


public class Bluetooth {
    private static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");    //WTF IS DIS
    private BluetoothAdapter bluetoothAdapter;                                                      //Android's bt adapter
    private BluetoothSocket socket;                                                                 //BT Connection socket
    private BluetoothDevice device, devicePair;
    private BufferedReader input;                                                                   //input, consider changing to Integer
    private OutputStream out;                                                                       //The output stream of the device

    private boolean connected=false;                                                                //Status of connection
    private CommunicationCallback communicationCallback=null;                                       //WTF IS DIS
    private DiscoveryCallback discoveryCallback=null;                                               //WTF IS DIS

    private Activity activity;                                                                      //The activity Bluetooth is created in

    /* <Constructor>
     * Takes in the host activity as parameter.
     * Saves the adapter to a variable.
     */
    public Bluetooth(Activity activity){
        this.activity=activity;
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    }

    /*
     * Enables the bluetooth adapter.
     */
    public void enableBluetooth(){
        if(bluetoothAdapter!=null) {
            if (!bluetoothAdapter.isEnabled()) {
                bluetoothAdapter.enable();
            }
        }
    }

    /*
     * Disables the bluetooth adapter.
     */
    public void disableBluetooth(){
        if(bluetoothAdapter!=null) {
            if (bluetoothAdapter.isEnabled()) {
                bluetoothAdapter.disable();
            }
        }
    }

    /*
     * Takes in the address of a device as a string parameter
     * Connects to a BT Device by using given address
     */
    public void connectToAddress(String address) {
        BluetoothDevice device = bluetoothAdapter.getRemoteDevice(address);
        new ConnectThread(device).start();
    }

    /*
     * Takes in the name of a device as a string parameter
     * Connects to a BT Device by using given name
     */
    public void connectToName(String name) {
        for (BluetoothDevice blueDevice : bluetoothAdapter.getBondedDevices()) {
            if (blueDevice.getName().equals(name)) {
                connectToAddress(blueDevice.getAddress());
                return;
            }
        }
    }

    /*
     * Takes in a BluetoothDevice as a parameter
     * Connects to a the device.
     */
    public void connectToDevice(BluetoothDevice device){
        new ConnectThread(device).start();
    }

    /*
     * Disconnect from currently connected device.
     */
    public void disconnect() {
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*
     * Returns connection status.
     */
    public boolean isConnected(){
        return connected;
    }

    /*
     * Sends the message byte throught the connection
     */
    public void send(byte msg){
        try {
            out.write(msg);
        } catch (IOException e) {
            connected=false;
            e.printStackTrace();
        }
    }

    /*
     * Reciever Thread. Recieves messages and notifies via CommunicationCallback
     */
    private class ReceiveThread extends Thread implements Runnable{
        public void run(){
            String msg;
            try {
                while ((msg = input.readLine()) != null) {
                    //Handle recieved message
                    //TO BE EDITED
                }
            } catch (IOException e) {
                connected=false;
                e.printStackTrace();
            }
        }
    }

    /*
     * Connects to given device
     */

    private class ConnectThread extends Thread {
        public ConnectThread(BluetoothDevice device) {
            Bluetooth.this.device=device;
            try {
                //Opens socket for communication
                Bluetooth.this.socket = device.createRfcommSocketToServiceRecord(MY_UUID);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public void run() {
            bluetoothAdapter.cancelDiscovery();

            try {
                socket.connect();
                out = socket.getOutputStream();
                input = new BufferedReader(new InputStreamReader(socket.getInputStream()));         // Change here to suit our program
                connected=true;

                new ReceiveThread().start();

            } catch (IOException e) {
                e.printStackTrace();
                try {
                    socket.close();
                } catch (IOException closeException) {
                    e.printStackTrace();
                }
            }
        }
    }

    public List<BluetoothDevice> getPairedDevices(){
        List<BluetoothDevice> devices = new ArrayList<>();
        for (BluetoothDevice blueDevice : bluetoothAdapter.getBondedDevices()) {
            devices.add(blueDevice);
        }
        return devices;
    }

    public BluetoothSocket getSocket(){
        return socket;
    }

    public BluetoothDevice getDevice(){
        return device;
    }
//Probably will not be used, delete before finial handin
    public void scanDevices(){
        IntentFilter filter = new IntentFilter();

        filter.addAction(BluetoothDevice.ACTION_FOUND);
        filter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);

        activity.registerReceiver(mReceiverScan, filter);
        bluetoothAdapter.startDiscovery();
    }
//same
    public void pair(BluetoothDevice device){
        activity.registerReceiver(mPairReceiver, new IntentFilter(BluetoothDevice.ACTION_BOND_STATE_CHANGED));
        devicePair=device;
        try {
            Method method = device.getClass().getMethod("createBond", (Class[]) null);
            method.invoke(device, (Object[]) null);
        } catch (Exception e) {
            if(discoveryCallback!=null)
                discoveryCallback.onError(e.getMessage());
        }
    }
//same
    public void unpair(BluetoothDevice device) {
        devicePair=device;
        try {
            Method method = device.getClass().getMethod("removeBond", (Class[]) null);
            method.invoke(device, (Object[]) null);
        } catch (Exception e) {
            if(discoveryCallback!=null)
                discoveryCallback.onError(e.getMessage());
        }
    }
//same
    private BroadcastReceiver mReceiverScan = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            switch (action) {
                case BluetoothAdapter.ACTION_STATE_CHANGED:
                    final int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.ERROR);
                    if (state == BluetoothAdapter.STATE_OFF) {
                        if (discoveryCallback != null)
                            discoveryCallback.onError("Bluetooth turned off");
                    }
                    break;
                case BluetoothAdapter.ACTION_DISCOVERY_FINISHED:
                    context.unregisterReceiver(mReceiverScan);
                    if (discoveryCallback != null)
                        discoveryCallback.onFinish();
                    break;
                case BluetoothDevice.ACTION_FOUND:
                    BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                    if (discoveryCallback != null)
                        discoveryCallback.onDevice(device);
                    break;
            }
        }
    };
//samw
    private final BroadcastReceiver mPairReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            if (BluetoothDevice.ACTION_BOND_STATE_CHANGED.equals(action)) {
                final int state = intent.getIntExtra(BluetoothDevice.EXTRA_BOND_STATE, BluetoothDevice.ERROR);
                final int prevState	= intent.getIntExtra(BluetoothDevice.EXTRA_PREVIOUS_BOND_STATE, BluetoothDevice.ERROR);

                if (state == BluetoothDevice.BOND_BONDED && prevState == BluetoothDevice.BOND_BONDING) {
                    context.unregisterReceiver(mPairReceiver);
                    if(discoveryCallback!=null)
                        discoveryCallback.onPair(devicePair);
                } else if (state == BluetoothDevice.BOND_NONE && prevState == BluetoothDevice.BOND_BONDED){
                    context.unregisterReceiver(mPairReceiver);
                    if(discoveryCallback!=null)
                        discoveryCallback.onUnpair(devicePair);
                }
            }
        }
    };
//will delete these after final test
    public interface CommunicationCallback{
        void onConnect(BluetoothDevice device);
        void onDisconnect(BluetoothDevice device, String message);
        void onMessage(String message);
        void onError(String message);
        void onConnectError(BluetoothDevice device, String message);
    }

    public void setCommunicationCallback(CommunicationCallback communicationCallback) {
        this.communicationCallback = communicationCallback;
    }

    public void removeCommunicationCallback(){
        this.communicationCallback = null;
    }

    public interface DiscoveryCallback{
        void onFinish();
        void onDevice(BluetoothDevice device);
        void onPair(BluetoothDevice device);
        void onUnpair(BluetoothDevice device);
        void onError(String message);
    }

    public void setDiscoveryCallback(DiscoveryCallback discoveryCallback){
        this.discoveryCallback=discoveryCallback;
    }

    public void removeDiscoveryCallback(){
        this.discoveryCallback=null;
    }

}
