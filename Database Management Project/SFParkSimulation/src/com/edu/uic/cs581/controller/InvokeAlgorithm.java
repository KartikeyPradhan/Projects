package com.edu.uic.cs581.controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.naming.InitialContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;
import javax.swing.text.html.HTMLDocument.HTMLReader.IsindexAction;

import org.apache.catalina.connector.Request;

import com.edu.uic.cs581.algorithm.ImplementAlgorithm;
import com.edu.uic.cs581.beans.DataSet;
import com.edu.uic.cs581.beans.DistanceMap;
import com.edu.uic.cs581.beans.Points;
import com.edu.uic.cs581.beans.QuadAvailable;
import com.edu.uic.cs581.beans.Quadrant;
import com.edu.uic.cs581.constants.ConstData;
import com.edu.uic.cs581.database.ExtractPoints;
import com.edu.uic.cs581.database.ParkingExtractPoints;
import com.edu.uic.cs581.database.Weight;
import com.google.gson.Gson;

/**
 * Servlet implementation class InvokeAlgorithm
 */
//@WebServlet(description = "Invokes the routing algorithm", urlPatterns = { "/InvokeAlgorithm" })
public class InvokeAlgorithm extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public InvokeAlgorithm() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		String end = "";
		String finalDest = "";
		String intersection = "";
		
		//getting destination co-ordinates
		String destination = request.getParameter("getDest");
		
		//getting the algorithm type
		String algoType = request.getParameter("getAlgoType");

		//select iteration for baseline algorithm
		String iteration = request.getParameter("getIteration");
		
		//creating object of ImplementAlgorithm
		ImplementAlgorithm impl = new ImplementAlgorithm();
		
		HttpSession session = request.getSession();

		if(iteration!=null)
		{
			if(iteration!="undefined")
			{
				if(iteration.equalsIgnoreCase("next")){
					String points = request.getParameter("getPoints");
					points = points.replace("(", "");
					points = points.replace(")","");
					Double pointsLat = Double.parseDouble(points.split(",")[0]);
					Double pointsLong = Double.parseDouble(points.split(",")[1]);
					DistanceMap distanceMap = new Weight().pointWeight(pointsLat,pointsLong, ExtractPoints.point);
					points = distanceMap.getPoint().getLatitude() + "," + distanceMap.getPoint().getLongitude();;
					String currBlock =  session.getAttribute("currBlock") + "";
					currBlock = session.getAttribute("blockID") + "," + currBlock;
					session.setAttribute("blockID", currBlock);
					Quadrant quad = new Quadrant();
					ArrayList<QuadAvailable> q = new ArrayList<QuadAvailable>();
					q.addAll(quad.maxAvailable(pointsLat, pointsLong));
					String Lat = "";
					String Long = "";
					for(QuadAvailable i : q)
					{
						Lat = Lat + i.getC1().getLatitude() +  ", ";
						Long = Long + i.getC1().getLongitude() + ", ";
					}
					if(algoType.equalsIgnoreCase(ConstData.PT_COST_BASED_FORCE_ALGO))
							end = impl.probabilisticCostBased(points, destination, request);
					else if(algoType.equalsIgnoreCase(ConstData.PT_DISTANCE_BASED_FORCE_ALGO))
							end = impl.probabilisticDistanceBased(points, destination, request);
					else if(algoType.equalsIgnoreCase(ConstData.PT_GREEDY_ALGO))
						end = impl.probabilisticGreedy(points, destination, request);
					else if(algoType.equalsIgnoreCase(ConstData.BASELINE))
						end = impl.baselined(points, destination, request);
				}
				
			}
		}
		

		if(request.getParameter("getPoints")!=null || request.getParameter("getPoints")!="undefined")
		{
			intersection = impl.checkIntersection(request);

			if(!intersection.equalsIgnoreCase("")){
				if(destination != null || destination!="undefined"){
					
					//for gravitational force cost based
					if(algoType.equalsIgnoreCase(ConstData.RT_COST_BASED_FORCE_ALGO))
						end = impl.costBasedForce(intersection, destination);
					
					//for greedy algorithm
					else if(algoType.equalsIgnoreCase(ConstData.RT_GREEDY_ALGO))
						end = impl.greedyAlgorithm(intersection, destination);
					//for gravitational force distance based
					else if(algoType.equalsIgnoreCase(ConstData.RT_DISTANCE_BASED_FORCE_ALGO))
						end = impl.distanceBasedForce(intersection,destination);
					//for probabilistic cost based
					else if(algoType.equalsIgnoreCase(ConstData.PT_COST_BASED_FORCE_ALGO))
						end = impl.probabilisticCostBased(intersection, destination, request);
					//for probabilistic distance based
					else if(algoType.equalsIgnoreCase(ConstData.PT_DISTANCE_BASED_FORCE_ALGO))
						end = impl.probabilisticDistanceBased(intersection, destination, request);
					//probabilistic greedy algo
					else if(algoType.equalsIgnoreCase(ConstData.PT_GREEDY_ALGO))
						end = impl.probabilisticGreedy(intersection, destination, request);
					//for baseline
					else if(algoType.equalsIgnoreCase(ConstData.BASELINE))
						end = impl.baselined(intersection, destination, request);
				}
			}
			
		}

		//writing to JSON object
		write(response, end);
		
		
		
	}
	
	/*
	 * for writing the asynchronous response
	 */
	private void write(HttpServletResponse response, String end) throws IOException {
		
		//setting the content type and character encoding
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		
		//writing the response to Json Object
		response.getWriter().write(new Gson().toJson(end));
		
	}
	
	

}
