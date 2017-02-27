package com.example.Ramya.MusicTransfer;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.File;
import java.io.FileInputStream;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import com.example.Ramya.MusicTransfer.graphs.AvailabilityGraph;
import com.example.Ramya.MusicTransfer.graphs.DijkstraAlgorithm;
import com.example.Ramya.MusicTransfer.graphs.Graph;
import com.example.Ramya.MusicTransfer.graphs.GraphUtils;
import com.example.Ramya.MusicTransfer.graphs.Vertex;
import com.example.Ramya.mehrab_sample_code.R;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class Receiver extends Activity{
    private BluetoothAdapter bluetooth_adapter;
    private final static int REQUEST_ENABLE_BT = 1;
    private TextView conState;
    private static TextView readyFile;
    private ListView file_list;
    public String[] pairlist;
    static TextView file_len;
    BluetoothSocket socket;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receiver);
        conState = (TextView) findViewById(R.id.connection);
        readyFile = (TextView) findViewById(R.id.textViewFileReady);
        readyFile.setVisibility(View.INVISIBLE);
        pairlist = new String[10];
//        file_len = (TextView) findViewById(R.id.textViewFileLength);

        file_list = (ListView) findViewById(R.id.listViewFiles);
        bluetooth_adapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetooth_adapter == null) {
            // Device does not support Bluetooth
        }
        if (!bluetooth_adapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        }
        DatabaseHandler db = new DatabaseHandler(this);
        //List<Music> music = db.getAllMusic();

        //showpairlist();
        // DatabaseHandler db = new DatabaseHandler(this);
        // Inserting Contacts
        Log.d("Insert: ", "Inserting ..");
        db.addMusic(new Music("Thriller.mp3", ""));
        db.addMusic(new Music("Death.mp3", ""));
        db.addMusic(new Music("Paradise.mp3", ""));
        db.addMusic(new Music("ForYor.mp3", ""));
        Log.d("Reading: ", "Reading all contacts..");
        List<Music> music = db.getAllMusic();
        if(music == null) {
            String log = "Music list is NULL";
            Log.d(GlobalConstants.TAG_CLIENT_SOCKET_STREAM, log);

        }
        for (Music cn : music) {
            String log = "Id: "+cn.getID()+" ,Name: " + cn.getName() + " ,Owner: " + cn.getOwner();
            // Writing Contacts to log
            Log.d("Music", log);
        }

        //List<Music> datadisplay = db.getAllMusic();
        ArrayAdapter<Music> adapter = new ArrayAdapter<Music>(this, android.R.layout.simple_list_item_1, music);
        file_list.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        file_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String item = ((TextView) view).getText().toString();
                Toast.makeText(getBaseContext(), item, Toast.LENGTH_LONG).show();

                // old code: without using sockets
                //Starting the bluetooth

                // ToDo: check if the device is connected to any node. If yes, get the song from that node.
//                BluetoothAdapter btAdapter = BluetoothAdapter.getDefaultAdapter();
//                String packageName = null;
//                String className = null;
//                boolean found = false;
//                String site = "http://www.sprii.net/db/getItem.php?pid=" + item + "/";
//                try {
//                    URL url = new URL(site);
//                    HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
//                    InputStream in = new BufferedInputStream(urlConnection.getInputStream());
//                    BufferedReader reader = new BufferedReader(new InputStreamReader(in));
//                    StringBuilder result = new StringBuilder();
//                    String line;
//                    while((line = reader.readLine()) != null) result.append(line);
//
//
//                }
//                catch (Exception e) {
//                }
                //Bundle data = new Bundle();
                //data.putString("key",item);
                //Intent intent = new Intent(Receiver.this,Relay.class);
