package Bluetooth;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

//Bluetooth class used in Gasboard to manage the connection.
//App requires manual pairing before execution as pairing and device discovery will not be implemented
public class Bluetooth implements Serializable {
    private static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");    //WTF IS DIS
    private BluetoothAdapter bluetoothAdapter;                                                      //Android's bt adapter
    private BluetoothSocket socket;                                                                 //BT Connection socket
    private BluetoothDevice device;
    private InputStream input;                                                                      //input, consider changing to Integer
    private OutputStream out;                                                                       //The output stream of the device
    private boolean connected = false;                                                              //Status of connection
    private CommunicationCallback ccb = null;                                                       //Establishes communication between MainActivity and BT threads.


    /* <Constructor>
     * Saves the adapter to a variable.
     */
    public Bluetooth() {
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    }

    /*
     * Enables the bluetooth adapter.
     */
    public void enableBluetooth() {
        if (bluetoothAdapter != null) {
            if (!bluetoothAdapter.isEnabled()) {
                bluetoothAdapter.enable();
            }
        }
    }

    /*
     * Disables the bluetooth adapter.
     */
    public void disableBluetooth() {
        if (bluetoothAdapter != null) {
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
    public void connectToDevice(BluetoothDevice device) {
        new ConnectThread(device).start();
    }

    /*
     * Disconnect from currently connected device.
     */
    public void disconnect() {
        try {
            socket.close();
        } catch (IOException e) {
            ccb.onError(e.getMessage());
        }
    }

    /*
     * Returns connection status.
     */
    public boolean isConnected() {
        return connected;
    }

    /*
     * Sends the message byte through the connection
     */
    public void send(byte msg) {
        try {
            out.write(msg);
        } catch (IOException e) {
            connected = false;
            ccb.onDisconnect(device, e.getMessage());
        }
    }

    /*
     * Reciever Thread. Recieves messages and notifies via CommunicationCallback
     */
    private class ReceiveThread extends Thread implements Runnable {
        public void run() {
            byte msg;
            Log.d("MSG","RT started" );
            try {
                while (true) {
                    msg = ((byte)input.read());
                    ccb.onMessage(msg);
                }
            } catch (IOException e) {
                connected = false;
                ccb.onDisconnect(device, e.getMessage());
            }
        }
    }

    /*
     * Connects to given device
     */

    private class ConnectThread extends Thread {
        public ConnectThread(BluetoothDevice device) {
            Bluetooth.this.device = device;
            try {
                //Opens socket for communication
                Bluetooth.this.socket = device.createRfcommSocketToServiceRecord(MY_UUID);
            } catch (IOException e) {
                ccb.onError(e.getMessage());
            }
        }

        public void run() {
            bluetoothAdapter.cancelDiscovery();

            try {
                socket.connect();
                out = socket.getOutputStream();
                input = socket.getInputStream();         // Change here to suit our program
                connected = true;

                new ReceiveThread().start();
                ccb.onConnect(device);


            } catch (IOException e) {
                ccb.onConnectError(device, e.getMessage());
                try {
                    socket.close();
                } catch (IOException closeException) {
                    ccb.onError(closeException.getMessage());
                }
            }
        }
    }

    public List<BluetoothDevice> getPairedDevices() {
        List<BluetoothDevice> devices = new ArrayList<>();
        for (BluetoothDevice blueDevice : bluetoothAdapter.getBondedDevices()) {
            devices.add(blueDevice);
        }
        return devices;
    }

    public BluetoothSocket getSocket() {
        return socket;
    }

    public BluetoothDevice getDevice() {
        return device;
    }


    public interface CommunicationCallback {
        void onConnect(BluetoothDevice device);

        void onDisconnect(BluetoothDevice device, String message);

        void onMessage(byte message);

        void onError(String message);

        void onConnectError(BluetoothDevice device, String message);
    }

    public void setCommunicationCallback(CommunicationCallback ccb) {
        this.ccb = ccb;
    }
}
