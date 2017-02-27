package com.example.Ramya.MusicTransfer.graphs;

import android.util.Log;

import com.example.Ramya.MusicTransfer.GlobalConstants;

import java.util.ArrayList;
import java.util.List;

public class AvailabilityGraph {
    private List<Edge> edges;
    private List<Vertex> vertexes;
    private static final int TOTAL_CASES = 1;
    private int hourOfDay = 0;

    public AvailabilityGraph(int hourOfDay){
        edges = new ArrayList<Edge>();
        vertexes = new ArrayList<Vertex>();
        this.hourOfDay = hourOfDay;
//        this.hourOfDay = TOTAL_CASES;
    }


    private void addEdge(Vertex source, Vertex dest,
                         int distance) {
        Edge lane = new Edge(source.getName() + "-" + dest.getName() ,
                source,
                dest,
                distance);
        edges.add(lane);
    }

    public Graph getAvailableGraph(){
//        int hourOfDay = 0;
//        Calendar calendar = Calendar.getInstance();
//        calendar.setTimeInMillis(System.currentTimeMillis());
//        hourOfDay = calendar.get(calendar.HOUR_OF_DAY);

        addVertexes();
        addEdges();
        return new Graph(vertexes, edges);
    }

    private void addVertexes() {
        if(isNodeAvailable("D", hourOfDay)){
            vertexes.add(new Vertex("D"));
        }

        if(isNodeAvailable("M", hourOfDay)){
            vertexes.add(new Vertex("M"));
        }

        if(isNodeAvailable("N6", hourOfDay)){
            vertexes.add(new Vertex("N6"));
        }

        if(isNodeAvailable("N7", hourOfDay)){
            vertexes.add(new Vertex("N7"));
        }

        if(isNodeAvailable("Y", hourOfDay)) {
            vertexes.add(new Vertex("Y"));
        }
    }

    private void addEdges() {
        if(isNodeAvailable("D", hourOfDay)){
            Vertex source = getVertexByName("D");

            if (isNodeAvailable("M", hourOfDay))
                addEdge(source, getVertexByName("M"), 3);
            if (isNodeAvailable("N6", hourOfDay))
                addEdge(source, getVertexByName("N6"), 5);
            if (isNodeAvailable("Y", hourOfDay))
                addEdge(source, getVertexByName("Y"), 4);
        }

        if(isNodeAvailable("M", hourOfDay)){
            Vertex source = getVertexByName("M");

            if (isNodeAvailable("D", hourOfDay))
                addEdge(source, getVertexByName("D"), 3);
            if (isNodeAvailable("N6", hourOfDay))
                addEdge(source, getVertexByName("N6"), 2);
        }

        if(isNodeAvailable("N6", hourOfDay)){
            Vertex source = getVertexByName("N6");

            if (isNodeAvailable("M", hourOfDay))
                addEdge(source, getVertexByName("M"), 2);
            if (isNodeAvailable("D", hourOfDay))
                addEdge(source, getVertexByName("D"), 5);
            if (isNodeAvailable("N7", hourOfDay))
                addEdge(source, getVertexByName("N7"), 1);
        }

        if(isNodeAvailable("N7", hourOfDay)){
            Vertex source = getVertexByName("N7");

            if (isNodeAvailable("N6", hourOfDay))
                addEdge(source, getVertexByName("N6"), 1);
            if (isNodeAvailable("Y", hourOfDay))
                addEdge(source, getVertexByName("Y"), 3);
        }

        if(isNodeAvailable("Y", hourOfDay)) {
            Vertex source = getVertexByName("Y");

            if (isNodeAvailable("D", hourOfDay))
                addEdge(source, getVertexByName("D"), 4);
            if (isNodeAvailable("N7", hourOfDay))
                addEdge(source, getVertexByName("N7"), 3);
        }
    }

    private Vertex getVertexByName(String name) {
        for(Vertex vertex : vertexes){
            if(vertex.getName().equalsIgnoreCase(name))
                return vertex;
        }
        return null;
    }

    public static boolean isNodeAvailable(String node, int hourOfDay){
        switch (hourOfDay % TOTAL_CASES){
            case 0:
                Log.d(GlobalConstants.TAG_DIJKSTRAS_ALGO, "All nodes are available");
                return true;

            case 1:
                if(node.equalsIgnoreCase("D")) {
                    Log.d(GlobalConstants.TAG_DIJKSTRAS_ALGO, "Node D is unavailable");
                    return false;
                }
                else
                    return true;

            case 2:
                if (node.equalsIgnoreCase("N6")) {
                    Log.d(GlobalConstants.TAG_DIJKSTRAS_ALGO, "Node N6 is unavailable");
                    return false;
                }
                else
                    return true;

            case 3:
                if (node.equalsIgnoreCase("N7")) {
                    Log.d(GlobalConstants.TAG_DIJKSTRAS_ALGO, "Node N7 is unavailable");
                    return false;
                }
                else
                    return true;

            /*
            case 4:
                if(node.equalsIgnoreCase("M")){
                    Log.d(GlobalConstants.TAG_DIJKSTRAS_ALGO, "Node M is unavailable");
                    return false;
                }
                else
                    return true;

            case 5:
                if (node.equalsIgnoreCase("Y")){
                    Log.d(GlobalConstants.TAG_DIJKSTRAS_ALGO, "Node Y is unavailable");
                    return false;
                }
                else
                    return true;
            */

            default:
                Log.d(GlobalConstants.TAG_DIJKSTRAS_ALGO, "Default case: All nodes are available");
                return true;
        }
    }
}
