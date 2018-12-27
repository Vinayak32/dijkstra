package com.example.util;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.example.domain.PlanetNames;
import com.example.domain.Routes;


public class DijkstraAlgo{

    private final List<PlanetNames> nodes;
    private final List<Routes> edges;
    private Set<PlanetNames> settledNodes;
    private Set<PlanetNames> unSettledNodes;
    private Map<PlanetNames, PlanetNames> predecessors;
    private Map<PlanetNames, Float> distance;

    public DijkstraAlgo(Graph graph) {
        // create a copy of the array so that we can operate on this array
        this.nodes = new ArrayList<PlanetNames>(graph.getVertexes());
        this.edges = new ArrayList<Routes>(graph.getEdges());
    }
   /* public DijkstraAlgo() {
    	
    }*/
    
    public void execute(PlanetNames source) {
        settledNodes = new HashSet<PlanetNames>();
        unSettledNodes = new HashSet<PlanetNames>();
        distance = new HashMap<PlanetNames, Float>();
        predecessors = new HashMap<PlanetNames, PlanetNames>();
        distance.put(source, 0.0f);
        unSettledNodes.add(source);
        while (unSettledNodes.size() > 0) {
            PlanetNames node = getMinimum(unSettledNodes);
            settledNodes.add(node);
            unSettledNodes.remove(node);
            findMinimalDistances(node);
        }
    }

    private void findMinimalDistances(PlanetNames node) {
        List<PlanetNames> adjacentNodes = getNeighbors(node);
        for (PlanetNames target : adjacentNodes) {
            if (getShortestDistance(target) > getShortestDistance(node)
                    + getDistance(node, target)) {
                distance.put(target, getShortestDistance(node)
                        + getDistance(node, target));
                predecessors.put(target, node);
                unSettledNodes.add(target);
            }
        }

    }	

    public float getDistance(PlanetNames node, PlanetNames target) {
    	float distance = 0;
        for (Routes edge : edges) {
        	distance = edge.getDistance();
            if (edge.getPlanetOrigin() !=null && edge.getPlanetOrigin().equals(node)
                    && edge.getDestination().equals(target)) {
                return distance;
            }
        }
        return distance;
    }

    private List<PlanetNames> getNeighbors(PlanetNames node) {
        List<PlanetNames> neighbors = new ArrayList<PlanetNames>();
        System.out.print(node);
        for (Routes edge : edges) {
        	if (edge.getPlanetOrigin()!=null && edge.getPlanetOrigin().equals(node)
                    && !isSettled(edge.getDestination())) {
            	System.out.print("Orign " +edge.getPlanetOrigin());
            	System.out.print("Neigh " +edge.getDestination());
                neighbors.add(edge.getDestination());
            }
        }
        System.out.println("");
        return neighbors;
    }

    private PlanetNames getMinimum(Set<PlanetNames> vertexes) {
    	PlanetNames minimum = null;
        for (PlanetNames vertex : vertexes) {
            if (minimum == null) {
                minimum = vertex;
            } else {
                if (getShortestDistance(vertex) < getShortestDistance(minimum)) {
                    minimum = vertex;
                }
            }
        }
        return minimum;
    }

    private boolean isSettled(PlanetNames vertex) {
        return settledNodes.contains(vertex);
    }

    private float getShortestDistance(PlanetNames destination) {
        Float d = distance.get(destination);
        if (d == null) {
            return Float.MAX_VALUE;
        } else {
            return d;
        }
    }

    /*
     * This method returns the path from the source to the selected target and
     * NULL if no path exists
     */
    public LinkedList<PlanetNames> getPath(PlanetNames target) {
        LinkedList<PlanetNames> path = new LinkedList<PlanetNames>();
        PlanetNames step = target;
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