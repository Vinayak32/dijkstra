package com.example;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.nio.file.Files;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.example.domain.PlanetNames;
import com.example.domain.Routes;
import com.example.domain.Traffic;
import com.example.service.PlanetService;
import com.example.config.HibernateInitializator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.springframework.util.ResourceUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.beans.factory.annotation.Autowired;

//@SpringBootApplication

public class ExcelFileReader {

    @Value("classpath:excellRead.xlsx") private static Resource aResource;
	
	@Autowired
    private static ResourceLoader resourceLoader;
	
	
	//final Resource fileResource = resourceLoader.getResource("classpath:/excellRead.xlsx");
	
	public static void main(String[] args) throws FileNotFoundException, IOException {
		// TODO Auto-generated method stub
		HibernateInitializator temp = new HibernateInitializator();

		SessionFactory tempFactory;

		tempFactory = temp.getSessionFactory();
		Session s = tempFactory.openSession();

		readExcel(s);

	}

	public static void readExcel(Session s) throws FileNotFoundException, IOException {

		// Properties properties = new Properties();

		// File file = ResourceUtils.getFile("classpath:excelRead.xlsx");
		String fileName = "excellRead.xlsx";
        ClassLoader classLoader = new ExcelFileReader().getClass().getClassLoader();
 
        File file = new File(classLoader.getResource(fileName).getFile());
		//File file = new File("G:/Seagate Dashboard 2.0/WP6PORTAL/springboot/demo/src/main/resources/excellRead.xlsx");
		InputStream in = new FileInputStream(file);

		FileInputStream fis = new FileInputStream(file);
		// Finds the workbook instance for XLSX file
		XSSFWorkbook myWorkBook = new XSSFWorkbook(fis);

		// Read the Planet Names excel Sheet
		XSSFSheet mySheet = myWorkBook.getSheetAt(0);
		List<PlanetNames> pNames = readPlanetNames(mySheet, s);
		PlanetNames.list = pNames;
		// pNames.stream().forEach(i -> System.out.print(" "+i));
		// Read the Routes excel Sheet
		XSSFSheet myRoutes = myWorkBook.getSheetAt(1);
		List<Routes> pRoutes = readRoutes(myRoutes, s);

		// Read the Traffic excel Sheet
		// XSSFSheet myTraffic = myWorkBook.getSheetAt(2);
		// readTraffic(myTraffic);

	}

	public static void printValues(Object p) {
		if (p instanceof PlanetNames) {
			PlanetNames pp = (PlanetNames) p;
			System.out.println(pp.getNode());
		}
	}

	public static List<PlanetNames> readPlanetNames(XSSFSheet myPlanetNames, Session s) throws IOException {

		Transaction tx = s.beginTransaction();
		// Get iterator to all the rows in current sheet
		Iterator<Row> rowIterator = myPlanetNames.iterator();
		// Traversing over each row of XLSX file

		// Create List of PlanetNames
		List<PlanetNames> pPlanetList = new ArrayList<PlanetNames>();
		PlanetNames p = new PlanetNames();
		System.out.println("Planet Node " + p.getNode());
		System.out.println("Planet Name " + p.getPlanetName());
		Row row = rowIterator.next();

		try {

			while (rowIterator.hasNext()) {

				// skip first row it is header
				row = rowIterator.next();

				p = new PlanetNames();
				// For each row, iterate through each columns
				Iterator<Cell> cellIterator = row.cellIterator();

				int cellCntr = 0;
				while (cellIterator.hasNext()) {
					Cell cell = cellIterator.next();

					switch (cell.getCellType()) {
					case Cell.CELL_TYPE_STRING:
						System.out.print(cell.getStringCellValue() + "\t");
						if (cellCntr == 0) {
							p.setPlanetNode(cell.getStringCellValue());
						} else if (cellCntr == 1) {
							p.setPlanetName(cell.getStringCellValue());
						}
						break;
					case Cell.CELL_TYPE_NUMERIC:
						System.out.print(cell.getNumericCellValue() + "\t");
						break;
					case Cell.CELL_TYPE_BOOLEAN:
						System.out.print(cell.getBooleanCellValue() + "\t");
						break;
					default:
					}
					cellCntr++;

				}
				System.out.println("");
				pPlanetList.add(p);
				s.save(p);
			}
			tx.commit();
		} catch (Exception e) {
			System.out.println(e);
			tx.rollback();
		}
		return pPlanetList;

	}

