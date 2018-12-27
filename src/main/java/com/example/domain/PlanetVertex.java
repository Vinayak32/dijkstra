package com.example.domain;

import java.util.List;

public class PlanetVertex  extends PlanetNames {
  
	public float min  = Float.MAX_VALUE; 
	public PlanetNames previous; 
	public List<Edge> pEdge; 
 	
	public PlanetVertex( PlanetNames p ){
	   super(p.node, p.planetName);
	}
	
	public void addEdge (Edge nd){
		pEdge.add(nd);
	}
	
}
