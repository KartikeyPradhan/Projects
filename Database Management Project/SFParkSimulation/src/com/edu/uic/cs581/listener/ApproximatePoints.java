package com.edu.uic.cs581.listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import com.edu.uic.cs581.database.ExtractPoints;
import com.edu.uic.cs581.database.ParkingExtractPoints;

public class ApproximatePoints implements ServletContextListener {

	public static int requestCount = 0;
	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		
		System.out.println("Server stopped............");
		
	}

	@Override
	public void contextInitialized(ServletContextEvent arg0) {
		
		System.out.println("Server started............");
		
		new ExtractPoints().getCoordinates();
		
		new ParkingExtractPoints().getCoordinates();
		
		System.out.println("Arraylist populated: "+ExtractPoints.point.size());
	}

}
