package com.example.Ramya.MusicTransfer;
import com.example.Ramya.MusicTransfer.graphs.AvailabilityGraph;
import com.example.Ramya.MusicTransfer.graphs.BreadthFirstSearch;
import com.example.Ramya.MusicTransfer.graphs.DijkstraAlgorithm;
import com.example.Ramya.MusicTransfer.graphs.GraphUtils;
import com.example.Ramya.MusicTransfer.graphs.Graph;
import com.example.Ramya.MusicTransfer.graphs.Vertex;
import com.example.Ramya.mehrab_sample_code.R;

import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.DialogInterface;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.Menu;
import android.view.View;
import android.widget.Toast;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Set;

public class MainActivity extends Activity {

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    @SuppressLint("CommitPrefEdits")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(this);
        editor = sharedPreferences.edit();

        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if(bluetoothAdapter == null){
            Toast.makeText(getApplicationContext(), "This device does not support bluetooth", Toast.LENGTH_LONG).show();
            return;
        }
        if(!bluetoothAdapter.isEnabled()){
//            Toast.makeText(getApplicationContext(), "Please enable bluetooth", Toast.LENGTH_LONG).show();
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Bluetooth").setMessage("Please enable bluetooth")
                    .setCancelable(false).setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
            // Todo: code to ask to enable bluetooth
        }
        else{
            GraphUtils graph = new GraphUtils(getApplicationContext());
            HashMap<String, String> map = graph.getShortestPathToAllNodes();

            String shortestPaths = "";
            Set<String> keySet = map.keySet();
            Iterator iter = keySet.iterator();
            while(iter.hasNext()){
                String key = (String) iter.next();
                shortestPaths += "Shortest path from this device to " + key + ": " + map.get(key) + "\n\n";
            }

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Shortest path: Without weights").setMessage(shortestPaths)
                    .setCancelable(false).setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    showDijkstrasPath();
                }
            });
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
//            showDijkstrasPath();
        }
    }

    public void showDijkstrasPath(){

        int hourOfDay = 0;
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        hourOfDay = calendar.get(calendar.HOUR_OF_DAY);

        AvailabilityGraph availablilityGraph = new AvailabilityGraph(hourOfDay);
        Graph graph = availablilityGraph.getAvailableGraph();

        DijkstraAlgorithm dijkstra = new DijkstraAlgorithm(graph);

        BreadthFirstSearch bfs = new BreadthFirstSearch(hourOfDay, graph);

        String currentDevice = GraphUtils.getNameFromBluetoothAddress(GraphUtils.getBTAddress());
        String bfsTraversal = bfs.executeBfs(getVertexByName(currentDevice, graph));

        dijkstra.execute(getVertexByName(currentDevice, graph));
        String pathStr = "BFS traversal from vertex " + currentDevice + ": " + bfsTraversal + "\n\n--------------\n\n";

        if(!currentDevice.equalsIgnoreCase("D") && AvailabilityGraph.isNodeAvailable("D", hourOfDay)) {
            pathStr += currentDevice + " to D: ";
            LinkedList<Vertex> path = dijkstra.getPath(getVertexByName("D", graph));
            String tempStr = "";
            for(Vertex node : path){
                tempStr += node.getName() + " --> ";
            }
            pathStr += tempStr.substring(0, tempStr.lastIndexOf(" --> ")) + "\n\n";
        }
        if(!currentDevice.equalsIgnoreCase("M") && AvailabilityGraph.isNodeAvailable("M", hourOfDay)) {
            pathStr += currentDevice + " to M: ";
            LinkedList<Vertex> path = dijkstra.getPath(getVertexByName("M", graph));
            String tempStr = "";
            for(Vertex node : path){
                tempStr += node.getName() + " --> ";
            }
            pathStr += tempStr.substring(0, tempStr.lastIndexOf(" --> ")) + "\n\n";
        }
        if(!currentDevice.equalsIgnoreCase("N6") && AvailabilityGraph.isNodeAvailable("N6", hourOfDay)) {
            pathStr += currentDevice + " to N6: ";
            LinkedList<Vertex> path = dijkstra.getPath(getVertexByName("N6", graph));
            String tempStr = "";
            for(Vertex node : path){
                tempStr += node.getName() + " --> ";
            }
            pathStr += tempStr.substring(0, tempStr.lastIndexOf(" --> ")) + "\n\n";
        }
        if(!currentDevice.equalsIgnoreCase("N7") && AvailabilityGraph.isNodeAvailable("N7", hourOfDay)) {
            pathStr += currentDevice + " to N7: ";
            LinkedList<Vertex> path = dijkstra.getPath(getVertexByName("N7", graph));
            String tempStr = "";
            for(Vertex node : path){
                tempStr += node.getName() + " --> ";
            }
            pathStr += tempStr.substring(0, tempStr.lastIndexOf(" --> ")) + "\n\n";
        }
        if(!currentDevice.equalsIgnoreCase("Y") && AvailabilityGraph.isNodeAvailable("Y", hourOfDay)) {
            pathStr += currentDevice + " to Y: ";
            LinkedList<Vertex> path = dijkstra.getPath(getVertexByName("Y", graph));
            String tempStr = "";
            for(Vertex node : path){
                tempStr += node.getName() + " --> ";
            }
            pathStr += tempStr.substring(0, tempStr.lastIndexOf(" --> ")) + "\n\n";
        }

        pathStr = pathStr.substring(0, pathStr.lastIndexOf("\n\n"));

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Using Dijkstra's Algorithm").setMessage(pathStr)
                .setCancelable(false).setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
//                showDijkstrasPath();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private Vertex getVertexByName(String currentDevice, Graph graph) {
        for (Vertex v : graph.getVertexes()){
            if (v.getName().equalsIgnoreCase(currentDevice))
                return v;
        }
        throw new RuntimeException("Vertex not found in MainActivity.java");
    }


    public void senderClick(View view){
        Toast.makeText(getBaseContext(), "Role changed to " + "Sender", Toast.LENGTH_SHORT).show();
        editor.putString("role", "sender");
        editor.commit();
        Intent intent = new Intent(this, Sender.class);
        startActivity(intent);
    }

    public void recieverClick(View view){
        Toast.makeText(getBaseContext(), "Role changed to " + "reciever", Toast.LENGTH_SHORT).show();
        editor.putString("role", "receiver");
        editor.commit();
        Intent intent = new Intent(this, Receiver.class);
        startActivity(intent);
    }

    public void relayClick(View view){
        Toast.makeText(getBaseContext(), "Role changed to " + "Relay", Toast.LENGTH_SHORT).show();
        editor.putString("role", "relay");
        editor.commit();
        Intent intent = new Intent(this, Relay.class);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

}
