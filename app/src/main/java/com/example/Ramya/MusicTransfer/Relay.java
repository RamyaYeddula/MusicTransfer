package com.example.Ramya.MusicTransfer;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.Set;

import com.example.Ramya.MusicTransfer.graphs.AvailabilityGraph;
import com.example.Ramya.MusicTransfer.graphs.DijkstraAlgorithm;
import com.example.Ramya.MusicTransfer.graphs.Graph;
import com.example.Ramya.MusicTransfer.graphs.GraphUtils;
import com.example.Ramya.MusicTransfer.graphs.Vertex;
import com.example.Ramya.mehrab_sample_code.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;

public class Relay extends Activity {

    private BluetoothServerSocket relayServerSocket;
    private BluetoothSocket relayClientSocket;
    private BluetoothSocket senderSocket;
    private BluetoothAdapter bluetoothAdapter;
    public Context c;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_relay);

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        if(bluetoothAdapter == null){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Relay").setMessage("This device does not support Bluetooth")
                    .setCancelable(false).setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    return;
                }
            });
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        }
        else if(!bluetoothAdapter.isEnabled()){
            Log.d(GlobalConstants.TAG_RELAY_SOCKET, "Bluetooth is disabled");
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Relay").setMessage("Bluetooth is disabled.\nPlease enable Bluetooth on this device.")
                    .setCancelable(true).setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        }
        else {
            Log.d(GlobalConstants.TAG_RELAY_SOCKET, "Relay processing begins...");

            String currentDevice = GraphUtils.getNameFromBluetoothAddress(bluetoothAdapter.getAddress());
//            GraphUtils graph = new GraphUtils(this.getApplicationContext());
//            ArrayList<String> shortestPath = graph.getShortestPath("M", "Y");

            int hourOfDay = 0;
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(System.currentTimeMillis());
            hourOfDay = calendar.get(calendar.HOUR_OF_DAY);

            AvailabilityGraph availablilityGraph = new AvailabilityGraph(hourOfDay);
            Graph graph = availablilityGraph.getAvailableGraph();
            DijkstraAlgorithm dijkstra = new DijkstraAlgorithm(graph);
            dijkstra.execute(getVertexByName("M", graph));
            LinkedList<Vertex> shortestPath = dijkstra.getPath(getVertexByName("Y", graph));

            Vertex[] prevAndNextDevice = getPreviousAndNextDevice(shortestPath, currentDevice, graph);
//            String[] prevAndNextDevice = getPreviousAndNextDevice(shortestPath, currentDevice);
            startRelayProcessing(prevAndNextDevice);
        }
    }

    public static Vertex getVertexByName(String currentDevice, Graph graph) {
        for (Vertex v : graph.getVertexes()){
            if (v.getName().equalsIgnoreCase(currentDevice))
                return v;
        }
        throw new RuntimeException("Vertex not found in MainActivity.java");
    }

