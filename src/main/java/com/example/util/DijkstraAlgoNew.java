package com.example.util;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;

import com.example.domain.Edge;
import com.example.domain.PlanetNames;
import com.example.domain.PlanetVertex;
import com.example.domain.Routes;


public class DijkstraAlgoNew{

    private  List<PlanetNames> nodes;
    private  List<Routes> edges;
   
    private float     distances[];
    private Set<Integer> visitted;
    private Set<Integer> unvistted;
    private int       number_of_nodes;
    private float       graph[][];
    
    
    
    public void setNumberOfNodes(int nodes){
    	this.number_of_nodes = nodes;
    }
    public void setNodes( List<PlanetNames> plNames) {
        this.nodes = new ArrayList<PlanetNames>(plNames);
        
    }
    public void setEdges ( List<Routes> rlNames )  {
         this.edges = new ArrayList<Routes>(rlNames);
    }
  
    
    public DijkstraAlgoNew(){
       
    }
    
    public void instantiateGraph(){
    	
    	 distances = new float[number_of_nodes + 1];
    	 visitted = new HashSet<Integer>();
    	 unvistted = new HashSet<Integer>();
    	 graph = new float[number_of_nodes + 1][number_of_nodes + 1];
    	 for (int i = 1; i <= this.nodes.size(); i++)
         {
             for (int j = 1; j <= this.nodes.size(); j++)
             {
    	       graph[i][j] = 0f; 
             }
         }
    }
    
    public void fillGraph (){
    	
    	 for (int i = 1; i <= this.nodes.size(); i++)
         {
             for (int j = 1; j <= this.nodes.size(); j++)
             {
            	 if ( i !=j )
            	 { graph[i][j] = checkRoutesFromPlanets(i,j);
            	 }
                 if (i == j)
                 {
                	 graph[i][j] = 0;
                     continue;
                 }
                 if (graph[i][j] == 0)
                 {
                	 graph[i][j] = Float.MAX_VALUE;
                 }
             }
         }
    }
    public void algoPrepare(PlanetNames source ){
    	
    	  int evaluationNode;
          
   
          for (int i = 1; i <= number_of_nodes; i++)
          {
              distances[i] = Float.MAX_VALUE;
          }
   
          unvistted.add(source.getId());
          distances[source.getId()] = 0;
          while (!unvistted.isEmpty())
          {
              evaluationNode = getNodeMinDistanceFromUnVisted();
              unvistted.remove(evaluationNode);
              visitted.add(evaluationNode);
              evaluateNeighbours(evaluationNode);
          }
    }
    
    private int getNodeMinDistanceFromUnVisted()
    {
        float min;
        int node = 0;
 
        Iterator<Integer> iterator = unvistted.iterator();
        node = iterator.next();
        min = distances[node];
        for (int i = 1; i <= distances.length; i++)
        {
            if (unvistted.contains(i))
            {
                if (distances[i] <= min)
                {
                    min = distances[i];
                    node = i;
                }
            }
        }
        return node;
    }
    private void evaluateNeighbours(int evaluationNode)
    {
        float edgeDistance = 0f;
        float newDistance = 0f;
 
        for (int destinationNode = 1; destinationNode <= number_of_nodes; destinationNode++)
        {
            if (!visitted.contains(destinationNode))
            {
                if (graph[evaluationNode][destinationNode] != Float.MAX_VALUE)
                {
                    edgeDistance = graph[evaluationNode][destinationNode];
                    newDistance = distances[evaluationNode] + edgeDistance;
                    if (newDistance < distances[destinationNode])
                    {
                        distances[destinationNode] = newDistance;
                    }
                    unvistted.add(destinationNode);
                }
            }
        }
    }
    private float checkRoutesFromPlanets(int p1 , int p2 ) {
	   PlanetNames from = null, to = null;
    	 float ft = 0;
    	for (PlanetNames p : nodes){
    		if (p.getId() == p1 ){
    			from = p;
    			break;
    		}
    	}
    	for (PlanetNames p : nodes){
       	if(p.getId() == p2){
    			to = p;
    			break;
    		}
    	}
    	for ( Routes rt : edges){
    	  if (from!=null && to !=null && rt.getPlanetOrigin().getNode().equalsIgnoreCase(from.getNode()) && 
    				rt.getDestination().getNode().equalsIgnoreCase(to.getNode()))
    		{
    			ft= rt.getDistance();
    			break;
    		}
    	}
    	return ft;
	}
	public float dijkstraDistance ( PlanetNames source ) {
         float sourceMinDistance = 0; 
         Queue<PlanetVertex> clq = new ConcurrentLinkedQueue<PlanetVertex>(); 
         
         PlanetVertex sV = null;
         Edge eV = null;
         int cnt =0;
         for (PlanetNames p : nodes){
           sV = new PlanetVertex(p);
           if (cnt == 0){
        	   sV.min = 0;
           }
           for (Routes e : this.edges)
           {
        	 if ( p.getNode().equals(e.getPlanetOrigin().getNode()) 
        			 && !p.getNode().equals(e.getDestination().getNode()))
               {   eV = new Edge(new PlanetVertex(e.getDestination()),e.getDistance());
    	           sV.addEdge(eV);
      	       }   
        	   
           }
           clq.add(sV); 
           cnt++;
         }
         
         for (;! clq.isEmpty();){
        	 PlanetVertex single = clq.poll();
        	 
        	 // Visit each edge exiting u
            for (Edge e : single.pEdge) {	
            	 PlanetVertex dest = e.end;
                 float distance = e.distance;
                 float tempDistance = single.min + distance;
                 if (tempDistance < dest.min) {
                	 clq.remove(dest); // remove to re-insert it in the queue with the new cost.
                     dest.min = tempDistance;
                     dest.previous = single;
                     clq.add(dest);
                     sourceMinDistance  = tempDistance;
                 }
             }
         }
        
        return sourceMinDistance; 
    }
   