	public static List<Routes> readRoutes(XSSFSheet myRoutes, Session s) throws IOException {

		Transaction tx = s.beginTransaction();

		// Get iterator to all the rows in current sheet
		Iterator<Row> rowIterator = myRoutes.iterator();
		// Traversing over each row of XLSX file

		// Create List of PlanetNames
		List<Routes> pRouteList = new ArrayList<Routes>();
		Routes p = new Routes();
		System.out.println("Route Id " + p.getId());
		System.out.println("Planet Origin " + p.getPlanetOrigin());
		System.out.println("Planet Destination " + p.getDestination());
		System.out.println("Distance " + p.getDistance());

		Row row = rowIterator.next();
		try {
			while (rowIterator.hasNext()) {

				// skip first row it is header
				row = rowIterator.next();

				p = new Routes();
				// For each row, iterate through each columns
				Iterator<Cell> cellIterator = row.cellIterator();

				int cellCntr = 0;
				while (cellIterator.hasNext()) {
					Cell cell = cellIterator.next();
					switch (cell.getCellType()) {
					// case Cell.
					case Cell.CELL_TYPE_STRING:
						System.out.print(cell.getStringCellValue() + "\t");
						switch (cellCntr) {
						case 0:
							p.setId(cell.getStringCellValue());

							break;
						case 1:
							p.setOrigin(cell.getStringCellValue());
							break;
						case 2:
							p.setDestination(cell.getStringCellValue());
							break;
						case 3:
							p.setDistance(cell.getStringCellValue());
							break;
						}

						break;
					case Cell.CELL_TYPE_NUMERIC:
						System.out.print(cell.getNumericCellValue() + "\t");
						switch (cellCntr) {
						case 0:
							p.setId((int) cell.getNumericCellValue());
							break;
						case 1:
							p.setOrigin(cell.getStringCellValue());
							break;
						case 2:
							p.setDestination(cell.getStringCellValue());
							break;
						case 3:
							p.setDistance((float) cell.getNumericCellValue());
							break;
						}

						break;
					case Cell.CELL_TYPE_BOOLEAN:
						System.out.print(cell.getBooleanCellValue() + "\t");
						break;
					default:
					}
					cellCntr++;

				}
				System.out.println("");
				pRouteList.add(p);
				s.save(p);
			}
			tx.commit();
		} catch (Exception e) {
			System.out.println(e);
			tx.rollback();
		}
		return pRouteList;

	}
	public static List<Routes> readRoutes(XSSFSheet myRoutes) throws IOException {

		

		// Get iterator to all the rows in current sheet
		Iterator<Row> rowIterator = myRoutes.iterator();
		// Traversing over each row of XLSX file

		// Create List of PlanetNames
		List<Routes> pRouteList = new ArrayList<Routes>();
		Routes p = new Routes();
		System.out.println("Route Id " + p.getId());
		System.out.println("Planet Origin " + p.getPlanetOrigin());
		System.out.println("Planet Destination " + p.getDestination());
		System.out.println("Distance " + p.getDistance());

		Row row = rowIterator.next();
		try {
			while (rowIterator.hasNext()) {

				// skip first row it is header
				row = rowIterator.next();

				p = new Routes();
				// For each row, iterate through each columns
				Iterator<Cell> cellIterator = row.cellIterator();

				int cellCntr = 0;
				while (cellIterator.hasNext()) {
					Cell cell = cellIterator.next();
					switch (cell.getCellType()) {
					// case Cell.
					case Cell.CELL_TYPE_STRING:
						System.out.print(cell.getStringCellValue() + "\t");
						switch (cellCntr) {
						case 0:
							p.setId(cell.getStringCellValue());

							break;
						case 1:
							p.setOrigin(cell.getStringCellValue());
							p.setPlanetOrigin(cell.getStringCellValue());
						
							break;
						case 2:
							p.setDestination(cell.getStringCellValue());
							
							break;
						case 3:
							p.setDistance(cell.getStringCellValue());
							break;
						}

						break;
					case Cell.CELL_TYPE_NUMERIC:
						System.out.print(cell.getNumericCellValue() + "\t");
						switch (cellCntr) {
						case 0:
							p.setId((int) cell.getNumericCellValue());
							break;
						case 1:
							p.setOrigin(cell.getStringCellValue());
							p.setPlanetOrigin(cell.getStringCellValue());
							break;
						case 2:
							p.setDestination(cell.getStringCellValue());
							break;
						case 3:
							p.setDistance((float) cell.getNumericCellValue());
							break;
						}

						break;
					case Cell.CELL_TYPE_BOOLEAN:
						System.out.print(cell.getBooleanCellValue() + "\t");
						break;
					default:
					}
					cellCntr++;

				}
				System.out.println(" route :"+p.toString());
				
				pRouteList.add(p);
				
			}
			
		} catch (Exception e) {
			System.out.println(e);
			
		}
		return pRouteList;

	}
	public static List<Traffic> readTraffic(XSSFSheet myTraffic) {

		// Get iterator to all the rows in current sheet
		Iterator<Row> rowIterator = myTraffic.iterator();
		// Traversing over each row of XLSX file

		// Create List of PlanetNames
		List<Traffic> pTrafficList = new ArrayList<Traffic>();
		Traffic p = new Traffic();
		System.out.println("Route Id " + p.getId());
		System.out.println("Planet Origin " + p.getPlanetOrigin());
		System.out.println("Planet Destination " + p.getDestination());
		System.out.println("Distance " + p.getTrafficDelay());

		while (rowIterator.hasNext()) {

			Row row = rowIterator.next();
			// skip first row it is header
			row = rowIterator.next();

			p = new Traffic();
			// For each row, iterate through each columns
			Iterator<Cell> cellIterator = row.cellIterator();

			int cellCntr = 0;
			while (cellIterator.hasNext()) {
				Cell cell = cellIterator.next();
				switch (cell.getCellType()) {
				case Cell.CELL_TYPE_STRING:
					System.out.print(cell.getStringCellValue() + "\t");
					switch (cellCntr) {
					case 0:
						p.setId(cell.getStringCellValue());
						break;
					case 1:
						p.setOrigin(cell.getStringCellValue());
						break;
					case 2:
						p.setDestination(cell.getStringCellValue());
						break;
					case 3:
						p.setTrafficDelay(cell.getStringCellValue());
						break;
					}

					break;
				case Cell.CELL_TYPE_NUMERIC:
					System.out.print(cell.getNumericCellValue() + "\t");
					break;
				case Cell.CELL_TYPE_BOOLEAN:
					System.out.print(cell.getBooleanCellValue() + "\t");
					break;
				default:
				}
				cellCntr++;

			}
			System.out.println("");
			pTrafficList.add(p);
		}

		return pTrafficList;

	}
	public static PlanetService ps;
	public static void readExcel() throws IOException {

		// File file = ResourceUtils.getFile("classpath:excelRead.xlsx");
		String fileName = "excellRead.xlsx";
		Resource fileResource = null;
        ClassLoader classLoader = new ExcelFileReader().getClass().getClassLoader();
 
        //File file = new File(classLoader.getResource(fileName).getFile());
		if(aResource !=null){
			System.out.println("The  aResource" +aResource +" Resource exists ");
			
		}
		if (resourceLoader !=null )
			{ System.out.println("The  resourceLoader" +resourceLoader +" Resource exists ");
				 fileResource =  resourceLoader.getResource("classpath:excellRead.xlsx");
			  if (fileResource !=null){
				  System.out.println("The  fileResource " +fileResource +" Resource exists ");
			  }
			 }
		//File file =  ResourceUtils.getFile("classpath:excellRead.xlsx");
		//InputStream in = ResourceUtils.getURL(fileName).openStream();
		String prefixedResourcePath = ResourceUtils.CLASSPATH_URL_PREFIX + fileName;
	    ClassPathResource classPathResource = new ClassPathResource(prefixedResourcePath);

	    if (classPathResource.exists()) {
			System.out.println("The fileName " +fileName +" Resource exists ");
		}
		//File file = new File("G:/Seagate Dashboard 2.0/WP6PORTAL/springboot/demo/src/main/resources/excellRead.xlsx");
		//InputStream in = new FileInputStream(file);

		//FileInputStream fis = new FileInputStream(file);
		// Finds the workbook instance for XLSX file
		XSSFWorkbook myWorkBook = new XSSFWorkbook(classPathResource.getInputStream());
		// Read the Planet Names excel Sheet
		XSSFSheet mySheet = myWorkBook.getSheetAt(0);
		List<PlanetNames> pNames = readPlanetNames(mySheet);
		
		
		ps.saveAll(pNames);
		PlanetNames.list = ps.findAll();
		
		XSSFSheet myRoutes = myWorkBook.getSheetAt(1);
		List<Routes> pRoutes = readRoutes(myRoutes);
		Routes.list = pRoutes;
	}

