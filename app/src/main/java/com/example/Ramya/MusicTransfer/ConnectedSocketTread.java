package com.example.Ramya.MusicTransfer;

import android.bluetooth.BluetoothSocket;
import android.media.MediaPlayer;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;


public class ConnectedSocketTread extends Thread {
    private final InputStream inputStream;
    private final OutputStream outputStream;
    private final BluetoothSocket socket;
    private Handler msgHandler;
    private final int bufferSize = GlobalConstants.BUFFER_SIZE;

    public ConnectedSocketTread(BluetoothSocket mSocket, Handler mHandler){
        socket = mSocket;
        msgHandler = mHandler;
        InputStream tempInStream = null;
        OutputStream tempOutStream = null;

        if(!socket.isConnected()){
            Log.d(GlobalConstants.TAG_SERVER_SOCKET_STREAM, "Not connected!");
        }
        else {
            Log.d(GlobalConstants.TAG_SERVER_SOCKET_STREAM, "ConnectedSocketThread: Connection still available.");
        }

        try {
            tempInStream = socket.getInputStream();
            tempOutStream = socket.getOutputStream();
        } catch (IOException e) {
            Log.d(GlobalConstants.TAG_SERVER_SOCKET_STREAM, "IOException occured");
            e.printStackTrace();
        }

        inputStream = tempInStream;
        outputStream = tempOutStream;
        Log.d(GlobalConstants.TAG_SERVER_SOCKET_STREAM, "Exiting ConnectedSocketThread constructor");
    }

    @Override
    public void run() {
        Log.d(GlobalConstants.TAG_CLIENT_SOCKET_STREAM, "Inside the run() method");
        byte buffer[] = new byte[bufferSize];
        int bytesReturned;

        File tempFile = GlobalConstants.SOURCE_FILE;
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(tempFile);
        } catch (FileNotFoundException e) {
            Log.d(GlobalConstants.TAG_CLIENT_SOCKET_STREAM, "Playing file - FileNotFoundException");
            e.printStackTrace();
        }

        int cnt = 0;
        int totalBytes = 0;

        while(true){
            try {
                bytesReturned = inputStream.read(buffer);
                if(fos == null)
                    Log.d(GlobalConstants.TAG_CLIENT_SOCKET, "fos is null");
                else {
                    fos.write(buffer);
                }
                cnt++;
//                if(bytesReturned >= 0)
//                    totalBytes += bytesReturned;
//                else {
//                    Log.d(GlobalConstants.TAG_CLIENT_SOCKET, "Last Iteration " + cnt);
//                    Log.d(GlobalConstants.TAG_CLIENT_SOCKET, "Exiting the while loop");
//                    break;
//                }
                // code to send the received bytes to the Receiver activity.
//                msgHandler.obtainMessage(GlobalConstants.PLAY_MUSIC, bytesReturned, -1, buffer).sendToTarget();
//                Log.d(GlobalConstants.TAG_CLIENT_SOCKET, "Sent data using mHandler");
                totalBytes = totalBytes + bytesReturned;
                if(cnt % 25 == 0){
                    Log.d(GlobalConstants.TAG_CLIENT_SOCKET, "Iteration " + cnt);
                }

            } catch (IOException e){
                Log.d(GlobalConstants.TAG_CLIENT_SOCKET, "Exception at log point 995");
                e.printStackTrace();
                break;
            } catch (NullPointerException npe) {
                Log.d(GlobalConstants.TAG_CLIENT_SOCKET, "Exception at log point 994");
                npe.printStackTrace();
                break;
            }
        }
        Log.d(GlobalConstants.TAG_CLIENT_SOCKET, "Outside the while loop");
    }

    public void write(byte[] bytes, int offset, int count){
        Log.d(GlobalConstants.TAG_SERVER_SOCKET, "Inside write() method");
        try{
            outputStream.write(bytes, offset, count);
            Log.d(GlobalConstants.TAG_SERVER_SOCKET, "Data bytes written to outputStream");
        } catch (IOException e){
            Log.d(GlobalConstants.TAG_SERVER_SOCKET, "Exception at log point 994");
            e.printStackTrace();
        }
    }

    public void cancel(String tag){
        Log.d(GlobalConstants.TAG_SERVER_SOCKET, "Attempting to close connection: " + tag);
//        try{
////            socket.close();
//        } catch (IOException e){
//            e.printStackTrace();
//        }
    }
}
