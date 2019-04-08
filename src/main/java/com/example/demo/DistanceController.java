
package com.example.demo;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicLong;

import javax.persistence.EntityManager;
import javax.validation.Valid;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.;
import org.springframework.web.servlet.ModelAndView;
import com.example.ExcelFileReader;
import com.example.config.HibernateInitializator;
import com.example.domain.PlanetNames;
import com.example.domain.Routes;
import com.example.repositories.PlanetNamesRepository;
import com.example.service.GraphService;
import com.example.service.PlanetService;
import com.example.service.RoutesService;
import com.example.util.DijkstraAlgo;

@Controller /* this is sufficient @ResponseBody not required on method level */
/*
  @RequestBody and @RespondeBody both are doing the JSON 
  Update: Ever since Spring 4.x, you usually won't use @ResponseBody on method level,
   but rather @RestController on class level, with the same effect. 
   See Creating REST Controllers with the @RestController annotation
 */
public class DistanceController {

	
	private static final String template = "Hello, %s!";
    private final AtomicLong counter = new AtomicLong();
    
    @Autowired
    PlanetNamesRepository respository;
 
	@Autowired
	EntityManager entityManager;

	@Autowired
    PlanetService planetService;
	
	@Autowired
	RoutesService routesService;
	
	@Autowired
    GraphService graphService;
	
	private static boolean excelLoaded=false;
   
    @GetMapping("/distance")
    public ModelAndView distance(Model ft) {
    	ModelAndView md= new ModelAndView();
    	//md.addAttribute("distance", new distanceForm());
    	md.addObject("distanceForm", new DistanceForm());
    	md.addObject("excelLoaded",excelLoaded);
        md.setViewName("distance");
        return md;
    }
    @PostMapping("/distance")
    public ModelAndView distancePost(@Valid DistanceForm distanceForm, BindingResult bindingResult) {
    	ModelAndView md= new ModelAndView();
    	
    	// first load the Data 
    	try {
    		if(!excelLoaded)
    		{
    		ExcelFileReader.ps = planetService;
        		ExcelFileReader.readExcel();
        		
        		routesService.saveAll(Routes.list);
        		
        		excelLoaded = true;
    		}	
    		} catch (FileNotFoundException ex) {
    			// TODO Auto-generated catch block
    			ex.printStackTrace();
    			} catch (IOException e) {
    		    // TODO Auto-generated catch block
    			e.printStackTrace();
    		}
    	
    	String end = "";String start  = "";
    	PlanetNames pStart  = new PlanetNames( );
    	PlanetNames pEnd  = new PlanetNames(  );
    	if(distanceForm.isNode()) {
    		end =  distanceForm.getEndNode(); 
    		start = distanceForm.getStartNode(); 
    		pStart.setNode(start);
    		pEnd.setNode(end);
    	}
    	else {
    		start   =    distanceForm.getStartPlanet(); 
    		end= distanceForm.getEndPlanet();
    		pStart.setPlanetName(start);
    		pEnd.setPlanetName(end);
    	}
       float distance=  graphService.useDijkstra(pStart, pEnd, PlanetNames.list, Routes.list);
       
         if(PlanetNames.list !=null){
        	 for (PlanetNames p : PlanetNames.list){
					
					if (p.getNode().equals(pStart.getNode()) || p.getPlanetName().equals(pStart.getPlanetName())){
						pStart = new PlanetNames(p.getNode(),p.getPlanetName());
					}
					if (p.getNode().equals(pEnd.getNode()) || p.getPlanetName().equals(pEnd.getPlanetName())){
						pEnd = new PlanetNames(p.getNode(),p.getPlanetName());
					}
				}
         }
       
    	distanceForm.setDistance(distance+"");
    	distanceForm.setStartNode(pStart.getNode());
    	distanceForm.setEndPlanet(pEnd.getPlanetName());
    	
    	distanceForm.setEndNode(pEnd.getNode());
    	distanceForm.setStartPlanet(pStart.getPlanetName());
    	
    	md.addObject("distanceForm", distanceForm);
    	md.addObject("excelLoaded",excelLoaded);
        md.setViewName("distance");
                   
        return md;
    }

}