	private static List<PlanetNames> readPlanetNames(XSSFSheet myPlanetNames) {
		// Get iterator to all the rows in current sheet
				Iterator<Row> rowIterator = myPlanetNames.iterator();
				// Traversing over each row of XLSX file

				// Create List of PlanetNames
				List<PlanetNames> pPlanetList = new ArrayList<PlanetNames>();
				PlanetNames p = new PlanetNames();
				System.out.println("Planet Node " + p.getNode());
				System.out.println("Planet Name " + p.getPlanetName());
				Row row = rowIterator.next();

				try {

					while (rowIterator.hasNext()) {

						// skip first row it is header
						row = rowIterator.next();

						p = new PlanetNames();
						// For each row, iterate through each columns
						Iterator<Cell> cellIterator = row.cellIterator();

						int cellCntr = 0;
						while (cellIterator.hasNext()) {
							Cell cell = cellIterator.next();

							switch (cell.getCellType()) {
							case Cell.CELL_TYPE_STRING:
								System.out.print(cell.getStringCellValue() + "\t");
								if (cellCntr == 0) {
									p.setPlanetNode(cell.getStringCellValue());
								} else if (cellCntr == 1) {
									p.setPlanetName(cell.getStringCellValue());
								}
								break;
							case Cell.CELL_TYPE_NUMERIC:
								System.out.print(cell.getNumericCellValue() + "\t");
								break;
							case Cell.CELL_TYPE_BOOLEAN:
								System.out.print(cell.getBooleanCellValue() + "\t");
								break;
							default:
							}
							cellCntr++;

						}
						System.out.println("");
						pPlanetList.add(p);
						
					}
				
				} catch (Exception e) {
					System.out.println(e);
					
				}
				return pPlanetList;
	}

}