//    private void startRelayProcessing(final String[] prevAndNextDevice) {
    private void startRelayProcessing(final Vertex[] prevAndNextDevice) {
        Thread socketsThread = new Thread(){
            @Override
            public void run() {
                establishSocketConnection(prevAndNextDevice[0].getName(), false);
                senderSocket = establishSocketConnection(prevAndNextDevice[1].getName(), true);
                if(senderSocket != null)
                    manageRelayConnection();
            }
        };
        socketsThread.start();
    }

    private void manageRelayConnection() {
        Log.d(GlobalConstants.TAG_RELAY_SOCKET, "Starting manageRelayConnection()");
        final Thread relayThread = new Thread(){
            @Override
            public void run() {
                try {
                    InputStream receiverIn = relayClientSocket.getInputStream();
//                    OutputStream receiverOut = relayClientSocket.getOutputStream();
                    Log.d(GlobalConstants.TAG_RELAY_SOCKET, "Input and output streams created for relay receiver");

//                    InputStream senderIn = senderSocket.getInputStream();
                    OutputStream senderOut = senderSocket.getOutputStream();
                    Log.d(GlobalConstants.TAG_RELAY_SOCKET, "Input and output streams created for relay sender");

                    byte[] bufferIn = new byte[GlobalConstants.BUFFER_SIZE];
                    int bytesReturned = 0, cnt = 0, totalBytes = 0;

                    while(bytesReturned != -1 && bluetoothAdapter.isEnabled()) {
//                        if (bluetoothAdapter.isEnabled()) {
                        bytesReturned = receiverIn.read(bufferIn);
                        Log.d(GlobalConstants.TAG_RELAY_SOCKET, "Received " + bytesReturned + " bytes from the sender");

                        totalBytes += bytesReturned;
                        cnt++;
                        senderOut.write(bufferIn);
                        Log.d(GlobalConstants.TAG_RELAY_SOCKET, "Relay -- sent bytes -- " + totalBytes);
                        if(!bluetoothAdapter.isEnabled())
                            break;
//                        } else {
                    }
                    Log.d(GlobalConstants.TAG_RELAY_SOCKET, "Total bytes processed : " + totalBytes);
                    if(!bluetoothAdapter.isEnabled()){
                        Log.d(GlobalConstants.TAG_RELAY_SOCKET, "Bluetooth disabled");
                        alert("Relay", "Bluetooth turned off!");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
        relayThread.start();
    }

    private void alert(final String title, final String msg){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                AlertDialog.Builder builder = new AlertDialog.Builder(Relay.this);
                builder.setTitle(title).setMessage(msg)
                        .setCancelable(false).setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
//                        return;
                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });
    }

    private Vertex[] getPreviousAndNextDevice(LinkedList<Vertex> path, String currDevice, Graph graph){
        int index = 0;
        for(int i = 0; i < path.size(); i++){
            if(getVertexByName(currDevice, graph).getName().equalsIgnoreCase(path.get(i).getName())) {
                index = i;
                break;
            }
        }



        Vertex prev = null;
        if (index > 0 && path.get(index - 1) != null)
            prev = path.get(index - 1);

        Vertex next = null;
        if (index < path.size() && path.get(index + 1) != null)
            next = path.get(index + 1);

        return new Vertex[] {prev, next};
//        return new String[] {path.get(index - 1), path.get(index + 1)};
    }

    private String[] getPreviousAndNextDevice(ArrayList<String> path, String currDevice){
        int index = 0;
        for(int i = 0; i < path.size(); i++){
            if(path.get(i).equalsIgnoreCase(currDevice)){
                index = i;
                break;
            }
        }
        return new String[] {path.get(index - 1), path.get(index + 1)};
    }

    private BluetoothSocket establishSocketConnection(String deviceToBeConnected, boolean isSender){
        BluetoothDevice device = getPairedDevice(deviceToBeConnected);
        if(device.getBondState() == device.BOND_BONDED){
            try {
                if(!isSender) {
                    if (deviceToBeConnected.equalsIgnoreCase("M")) {
                        relayClientSocket = device.createInsecureRfcommSocketToServiceRecord(GlobalConstants.SENDER_UUID);
                        Log.d(GlobalConstants.TAG_RELAY_SOCKET, "Created insecure RFCOMM socket as receiver with source using SENDER_UUID");
                    } else {
                        relayClientSocket = device.createInsecureRfcommSocketToServiceRecord(GlobalConstants.RELAY_UUID);
                        Log.d(GlobalConstants.TAG_RELAY_SOCKET, "Created insecure RFCOMM socket as receiver with second relay using RELAY_UUID ");
                    }
                    relayClientSocket.connect();
                    Log.d(GlobalConstants.TAG_RELAY_SOCKET, "Socket connection established as receiver");
                    return null;
                }
                else{
                    if(deviceToBeConnected.equalsIgnoreCase("Y")) {
                        relayServerSocket = bluetoothAdapter.listenUsingInsecureRfcommWithServiceRecord("LastConnection", GlobalConstants.RECEIVER_UUID);
                        Log.d(GlobalConstants.TAG_RELAY_SOCKET, "Started listening for socket connection from destination using RECEIVER_UUID");
                    }
                    else{
                        relayServerSocket = bluetoothAdapter.listenUsingInsecureRfcommWithServiceRecord("IntermediateConnection", GlobalConstants.RELAY_UUID);
                        Log.d(GlobalConstants.TAG_RELAY_SOCKET, "Started listening for socket connection from first relay using RELAY_UUID");
                    }
                    BluetoothSocket senderSocket = relayServerSocket.accept();
                    Log.d(GlobalConstants.TAG_RELAY_SOCKET, "Connection accepted as relay-sender");
                    return senderSocket;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    private BluetoothDevice getPairedDevice(String deviceName){
        BluetoothDevice device = null;
        Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();
        if(pairedDevices.size() > 0){
            for(BluetoothDevice tempDevice : pairedDevices){
                if (tempDevice.getAddress().equalsIgnoreCase(
                        GraphUtils.getBluetoothAddressFromName(deviceName))) {
                    device = tempDevice;
                    break;
                }
            }
        }
        return device;
    }

}

