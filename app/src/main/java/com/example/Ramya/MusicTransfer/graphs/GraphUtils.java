package com.example.Ramya.MusicTransfer.graphs;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import com.example.Ramya.MusicTransfer.DatabaseHandler;
import com.example.Ramya.MusicTransfer.GlobalConstants;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.StringTokenizer;

public class GraphUtils {
    private Context applicationContext;
    private ArrayList<String> visitedNodes = new ArrayList<String>();
    boolean isProcessingDone = false;

    public GraphUtils(Context context){
        this.applicationContext = context;
        Log.d(GlobalConstants.TAG_GRAPH, "Constructor executed");
    }

    public HashMap<String,String> getShortestPathToAllNodes(){
        HashMap<String, String> pathMap = new HashMap<>();
        String currentDeviceAddr = getBTAddress();
        String currentDevice = null;
        if(currentDeviceAddr.equalsIgnoreCase(getBluetoothAddressFromName("D"))){
            currentDevice = "D";
            Log.d(GlobalConstants.TAG_GRAPH,"Getting shortest paths for D");
            pathMap.put("M", showShortestPath(currentDevice, "M"));
            pathMap.put("N6", showShortestPath(currentDevice, "N6"));
            pathMap.put("N7", showShortestPath(currentDevice, "N7"));
            pathMap.put("Y", showShortestPath(currentDevice, "Y"));
        }
        else if(currentDeviceAddr.equalsIgnoreCase(getBluetoothAddressFromName("M"))){
            currentDevice = "M";
            Log.d(GlobalConstants.TAG_GRAPH,"Getting shortest paths for M");
            pathMap.put("D", showShortestPath(currentDevice, "D"));
            pathMap.put("N6", showShortestPath(currentDevice, "N6"));
            pathMap.put("N7", showShortestPath(currentDevice, "N7"));
            pathMap.put("Y", showShortestPath(currentDevice, "Y"));
        }
        else if(currentDeviceAddr.equalsIgnoreCase(getBluetoothAddressFromName("N6"))){
            currentDevice = "N6";
            Log.d(GlobalConstants.TAG_GRAPH,"Getting shortest paths for N6");

            pathMap.put("D", showShortestPath(currentDevice, "D"));
            pathMap.put("M", showShortestPath(currentDevice, "M"));
            pathMap.put("N7", showShortestPath(currentDevice, "N7"));
            pathMap.put("Y", showShortestPath(currentDevice, "Y"));
        }
        else if(currentDeviceAddr.equalsIgnoreCase(getBluetoothAddressFromName("N7"))){
            currentDevice = "N7";
            Log.d(GlobalConstants.TAG_GRAPH,"Getting shortest paths for N7");

            pathMap.put("D", showShortestPath(currentDevice, "D"));
            pathMap.put("M", showShortestPath(currentDevice, "M"));
            pathMap.put("N6", showShortestPath(currentDevice, "N6"));
            pathMap.put("Y", showShortestPath(currentDevice, "Y"));
        }
        else if(currentDeviceAddr.equalsIgnoreCase(getBluetoothAddressFromName("Y"))){
            currentDevice = "Y";
            Log.d(GlobalConstants.TAG_GRAPH,"Getting shortest paths for Y");

            pathMap.put("D", showShortestPath(currentDevice, "D"));
            pathMap.put("M", showShortestPath(currentDevice, "M"));
            pathMap.put("N6", showShortestPath(currentDevice, "N6"));
            pathMap.put("N7", showShortestPath(currentDevice, "N7"));
        }
        return pathMap;
    }

