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

/**
 * This is the Main controller for calculating the distance from Planet from A to B
 *  
 * @author vinayak_anvekar
 *
 */

@Controller 
public class DijkstraController {

	
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
   
    @GetMapping("/dijkstra")
    public ModelAndView dijkstra(Model ft) {
    	ModelAndView md= new ModelAndView();
    	//md.addAttribute("dijkstra", new DijkstraForm());
    	md.addObject("dijkstraForm", new DijkstraForm());
    	md.addObject("excelLoaded",excelLoaded);
        md.setViewName("dijkstra");
        return md;
    }
    @PostMapping("/dijkstra")
    public ModelAndView dijkstraPost(@Valid DijkstraForm dijkstraForm, BindingResult bindingResult) {
    	ModelAndView md= new ModelAndView();
    	
    	// first load the Data 
    	try {
    		if(!excelLoaded)
    		{
    		ExcelFileReader.ps = planetService;
        		ExcelFileReader.readExcel();
        		//planetService.saveAll(PlanetNames.list);
        		routesService.saveAll(Routes.list);
        		//routesService.saveAll(Routes.list);
        		excelLoaded = true;
    		}	
    		} catch (FileNotFoundException ex) {
    			// TODO Auto-generated catch block
    			ex.printStackTrace();
    			} catch (IOException e) {
    		    // TODO Auto-generated catch block
    			e.printStackTrace();
    		}
    	
    	String end =   dijkstraForm.getEnd(); 
    	String start  =   dijkstraForm.getStart();
    	
    	PlanetNames pStart  = new PlanetNames( start, start);
    	PlanetNames pEnd  = new PlanetNames( end, end);
    	
    	float distance=  graphService.useDijkstra(pStart, pEnd, PlanetNames.list, Routes.list);
    	dijkstraForm.setDistance(distance+"");
    	md.addObject("dijkstraForm", dijkstraForm);
    	md.addObject("excelLoaded",excelLoaded);
        md.setViewName("dijkstra");
                   
        return md;
    }

}