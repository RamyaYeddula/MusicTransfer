package com.example.Ramya.MusicTransfer.graphs;
import android.util.Log;
import com.example.Ramya.MusicTransfer.GlobalConstants;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class BreadthFirstSearch {
    private Graph graph;
    private int hourOfDay;
    private ArrayList<Vertex> visitedVertexes;

    public BreadthFirstSearch(int hourOfDay, Graph graph){
        this.hourOfDay = hourOfDay;
        this.graph = graph;
        this.visitedVertexes = new ArrayList<Vertex>();
    }

    public String executeBfs(Vertex source){
        visitedVertexes.add(source);
        ArrayList<Vertex> traversedNodes = new ArrayList<Vertex>();

        Queue<Vertex> q = new LinkedList<Vertex>();
        q.add(source);
        while (!q.isEmpty()){
            Vertex v = (Vertex) q.poll(); // similar to q.dequeue()
            traversedNodes.add(v);
            for(Vertex tempVertex : getAdjacentNodes(v)){
                if(!visitedVertexes.contains(tempVertex)) {
                    visitedVertexes.add(tempVertex);
                    q.add(tempVertex);
                }
            }
        }
        String logStr = "";
        for(Vertex v : traversedNodes){
            logStr += v.getName() + ", ";
        }
        Log.d(GlobalConstants.TAG_BFS, "BFS Traversal from " + source.getName() + ": " + logStr);
        return logStr.substring(0, logStr.lastIndexOf(","));
    }

    private ArrayList<Vertex> getAdjacentNodes(Vertex v) {
        ArrayList<Vertex> adjacentVertexes = new ArrayList<Vertex>();
        String logStr = "";
        for(Edge e : graph.getEdges()){
            if(e.getSource().equals(v)){
                logStr += e.getDestination().getName() + ", ";
                adjacentVertexes.add(e.getDestination());
            }
        }
        Log.d(GlobalConstants.TAG_BFS, "Adjacent vertexes for " + v.getName() + ": " + logStr);
        return adjacentVertexes;
    }
}