    public String showShortestPath(String source, String destination){
        visitedNodes = new ArrayList<String>();
        isProcessingDone = false;
        visitedNodes.add(source);
        ArrayList<String> shortestPathList = getNeighbors(source, destination, new ArrayList<String>());
        Log.d(GlobalConstants.TAG_GRAPH, "Finding shortest path for " + source + " to reach " + destination);
        String shortestPath = "";
        for(int i = 0; i < shortestPathList.size(); i++)
            shortestPath += shortestPathList.get(i) + "-->";
        shortestPath = shortestPath.substring(0, shortestPath.lastIndexOf("-->"));
        Log.d(GlobalConstants.TAG_GRAPH,"ShortestPathList for " + source + " to " + destination + " : " + shortestPath);

        return shortestPath;
    }

    public ArrayList<String> getShortestPath(String source, String destination){
        visitedNodes = new ArrayList<String>();
        ArrayList<String> shortestPath = new ArrayList<>();
        isProcessingDone = false;
        visitedNodes.add(source);
        shortestPath = getNeighbors(source, destination, shortestPath);
        Log.d("GraphUtils", "Finding shortest path for " + source + " to reach " + destination);

        return shortestPath;
    }

    public ArrayList<String> getNeighbors(String node, String destination, ArrayList<String> result) {
        if(!isProcessingDone) {
            DatabaseHandler dbHandler = new DatabaseHandler(applicationContext);
            SQLiteDatabase db = dbHandler.getWritableDatabase();
            dbHandler.createGraphTable(db);
            db = dbHandler.getReadableDatabase();
            String neighbors = dbHandler.getNeighborsFromDB(node, db);

            if (neighbors.contains(destination)) {
                visitedNodes.add(destination);
                result = visitedNodes;
                isProcessingDone = true;
                return result;
            } else {
                StringTokenizer tok = new StringTokenizer(neighbors, "|");
                while (tok.hasMoreElements()) {
                    String currentNode = (String) tok.nextElement();
                    if (!visitedNodes.contains(currentNode) && !isProcessingDone) {
                        visitedNodes.add(currentNode);
                        result = visitedNodes;
                        ArrayList<String> tempResultArrayList = getNeighbors(currentNode, destination, result);
                        result = tempResultArrayList;
                    }
                }
            }
        }
        return result;
    }

    public static String getBTAddress(){
        BluetoothAdapter btAdapter = BluetoothAdapter.getDefaultAdapter();
        String btAddress = "";
        if(btAdapter == null)
            Log.d(GlobalConstants.TAG_GRAPH, "This device does not support Bluetooth");
        if(!btAdapter.isEnabled()) {
            Log.d(GlobalConstants.TAG_GRAPH, "Bluetooth is not enabled for this device");
            return "";
        }
        else {
            btAddress = btAdapter.getAddress();
        }
        return btAddress;
    }

    public static String getBluetoothAddressFromName(String deviceName){
        switch (deviceName){
            case "D" : return GlobalConstants.BT_ADDR_D;
            case "BT_ADDR_D" : return GlobalConstants.BT_ADDR_D;

            case "M": return GlobalConstants.BT_ADDR_M;
            case "BT_ADDR_M": return GlobalConstants.BT_ADDR_M;

            case "N6" : return GlobalConstants.BT_ADDR_N6;
            case "BT_ADDR_N6" : return GlobalConstants.BT_ADDR_N6;

            case "N7" : return GlobalConstants.BT_ADDR_N7;
            case "BT_ADDR_N7" : return GlobalConstants.BT_ADDR_N7;

            case "Y" : return GlobalConstants.BT_ADDR_Y;
            case "BT_ADDR_Y" : return GlobalConstants.BT_ADDR_Y;
        }
        return "";
    }

    public static String getNameFromBluetoothAddress(String bluetoothAddress){
        switch (bluetoothAddress){
            case GlobalConstants.BT_ADDR_D: return "D";
            case GlobalConstants.BT_ADDR_M: return "M";
            case GlobalConstants.BT_ADDR_N6: return "N6";
            case GlobalConstants.BT_ADDR_N7: return "N7";
            case GlobalConstants.BT_ADDR_Y: return "Y";
        }
        return "";
    }
}
