package Bluetooth;

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
//This method is a SINGLETON!!!
