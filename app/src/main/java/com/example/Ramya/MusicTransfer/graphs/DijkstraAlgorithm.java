package com.example.Ramya.MusicTransfer.graphs;

import android.util.Log;
import com.example.Ramya.MusicTransfer.GlobalConstants;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Set;
public class DijkstraAlgorithm {
    private final List<Vertex> nodes;
    private final List<Edge> edges;
    private Set<Vertex> settledNodes;
    private Set<Vertex> unSettledNodes;
    private Map<Vertex, Vertex> predecessors;
    private Map<Vertex, Integer> distance;

    public DijkstraAlgorithm(Graph graph) {
        // create a copy of the array so that we can operate on this array
        this.nodes = new ArrayList<Vertex>(graph.getVertexes());
        this.edges = new ArrayList<Edge>(graph.getEdges());

        Log.d(GlobalConstants.TAG_DIJKSTRAS_ALGO, "Constructor executed");
    }

    public void execute(Vertex source) {
        settledNodes = new HashSet<Vertex>();
        unSettledNodes = new HashSet<Vertex>();
        distance = new HashMap<Vertex, Integer>();
        predecessors = new HashMap<Vertex, Vertex>();
        distance.put(source, 0);
        unSettledNodes.add(source);
        while (unSettledNodes.size() > 0) {
            Log.d(GlobalConstants.TAG_DIJKSTRAS_ALGO, "Getting minimum node for vertex " + source.getName());
            Vertex node = getMinimum(unSettledNodes);
            settledNodes.add(node);
            unSettledNodes.remove(node);
            Log.d(GlobalConstants.TAG_DIJKSTRAS_ALGO, "Finding minimal distances for vertex " + node.getName());
            findMinimalDistances(node);
        }
    }

    private void findMinimalDistances(Vertex node) {
        Log.d(GlobalConstants.TAG_DIJKSTRAS_ALGO, "Getting adjacent nodes");
        List<Vertex> adjacentNodes = getNeighbors(node);
        for (Vertex target : adjacentNodes) {
            int distTarget = getShortestDistance(target);
            int distNode = getShortestDistance(node);
            int tempDist = getDistance(node, target);

            Log.d(GlobalConstants.TAG_DIJKSTRAS_ALGO, "Target Dist: " + tempDist + ", Node dist: " + distNode + "+ tempDist: " + tempDist) ;

            if (distTarget > distNode + tempDist) {
                distance.put(target, distNode + tempDist);
                Log.d(GlobalConstants.TAG_DIJKSTRAS_ALGO, "Added predecessor to " + target.getName() + ": " + node.getName());
                predecessors.put(target, node);
                Log.d(GlobalConstants.TAG_DIJKSTRAS_ALGO, "Added unsettled node: " + target.getName());
                unSettledNodes.add(target);
            }
        }

    }

    private int getDistance(Vertex node, Vertex target) {
        for (Edge edge : edges) {
            if (edge.getSource().equals(node)
                    && edge.getDestination().equals(target)) {
                return edge.getWeight();
            }
        }
        throw new RuntimeException("Should not happen");
    }

    private List<Vertex> getNeighbors(Vertex node) {
        List<Vertex> neighbors = new ArrayList<Vertex>();
        for (Edge edge : edges) {
            if (edge.getSource().equals(node)
                    && !isSettled(edge.getDestination())) {
                neighbors.add(edge.getDestination());
            }
        }
        Log.d(GlobalConstants.TAG_DIJKSTRAS_ALGO, "No of neighbors of vertex " + node.getName() + ": " + neighbors.size());
        return neighbors;
    }

    private Vertex getMinimum(Set<Vertex> vertexes) {
        Vertex minimum = null;
        for (Vertex vertex : vertexes) {
            if (minimum == null) {
                minimum = vertex;
            } else {
                int distVertex = getShortestDistance(vertex);
                int distMin = getShortestDistance(minimum);
                Log.d(GlobalConstants.TAG_DIJKSTRAS_ALGO, "Shortest distance of vertex " + vertex.getName() + ": " + distVertex +
                        "AND min dist of min vertex " + minimum.getName() + " : " + distMin);
                if (distVertex < distMin) {
                    minimum = vertex;
                }
            }
        }
        Log.d(GlobalConstants.TAG_DIJKSTRAS_ALGO, "Minimum vertex : " + minimum.getName());
        return minimum;
    }

    private boolean isSettled(Vertex vertex) {
        return settledNodes.contains(vertex);
    }

    private int getShortestDistance(Vertex destination) {
        Integer d = distance.get(destination);
        if (d == null) {
            Log.d(GlobalConstants.TAG_DIJKSTRAS_ALGO, "getShortestDistance(" + destination.getName() + ") = Integer.MAX_VALUE");
            return Integer.MAX_VALUE;
        } else {
            Log.d(GlobalConstants.TAG_DIJKSTRAS_ALGO, "getShortestDistance(" + destination.getName() + ") = " + d);
            return d;
        }
    }

    /*
     * This method returns the path from the source to the selected target and
     * NULL if no path exists
     */
    public LinkedList<Vertex> getPath(Vertex target) {
        LinkedList<Vertex> path = new LinkedList<Vertex>();
        Vertex step = target;
        // check if a path exists
        if (predecessors.get(step) == null) {
            return null;
        }
        path.add(step);
        while (predecessors.get(step) != null) {
            step = predecessors.get(step);
            path.add(step);
        }
        // Put it into the correct order
        Collections.reverse(path);
        return path;
    }

}