    public float getDistance(PlanetNames node, PlanetNames target) {
    	float distance = 0;
    	int start = 0 , dest = 0; 
    	for (PlanetNames p : nodes){
    		if (p.getNode().equalsIgnoreCase(node.getNode()) ){
    			node.setId(p.getId());
    		}
    		if (p.getNode().equalsIgnoreCase(target.getNode())){
    			target.setId(p.getId());
    		}
    	}
    	if (node.getId() < target.getId()){
    		start = node.getId() ;
    		dest = target.getId() ;
    	}
    	else {
    		start = target.getId() ;
    		dest = node.getId() ;
    	}
    	for (int i = start; i <= this.distances.length - 1; i++)
         {
             if (i == dest)
             {   System.out.println(node.getNode() + " to " + target.getNode() + " is "
                         + this.distances[i]);
              distance = this.distances[i];
             }
         }
        return distance;
    }

 
    
   public static void main(String[] args) {
	
	 /* DijkstraAlgo mA  = new  DijkstraAlgo ();
	  
	  
	  List<Routes> rlNames = Arrays.asList(new Routes[]{new Routes("1"	,"A"	,"B"	,"0.44"),
			                                new Routes("2"	,"A"	,"C"	,"1.89"),
			                                new Routes("3"	,"A"	,"D"	,"0.10"),
			                                new Routes("4"	,"B"	,"H"	,"2.44"),
			                                new Routes("5"	,"B"	,"E"	,"3.45"),
			                                new Routes("6"	,"C"	,"F"	,"0.49"),
			                                new Routes("7"	,"D"	,"L"	,"2.34"),
			                                new Routes("8"	,"D"	,"M"	,"2.61"),
			                                new Routes("9"	,"H"	,"G"	,"3.71"),
			                                }); 
	   mA.setEdges(rlNames);
	   
	   List<
PlanetNames> plNames = Arrays.asList(new PlanetNames[]{new PlanetNames("A","Earth"),
               new PlanetNames("B","Moon"),
               new PlanetNames("C"	,"Jupiter"),
               new PlanetNames("D"	,"Venus"),
               new PlanetNames("E"	,"Mars"),
               new PlanetNames("F"	,"Saturn"),
               new PlanetNames("G"	,"Uranus"),
               new PlanetNames("H"	,"Pluto"),
               new PlanetNames("I"	,"Neptune"	),
               new PlanetNames("J"	,"Mercury"),
               new PlanetNames("K"	,"Alpha Centauri"),
               new PlanetNames("L"	,"Luhman 16"	),
               new PlanetNames("M"	,"Epsilon Eridani"	),
               }); 
	   mA.setNodes(plNames);
	   
	   mA.dijkstraDistance(new PlanetNames("A","Earth"));*/
	   
	   
	   
} 
  /*
    may require later
    
     ExecutorService exService = Executors.newFixedThreadPool(5);
         Future disFuture  = exService.submit( distanceCalculator()); 
         
         System.out.println("Distance calculation done? " + disFuture.isDone());
         Float minDistance = 0f;
		try {
			minDistance = (Float)disFuture.get();
		} catch (InterruptedException e) {
			System.out.println("Distance calculation interrupted ");
		} catch (ExecutionException e) {
			System.out.println("Distance calculation execution interrupted ");
		}

         System.out.println("future done? " + disFuture.isDone());
         System.out.print("result: " + minDistance);
   */
}
