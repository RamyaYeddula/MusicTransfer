package com.example.Ramya.MusicTransfer;
import android.os.Environment;
import java.io.File;
import java.util.UUID;

public class GlobalConstants {
    /************************************************************************************************/
    /* Bluetooth Addresses   */
    /************************************************************************************************/
    public static final String BT_ADDR_Y = "78:1F:DB:CD:C3:21";  // 
    public static final String BT_ADDR_M = "4C:3C:16:F4:A6:D4";  // 
    public static final String BT_ADDR_D = "34:31:11:71:36:EF";  // 
    public static final String BT_ADDR_N6 = "B0:C4:E7:D2:C3:0B"; //relay 2
    public static final String BT_ADDR_N7 = "00:17:EA:10:00:98"; //relay 1

    /************************************************************************************************/
    /* Global UUID   */
    /************************************************************************************************/
    public static final UUID SENDER_UUID = UUID.fromString("0000110E-0000-1000-8000-00805F9B345B");
    public static final UUID RELAY_UUID = UUID.fromString("0000110E-0000-1000-8000-00805F9B456C");
    public static final UUID RECEIVER_UUID = UUID.fromString("0000110E-0000-1000-8000-00805F9B678E");

    /************************************************************************************************/
    /* Logging Tags   */
    /************************************************************************************************/
    public static final String TAG_SERVER_SOCKET = "BluetoothSocket-Server";
    public static final String TAG_SERVER_SOCKET_STREAM = "BluetoothSocket-Server";

    public static final String TAG_CLIENT_SOCKET = "BluetoothSocket-Client";
    public static final String TAG_CLIENT_SOCKET_STREAM = "BluetoothSocket-Client";

    public static final String TAG_RELAY_SOCKET = "BluetoothSocket-Relay";
    public static final String TAG_RELAY_SOCKET_STREAM = "BluetoothSocket-Relay";

    public static final String TAG_MEDIA_PLAY = "BluetoothMediaPlay";
    public static final String TAG_DB = "DatabaseHandlerTag";
    public static final String TAG_GRAPH = "GraphUtils";

    public static final String TAG_DIJKSTRAS_ALGO = "DijkstrasAlgorithm";
    public static final String TAG_BFS = "BFS";

    /************************************************************************************************/
    /* Music Player constants   */
    /************************************************************************************************/
    public static final int PLAY_MUSIC = 1;

    public static final int BUFFER_SIZE = 256;

    public static final String TEMP_FILE_NAME = "abc";
    public static final String TEMP_FILE_EXTN = ".wav";
    public static final File SOURCE_FILE = new File(Environment.getExternalStorageDirectory()+ "/Download/" + TEMP_FILE_NAME + "" + TEMP_FILE_EXTN);


    /************************************************************************************************/
    /* Database constants   */
    /************************************************************************************************/
    public static final String GRAPH_TABLE_NAME = "Graph";
    public static final String GRAPH_COL_SOURCE = "SourceNode";
    public static final String GRAPH_COL_NEIGHBORS = "Neighbors";

    public static final String WEIGHTED_GRAPH_TABLE = "WeightedGraph";
    public static final String WEIGHTED_GRAPH_COL_ID = "ID";
    public static final String WEIGHTED_GRAPH_COL_SOURCE = "Source";
    public static final String WEIGHTED_GRAPH_COL_DEST = "Destination";
    public static final String WEIGHTED_GRAPH_COL_WEIGHT = "Weight";


    public static final Integer WEIGHTED_GRAPH_DIST_MIN = 0;
    public static final Integer WEIGHTED_GRAPH_DIST_MAX = 9999;
}