//                Intent intent = new Intent();
//                intent.setAction(Intent.ACTION_SEND);
//                intent.putExtra(Intent.EXTRA_TEXT, item);
//                intent.setType("text/plain");
//                Log.d("Data:", item);
//                packageName = "com.android.bluetooth";
//                className = "com.android.bluetooth.opp.BluetoothOppLauncherActivity";
//                intent.setClas    1sName(packageName, className);
//                startActivity(intent);
            }
        });

        final Button fab = (Button) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();

                MediaPlayer mediaPlayer = new MediaPlayer();
                File tempFile = GlobalConstants.SOURCE_FILE;
                FileInputStream fis = null;
                try {
                    fis = new FileInputStream(tempFile);
//                    if(mediaPlayer == null) {
                        mediaPlayer = new MediaPlayer();
                        Log.d(GlobalConstants.TAG_MEDIA_PLAY, "Created new instance of MediaPlayer");

                        mediaPlayer.setDataSource(fis.getFD());
                        Log.d(GlobalConstants.TAG_MEDIA_PLAY, "DataSource set from file descriptor");

                        mediaPlayer.prepare();
                        Log.d(GlobalConstants.TAG_MEDIA_PLAY, "player state: prepared");

                        mediaPlayer.start();
//                    }
//                    else{
////                        mediaPlayer.setDataSource(fis.getFD());
////                        Log.d(GlobalConstants.TAG_MEDIA_PLAY, "DataSource set from file descriptor");
//
//                        mediaPlayer.prepare();
//                        Log.d(GlobalConstants.TAG_MEDIA_PLAY, "player state: prepared");
//                    }
//
//                    if(mediaPlayer.isPlaying()) {
//                        mediaPlayer.stop();
//                        Log.d(GlobalConstants.TAG_MEDIA_PLAY, "Stopped");
//                        mediaPlayer.reset();
//                        fab.setText("PLAY");
//                    }
//                    else {
//                        mediaPlayer.prepare();
//                        mediaPlayer.start();
//                        Log.d(GlobalConstants.TAG_MEDIA_PLAY, "Playing music");
//
//                        fab.setTag("PAUSE");
//                    }
                } catch (FileNotFoundException e) {
                    Log.d(GlobalConstants.TAG_MEDIA_PLAY, "FileNotFound\n" + e.getStackTrace());

                    e.printStackTrace();
                } catch (IOException e) {
                    Log.d(GlobalConstants.TAG_MEDIA_PLAY, "IOException\n" + e.getStackTrace());

                    e.printStackTrace();
                } catch (Exception e) {
                    Log.d(GlobalConstants.TAG_MEDIA_PLAY, "Other exception");
                    e.printStackTrace();
                }
            }
        });

        //aud_manager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

        //aud_manager.startBluetoothSco();

        //aud_manager.setBluetoothScoOn(true);

        //BluetoothSocket Client code starts


        Thread clientThread = new Thread(){
            @Override
            public void run() {
                try {
                    BluetoothDevice device = null;
                    Set<BluetoothDevice> pairedDevices = bluetooth_adapter.getBondedDevices();

                    int hourOfDay = 0;
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTimeInMillis(System.currentTimeMillis());
                    hourOfDay = calendar.get(calendar.HOUR_OF_DAY);

                    AvailabilityGraph availablilityGraph = new AvailabilityGraph(hourOfDay);
                    Graph graph = availablilityGraph.getAvailableGraph();

                    DijkstraAlgorithm dijkstra = new DijkstraAlgorithm(graph);

                    dijkstra.execute(Relay.getVertexByName("M", graph));

                    LinkedList<Vertex> shortestPath = dijkstra.getPath(Relay.getVertexByName("Y", graph));

                    Vertex prevDevice = getPreviousDevice(shortestPath, "Y", graph);

                    String btAddr = GraphUtils.getBluetoothAddressFromName(prevDevice.getName());

                    // If there are paired devices
                    if (pairedDevices.size() > 0) {
                        // Loop through paired devices
                        for (BluetoothDevice tmpDevice : pairedDevices) {
                            // Add the name and address to an array adapter to show in a ListView
                            Log.d("PairedDeviceClient", tmpDevice.getName() + " -- " + tmpDevice.getAddress() + " -- " + tmpDevice.getBondState());
                            // XXX
                            // for using common relay (3 devices) this code should be changed to BT ADDR of the device to which sender also points
                            if(tmpDevice.getAddress().equalsIgnoreCase(btAddr)){
                                device = tmpDevice;
                                break;
                            }
                        }
                    }

                    Log.d(GlobalConstants.TAG_CLIENT_SOCKET, "Bonded BT device: " + device.getName() + " -- status: " +
                            device.getBondState());



                    // For one to one connection, un-comment this line
//                    socket = device.createInsecureRfcommSocketToServiceRecord(GlobalConstants.SENDER_UUID);
                    // For relay connection, un-comment this line
                    socket = device.createInsecureRfcommSocketToServiceRecord(GlobalConstants.RECEIVER_UUID);
                    if (device.getBondState() == BluetoothDevice.BOND_BONDED) {
                        Log.d(GlobalConstants.TAG_CLIENT_SOCKET, "Device status: BOND_BONDED");
                        bluetooth_adapter.cancelDiscovery();
                    } else if (device.getBondState() == BluetoothDevice.BOND_BONDING)
                        Log.d(GlobalConstants.TAG_CLIENT_SOCKET, "Device status: BOND_BONDING");
                    else if (device.getBondState() == BluetoothDevice.BOND_NONE) {
                        Log.d(GlobalConstants.TAG_CLIENT_SOCKET, "Device status: BOND_NONE");
                    } else
                        Log.d(GlobalConstants.TAG_CLIENT_SOCKET, "Device status:" + device.getBondState());


                    try {
                        Log.d(GlobalConstants.TAG_CLIENT_SOCKET, "Attempting socket.connect()");
                        if(!socket.isConnected())
                            socket.connect();
                        else
                            Log.d(GlobalConstants.TAG_CLIENT_SOCKET, "No need to connect again. Connection still exists.");
                        Log.d(GlobalConstants.TAG_CLIENT_SOCKET, "Connection established!");
                        manageConnectedSocket(socket);

                    } catch (Exception e) {
                        Log.d(GlobalConstants.TAG_CLIENT_SOCKET, "Exception at log point 998");
                        e.printStackTrace();
                    } finally {
                        Log.d(GlobalConstants.TAG_CLIENT_SOCKET, "Closing socket connection in Receiver: 1");
//                        socket.close();
                    }
                    //            }
                } catch (Exception e) {
                    Log.d(GlobalConstants.TAG_CLIENT_SOCKET, "Exception at log point 997");
                    e.printStackTrace();
                }
            }
        };
        clientThread.start();
    }

    private Vertex getPreviousDevice(LinkedList<Vertex> path, String currDevice, Graph graph){
        int index = 0;
        for(int i = 0; i < path.size(); i++){
            if(Relay.getVertexByName(currDevice, graph).getName().equalsIgnoreCase(path.get(i).getName())) {
                index = i;
                break;
            }
        }
        return path.get(index - 1);
//        return new String[] {path.get(index - 1), path.get(index + 1)};
    }

    private void manageConnectedSocket(BluetoothSocket socket) {
        Log.d(GlobalConstants.TAG_CLIENT_SOCKET, "Inside manageConnectedSocket()");
        ConnectedSocketTread conn = new ConnectedSocketTread(socket, null);
        conn.start();
//        conn.cancel();
    }

}

