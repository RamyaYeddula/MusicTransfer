package com.example.Ramya.MusicTransfer;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Set;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import com.example.Ramya.mehrab_sample_code.R;


public class Sender extends Activity{
    private BluetoothAdapter bluetooth_adapter;
    private BluetoothServerSocket mServerSocket;
    private static final int REQUEST_PICK_FILE = 1;
//    Button sendBtn = null;
    BluetoothSocket socket = null;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sender);
        bluetooth_adapter = BluetoothAdapter.getDefaultAdapter();
        createSocket();
    }


    protected void createSocket(){
        Log.d(GlobalConstants.TAG_SERVER_SOCKET, "creating socket...");

        BluetoothDevice device = null;

        Set<BluetoothDevice> pairedDevices = bluetooth_adapter.getBondedDevices();
        // If there are paired devices
        if (pairedDevices.size() > 0) {
            // Loop through paired devices
            for (BluetoothDevice tmpDevice : pairedDevices) {
                // Add the name and address to an array adapter to show in a ListView
                Log.d("PairedDevice", tmpDevice.getName() + " -- " + tmpDevice.getAddress() + " -- " + tmpDevice.getBondState());

                // XXX
                // For using only one relay, this code should point to the same relay device that receiver also points to
                if(tmpDevice.getAddress().equalsIgnoreCase(GlobalConstants.BT_ADDR_N7)){
                    device = tmpDevice;
                    break;
                }
            }
        }

        Log.d(GlobalConstants.TAG_SERVER_SOCKET, "Bonded BT device: " + device.getName() + " -- status: " +
                device.getBondState());

            Thread serverSocketThread = new Thread() {
                @Override
                public void run() {
                    BluetoothServerSocket tmpServerSocket = null;
                    try {
                        mServerSocket = bluetooth_adapter.listenUsingInsecureRfcommWithServiceRecord("mehrabSampleCode-SocketServer",
                                GlobalConstants.SENDER_UUID);
                        Log.d(GlobalConstants.TAG_SERVER_SOCKET, "Executed listenUsingRfcommWithServiceRecord() with SENDER_UUID");

                        if(mServerSocket == null){
                            Log.d(GlobalConstants.TAG_SERVER_SOCKET, "mServerSocket is null");
                        }
                        else{
                            tmpServerSocket = mServerSocket;
                            Log.d(GlobalConstants.TAG_SERVER_SOCKET, "mServerSocket is not null");
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    Log.d(GlobalConstants.TAG_SERVER_SOCKET, "Log point 1");
                    while (true) {
                        try {
                            Log.d(GlobalConstants.TAG_SERVER_SOCKET, "Log point 2");
                            if(tmpServerSocket == null){
                                Log.d(GlobalConstants.TAG_SERVER_SOCKET, "mServerSocket is null at log point 2");
                                break;
                            }
                            else {
                                Log.d(GlobalConstants.TAG_SERVER_SOCKET, "Log point 3");
                                socket = tmpServerSocket.accept();
                                Log.d(GlobalConstants.TAG_SERVER_SOCKET, "Server connection established.");
                                manageConnectedSocket(socket);
                            }
                        } catch (IOException closeException) {
                            Log.d(GlobalConstants.TAG_SERVER_SOCKET, "IOEx occured");
                            break;
                        }

                        if (socket != null) {
                            Log.d(GlobalConstants.TAG_SERVER_SOCKET, "Socket connection was accepted");

//                            try {
//                                Log.d(GlobalConstants.TAG_SERVER_SOCKET, "Closing the socket in Sender: 1");
//                                mServerSocket.close();
//                            } catch (IOException ex) {
//                                Log.d(GlobalConstants.TAG_SERVER_SOCKET, "Error closing connection");
//                                ex.printStackTrace();
//                            }
                            break;
                        }
                    }
                }
            };
            serverSocketThread.start();
    }

    private void manageConnectedSocket(BluetoothSocket mSocket){
        Log.d(GlobalConstants.TAG_SERVER_SOCKET_STREAM, "Inside manageConnectedSocket()");
        ConnectedSocketTread conn = new ConnectedSocketTread(mSocket, null);

//        File file  = new File(Environment.getExternalStorageDirectory()+ "/Download/bbm_outgoing_call.wav");
        File file  = GlobalConstants.SOURCE_FILE;

        try {
            long length = file.length();
            byte[] bytesToSend = new byte[GlobalConstants.BUFFER_SIZE];
            InputStream inputStream = new FileInputStream(file);
            int count;
            long finalCount = 0;

            while((count = inputStream.read(bytesToSend)) > 0){
                conn.write(bytesToSend, 0, count);
                finalCount += count;
            }
            Log.d(GlobalConstants.TAG_SERVER_SOCKET_STREAM, "File size = " + length + ", Total bytes sent = " + finalCount + " B");


        } catch (IOException e){
            Log.d(GlobalConstants.TAG_SERVER_SOCKET_STREAM, "IOException occured at log point 999");
            e.printStackTrace();
        } finally {
            conn.cancel("Sender.manageConnectedSocket()");
        }
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(resultCode == RESULT_OK) {

            switch(requestCode) {

                case REQUEST_PICK_FILE:

                    break;
            }
        }

    }
}

