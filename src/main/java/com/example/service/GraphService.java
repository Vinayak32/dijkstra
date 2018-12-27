package com.example.service;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.domain.PlanetNames;
import com.example.domain.Routes;
import com.example.repositories.PlanetNamesRepository;
import com.example.repositories.RoutesRepository;
import com.example.util.DijkstraAlgoNew;
import com.example.util.Graph;

/**
 * This is the Graph Service class that prepares the distance based graph 
 * for all planets
 * 
 * @author vinayak_anvekar
 *
 */
@Configuration
@Service
@Transactional
public class GraphService {
	    @Autowired
	    PlanetNamesRepository respository;
	    
	    @Autowired
	    RoutesRepository routesRepo;
	    
	    List<Routes> allRoutes ;
	    List<PlanetNames> allPlanets;
	    //DijkstraAlgo dAlgo; 
	    
	    DijkstraAlgoNew dAlgo; 
	    Graph gph;
	 
		@Autowired
		EntityManager entityManager;
		
		public void getRoutes(){
			allRoutes = routesRepo.findAll();
		}
		
		public void getPlanets() {
			
			allPlanets = respository.findAll();
		}
		
		
		private void prepareGraph(List<PlanetNames> lp, List<Routes> lr ) {
		    gph = new Graph( lp,lr);
			
		}
		public float useDijkstra(PlanetNames start, PlanetNames end, List<PlanetNames> list, List<Routes> list2) { 
			float distance = 0;
			    getPlanets();
				getRoutes();
				System.out.println("Planet Names "+list.size());
				System.out.println("Routes "+list2.size());
				
				prepareGraph(list,list2);
				System.out.println("Routes gph.getEdges().size() " +gph.getEdges().size());
				System.out.println("Planet Names gph.getEdges().size() " +gph.getVertexes().size());
				dAlgo = new DijkstraAlgoNew();
				// Lets check from location Loc_1 to Loc_10
				dAlgo.setNodes(list);
				dAlgo.setEdges(list2);
				dAlgo.setNumberOfNodes(list.size());
				//PlanetNames names=PlanetNames.NodePlanetFromNodeId("\)
				dAlgo.instantiateGraph(); 
				dAlgo.fillGraph();
				dAlgo.algoPrepare(list.get(0));
		         
		        distance += dAlgo.getDistance(start,end);
		        	
		         System.out.println("distance "+distance);
		            
		        
				
			return distance;
		}
		
		
		
}
