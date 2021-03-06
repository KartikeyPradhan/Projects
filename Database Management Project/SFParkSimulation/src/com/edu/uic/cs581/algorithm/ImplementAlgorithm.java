package com.edu.uic.cs581.algorithm;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.naming.InitialContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;

import com.edu.uic.cs581.beans.DistanceMap;
import com.edu.uic.cs581.beans.QuadAvailable;
import com.edu.uic.cs581.beans.Quadrant;
import com.edu.uic.cs581.constants.ConstData;
import com.edu.uic.cs581.database.ExtractPoints;
import com.edu.uic.cs581.database.ParkingExtractPoints;
import com.edu.uic.cs581.database.Weight;

public class ImplementAlgorithm {
	
	/*
	 * getting all the intersection co-ordinates on the path
	 */
	public String checkIntersection(HttpServletRequest request){
		
		String interSection = "";
			
			String intersectionPoints = request.getParameter("getPoints");
			String splitString[] = intersectionPoints.split(",");

			Double intersectionLat = Double.parseDouble(splitString[0].replace("(","").trim());
			Double intersectionLong = Double.parseDouble(splitString[1].replace(")","").trim());
									
			DistanceMap distanceMap = new Weight().pointWeight(intersectionLat,intersectionLong, ExtractPoints.point);

			if(distanceMap.getDist() < ConstData.INTERSECTION_THRESHOLD)
			{
				interSection = distanceMap.getPoint().getLatitude() + "," + distanceMap.getPoint().getLongitude();				
			}	
		return interSection;
	
	}
	
	/*
	 * getting all the parking slots
	 */
	public String checkBlockMidPoint(Double lat,Double lon){
		
		
			String finalDestination = "";									
			DistanceMap distanceMap = new Weight().pointWeight(lat,lon, ParkingExtractPoints.midPoint);
			if(distanceMap.getDist() < ConstData.INTERSECTION_THRESHOLD)
			{
				finalDestination = distanceMap.getPoint().getLatitude() + "," + distanceMap.getPoint().getLongitude();				
			}	
		return finalDestination;
	
	}
	
	
	//Distance based gravitational force algorithm
	public String distanceBasedForce(String end,String dest){
		
		String splitString[] = end.split(",");
		
		Double intersectionLat = Double.parseDouble(splitString[0].replace("(","").trim());
		Double intersectionLong = Double.parseDouble(splitString[1].replace(")","").trim());
		
		String destSplit[] = dest.split(",");
		
		Double destLat = Double.parseDouble(destSplit[0].replace("(","").trim());
		Double destLong = Double.parseDouble(destSplit[1].replace(")","").trim());
		String destLoc = ""; 
		String date = new SimpleDateFormat(ConstData.DATE_FROMAT).format(new Date());
		String destinationQuery = "select aa.block_id, aa.available, aa.distance,aa.block_latitude destination_latitude,aa.block_longitude destination_longitude, aa.GForce FROM " + 
				 "(select a.block_id, a.available, a.distance,a.block_latitude,a.block_longitude, (a.available *10000000/(a.distance*a.distance)) as GForce, " +
				 "row_number() over ( order by (a.available *10000000/(a.distance*a.distance)) desc ) as RowNum1 " +
				 "from( " +
				 "SELECT a.block_id, a.available, b.distance,b.block_latitude,b.block_longitude, row_number() over ( partition by a.block_id order by DATEANDTIME desc ) as RowNum " +
				 "FROM dbprojection a JOIN distance b " +
				 "ON a.block_id = b.block_id " + 
				 "where DATEANDTIME < '" + date + "' and " + 
				 "intersection_latitude =" + intersectionLat + " and " +
				 "intersection_longitude = " + intersectionLong + 
				 ") a " + 
				 "where RowNum = 1 "
				 + "and available <> 0 " + 
				 ") aa " + 
				 "where RowNum1 = 1";
		String walkingQuery = "";
		
		Connection connection = null;
		Statement statement = null;
		try {
			InitialContext ic = new InitialContext();
			DataSource ds = (DataSource) ic.lookup("java:comp/env/jdbc/sqlserv");
			connection = ds.getConnection();
			statement = connection.createStatement();
			ResultSet rs = statement.executeQuery(destinationQuery);
			int  i = 0;
			while(rs.next())
			{
				destLoc = rs.getDouble("destination_latitude") + "," + rs.getDouble("destination_longitude");
				i++;
			}
			
			
			String destination = checkBlockMidPoint(destLat, destLong);
			destLat = Double.parseDouble(destination.split(",")[0]);
			destLong = Double.parseDouble(destination.split(",")[1]);
			
			walkingQuery = "select Time from WalkingTime " + 
					  "where Parking_block_latitude = " + destLoc.split(",")[0] + " and Parking_block_longitude = " + destLoc.split(",")[1] +
					  " and Dest_block_latitude = " + destLat +" and Dest_block_longitude = " + destLong;
			rs = statement.executeQuery(walkingQuery);
			
			while(rs.next())
			{
				destLoc = destLoc + "|" + rs.getInt("Time");
			};
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return destLoc;
		
	}
	
	public String costBasedForce(String start,String dest)
	{
		Connection connection = null;
		String finalLocation = "";
		String startSplit[] = start.split(",");
		String destSplit[] = dest.split(",");
		
		Double startLat = Double.parseDouble(startSplit[0]);
		Double startLong = Double.parseDouble(startSplit[1]);
		
		
		Double destLat = Double.parseDouble(destSplit[0].replace("(","").trim());
		Double destLong = Double.parseDouble(destSplit[1].replace(")","").trim());
			
		String destination = checkBlockMidPoint(destLat, destLong);
		destLat = Double.parseDouble(destination.split(",")[0]);
		destLong = Double.parseDouble(destination.split(",")[1]);
		
		String date = new SimpleDateFormat(ConstData.DATE_FROMAT).format(new Date());
		date = date.replace("2015", "2012");	
		
		String query = "select " +
				   "aaa.Parking_block_latitude final_lat, aaa.Parking_block_longitude final_long,aaa.Time final_time " +
				    "from " +
					"( " +
					"select walking.Parking_block_id,walking.Parking_block_latitude as Parking_block_latitude,walking.Parking_block_longitude as Parking_block_longitude, " +
						"walking.Dest_block_id,walking.Dest_block_latitude,walking.Dest_block_longitude, walking.Time " +
					", driving.available, driving.driving_time as driving_time  " +
					",(driving.available * 10000000/((driving.driving_time+walking.Time)*(driving.driving_time+walking.Time))) as GForce " +
					",row_number() over ( order by (driving.available * 10000000/((driving.driving_time+walking.Time)*(driving.driving_time+walking.Time))) desc ) as RowNum1 " +
					"FROM " +
							"(select  " +
								"a.block_id, a.available, a.time as driving_time, " +
								"a.block_latitude,a.block_longitude, " +
								"intersection_latitude,intersection_longitude " +
								"from( " +
										"SELECT a.block_id, a.available, b.time,b.block_latitude,b.block_longitude, " +
										"intersection_latitude,intersection_longitude, " +
										"row_number() over ( partition by a.block_id order by DATEANDTIME desc ) as RowNum " +
										"FROM dbprojection a JOIN distance b  " +
										"ON a.block_id = b.block_id  " +
										"where DATEANDTIME < '"+date+"'  and " +
										"intersection_latitude ="+startLat+" and intersection_longitude ="+ startLong +
								") a " +
							"where RowNum = 1 "
							+ "and available <> 0 " +
							") as driving " +
					"INNER JOIN " + 
					"( " +
					"select * from WalkingTime " + 
					"where Dest_block_latitude = "+destLat+" and Dest_block_longitude ="+destLong +
					") as walking " +
					"ON driving.block_id = walking.Parking_block_id " +
			    ") aaa " +
				"where RowNum1 = 1 ";
		
		try {
			
			InitialContext ic = new InitialContext();
			DataSource ds = (DataSource) ic.lookup("java:comp/env/jdbc/sqlserv");
			connection = ds.getConnection();
			Statement statement = connection.createStatement();
			ResultSet rs = statement.executeQuery(query);

			int  i = 0;
			while(rs.next())
			{
				finalLocation = rs.getDouble("final_lat") + "," + rs.getDouble("final_long")+ "|" + rs.getInt("final_time");
				i++;
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			try {
				connection.close();

			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return finalLocation;
	}

	
	public String greedyAlgorithm(String start,String dest)
	{
		Connection connection = null;
		String finalLocation = "";
		String startSplit[] = start.split(",");
		String destSplit[] = dest.split(",");
		
		Double startLat = Double.parseDouble(startSplit[0]);
		Double startLong = Double.parseDouble(startSplit[1]);
		
		
		Double destLat = Double.parseDouble(destSplit[0].replace("(","").trim());
		Double destLong = Double.parseDouble(destSplit[1].replace(")","").trim());
		
		String destination = checkBlockMidPoint(destLat, destLong);
		////system.out.println("destination: "+ destination);
		destLat = Double.parseDouble(destination.split(",")[0]);
		destLong = Double.parseDouble(destination.split(",")[1]);
		
		String date = new SimpleDateFormat(ConstData.DATE_FROMAT).format(new Date());
		date = date.replace("2015", "2012");
		String query = "select aa.Parking_block_latitude final_lat, aa.Parking_block_longitude final_long,aa.Total_time Total_time "+
				"FROM " +
				"(select dest_info.*, availability_info.*,(dest_info.walking_time + availability_info.driving_time) as Total_time, row_number() over (order by (dest_info.walking_time + availability_info.driving_time) ASC ) as RowNum1 " + 
					"FROM "+
					"(SELECT Parking_block_id ,Parking_block_latitude ,Parking_block_longitude ,Dest_block_id ,Dest_block_latitude ,Dest_block_longitude ,Time as walking_time  "+
					"FROM SFPark.dbo.WalkingTime "+
					"where Dest_block_latitude = "+ destLat +
					"and Dest_block_longitude = " + destLong +
					") as dest_info  "+
				"INNER JOIN "+
					"(select a.block_id, a.available, a.distance,a.block_latitude,a.block_longitude , driving_time "+
					"from "+
						"(SELECT a.block_id, a.available, b.distance,b.time as driving_time, b.block_latitude,b.block_longitude, "+
						"row_number() over ( partition by a.block_id order by DATEANDTIME desc ) as RowNum  "+
						"FROM dbprojection a JOIN distance b ON a.block_id = b.block_id "+
						"where DATEANDTIME < "+ "\'" + date + "\'" +  
						"and intersection_latitude = " + startLat + 
						"and intersection_longitude = " + startLong +
						") a where RowNum = 1  and available <> 0 "+
					") as availability_info "+
				"ON dest_info.Parking_block_id = availability_info.block_id "+
			") aa where RowNum1 = 1 order by distance desc";
		
		
		
		try {
			
			InitialContext ic = new InitialContext();
			DataSource ds = (DataSource) ic.lookup("java:comp/env/jdbc/sqlserv");
			connection = ds.getConnection();
			Statement statement = connection.createStatement();
			ResultSet rs = statement.executeQuery(query);

			int  i = 0;
			while(rs.next())
			{
				finalLocation = rs.getDouble("final_lat") + "," + rs.getDouble("final_long") + "|" + rs.getInt("Total_time");
				i++;
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			try {
				connection.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return finalLocation;
	}
	
	
	public String probabilisticCostBased(String start,String dest,HttpServletRequest request){
		

		HttpSession session = request.getSession();
		if(session.getAttribute("blockID")==null)
		{
			session.setAttribute("blockID","0");
			session.setAttribute("currBlock","");
		}
		
		
		Connection connection = null;
		String finalLocation = "";
		String startSplit[] = start.split(",");
		String destSplit[] = dest.split(",");
		
		Double startLat = Double.parseDouble(startSplit[0]);
		Double startLong = Double.parseDouble(startSplit[1]);
		
		
		Double destLat = Double.parseDouble(destSplit[0].replace("(","").trim());
		Double destLong = Double.parseDouble(destSplit[1].replace(")","").trim());
		
		String destination = checkBlockMidPoint(destLat, destLong);
		destLat = Double.parseDouble(destination.split(",")[0]);
		destLong = Double.parseDouble(destination.split(",")[1]);
		
		String date = new SimpleDateFormat(ConstData.DATE_FROMAT).format(new Date());
		date = date.replace("2015", "2012");

		
		String query = "select " +
				"aaa.Parking_block_latitude final_lat, aaa.Parking_block_longitude final_long, aaa.Time final_time,aaa.Parking_block_id final_block_id "+
				"from ( "+
				"select walking.Parking_block_id,walking.Parking_block_latitude as Parking_block_latitude, "+
				"walking.Parking_block_longitude as Parking_block_longitude, "+
				"walking.Dest_block_id,walking.Dest_block_latitude,walking.Dest_block_longitude, walking.Time "+
				", driving.projected_availability, driving.driving_time as driving_time  "+
				",(driving.projected_availability * 10000000/((driving.driving_time+walking.Time)*(driving.driving_time+walking.Time))) as GForce "+
				",row_number() over ( order by (driving.projected_availability * 10000000/((driving.driving_time+walking.Time)*(driving.driving_time+walking.Time))) desc ) as RowNum1 "+
				"FROM "+
				"(select "+
				"a.block_id, a.projected_availability, a.time as driving_time, "+
				"a.block_latitude,a.block_longitude, "+
				"intersection_latitude,intersection_longitude "+
				"from( "+
				"SELECT a.block_id, a.projected_availability, b.time,b.block_latitude,b.block_longitude, "+
				"intersection_latitude,intersection_longitude, "+
				"row_number() over ( partition by a.block_id order by a.projected_time desc ) as RowNum "+
				"FROM projected_availability a JOIN distance b  "+
				"ON a.block_id = b.block_id "+
				"where datename(dw,'"+date+"') = a.dayname " +
				"and CONVERT(TIME, '"+date+"') >= a.projected_time  " +
				"and intersection_latitude = " + startLat +
				" and intersection_longitude = " + startLong +
				" and a.block_id not in ( "+session.getAttribute("blockID")+" ) "+
				") a  " +
   							"where RowNum = 1  " +
   							"and projected_availability <> 0 " +
   							") as driving " +
   							"INNER JOIN " +
   							"(  " +
   							"select * from WalkingTime  " + 
   							"where Dest_block_latitude = " + destLat +  
   							" and Dest_block_longitude = " + destLong +
   							" ) as walking " +
   							"ON driving.block_id = walking.Parking_block_id " + 
   			") aaa " + 
   			"where RowNum1 = 1";
		
				try {
					
					InitialContext ic = new InitialContext();
					DataSource ds = (DataSource) ic.lookup("java:comp/env/jdbc/sqlserv");
					connection = ds.getConnection();
					Statement statement = connection.createStatement();
					ResultSet rs = statement.executeQuery(query);
					int  i = 0;
					while(rs.next())
					{
						finalLocation = rs.getDouble("final_lat") + "," + rs.getDouble("final_long")+ "|" + rs.getInt("final_time");
						session.setAttribute("currBlock", rs.getInt("final_block_id"));
						i++;
					}
					
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}finally{
					try {
						connection.close();
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				return finalLocation;
				       
	}
	
	public String probabilisticDistanceBased(String start,String dest,HttpServletRequest request){
		
		HttpSession session = request.getSession();
		if(session.getAttribute("blockID")==null)
		{
			session.setAttribute("blockID","0");
			session.setAttribute("currBlock","");
		}
		
		
		Connection connection = null;
		String finalLocation = "";
		String startSplit[] = start.split(",");
		String destSplit[] = dest.split(",");
		
		Double startLat = Double.parseDouble(startSplit[0]);
		Double startLong = Double.parseDouble(startSplit[1]);
		
		
		Double destLat = Double.parseDouble(destSplit[0].replace("(","").trim());
		Double destLong = Double.parseDouble(destSplit[1].replace(")","").trim());
		
		String destination = checkBlockMidPoint(destLat, destLong);
		////system.out.println("destination: "+ destination);
		destLat = Double.parseDouble(destination.split(",")[0]);
		destLong = Double.parseDouble(destination.split(",")[1]);
		
		String date = new SimpleDateFormat(ConstData.DATE_FROMAT).format(new Date());
		date = date.replace("2015", "2012");
		
		String query = "SELECT " + 
				"aa.block_latitude final_lat,aa.block_longitude final_long, aa.block_id final_block_id " +
				"FROM " +
				"( "+
				"SELECT a.block_id, " +
				"a.projected_availability, "+
				" a.distance,a.block_latitude, "+
				"a.block_longitude, "+
				"(a.projected_availability *10000000/(a.distance*a.distance)) as GForce,"+ 
				"row_number() over ( order by (a.projected_availability *10000000/(a.distance*a.distance)) desc ) as RowNum1 "+
				"from( " +
					"SELECT a.block_id, a.projected_availability, "+
							"a.projected_time,a.dayname, "+
							"b.distance,b.block_latitude,b.block_longitude, "+
							"row_number() over ( partition by a.block_id order by a.projected_time desc ) as RowNum "+
						"FROM "+
						"projected_availability a JOIN distance b "+
						"ON a.block_id = b.block_id "+
						"WHERE "+
							"datename(dw,'" + date+ "') = a.dayname "+
							"and CONVERT(TIME,'" + date+ "') >= a.projected_time "+
							" and intersection_latitude = " + startLat +
							" and intersection_longitude = " + startLong +
							" and a.block_id not in ( "+session.getAttribute("blockID") + " ) "+
					") a "+
				"where RowNum = 1 and a.projected_availability <> 0 "+
				") aa "+
		"where RowNum1 = 1 ";
		

		try {
			
			InitialContext ic = new InitialContext();
			DataSource ds = (DataSource) ic.lookup("java:comp/env/jdbc/sqlserv");
			connection = ds.getConnection();
			Statement statement = connection.createStatement();
			ResultSet rs = statement.executeQuery(query);


			int  i = 0;
			while(rs.next())
			{
				finalLocation = rs.getDouble("final_lat") + "," + rs.getDouble("final_long");
				session.setAttribute("currBlock", rs.getInt("final_block_id"));
				i++;
			}
			
			String walkingQuery = "select Time from WalkingTime " + 
					  "where Parking_block_latitude = " + finalLocation.split(",")[0] + " and Parking_block_longitude = " + finalLocation.split(",")[1] +
					  " and Dest_block_latitude = " + destLat +" and Dest_block_longitude = " + destLong;
			rs = statement.executeQuery(walkingQuery);
			
			while(rs.next())
			{
				finalLocation = finalLocation + "|" + rs.getInt("Time");
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			try {
				connection.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return finalLocation;
	}
	
	public String probabilisticGreedy(String start,String dest,HttpServletRequest request)
	{
		
		HttpSession session = request.getSession();
		if(session.getAttribute("blockID")==null)
		{
			session.setAttribute("blockID","0");
			session.setAttribute("currBlock","");
		}
		
		
		Connection connection = null;
		String finalLocation = "";
		String startSplit[] = start.split(",");
		String destSplit[] = dest.split(",");
		
		Double startLat = Double.parseDouble(startSplit[0]);
		Double startLong = Double.parseDouble(startSplit[1]);
		
		
		Double destLat = Double.parseDouble(destSplit[0].replace("(","").trim());
		Double destLong = Double.parseDouble(destSplit[1].replace(")","").trim());
		
		String destination = checkBlockMidPoint(destLat, destLong);
		destLat = Double.parseDouble(destination.split(",")[0]);
		destLong = Double.parseDouble(destination.split(",")[1]);
		
		String date = new SimpleDateFormat(ConstData.DATE_FROMAT).format(new Date());
		date = date.replace("2015", "2012");
		
		String query = "select " + 
				"aa.Parking_block_latitude final_lat, aa.Parking_block_longitude final_long , aa.Time final_time,aa.Parking_block_id final_block_id " +
				"FROM " +
					"( "+
					"select dest_info.* , availability_info.* , "+ 
					"row_number() over (order by dest_info.Time ASC ) as RowNum1 "+
					"from( " +
						"SELECT Parking_block_id "+
						",Parking_block_latitude "+
						",Parking_block_longitude "+
						",Dest_block_id "+
						",Dest_block_latitude "+
						",Dest_block_longitude "+
						",Time "+
						"FROM SFPark.dbo.WalkingTime "+
						" where Dest_block_latitude = " + destLat +
						" and Dest_block_longitude = " + destLong +
					" ) as dest_info "+
				"INNER JOIN "+
					"(select " + 
					"a.block_id, a.projected_availability, a.distance,a.block_latitude,a.block_longitude "+
					"from( "+
						"SELECT "+
						"a.block_id, a.projected_availability, b.distance,"+
						"b.block_latitude,b.block_longitude, "+
						"row_number() over ( partition by a.block_id order by projected_time desc ) as RowNum  "+
						"FROM "+
						"projected_availability a JOIN distance b "+
						"ON a.block_id = b.block_id "+
						"where "+
						"datename(dw,'" + date + "') = a.dayname "+
						"and CONVERT(TIME,'" + date+ "') >= a.projected_time "+
						"and intersection_latitude = " + startLat +
						" and intersection_longitude = " + startLong +
						" and a.block_id not in ( "+session.getAttribute("blockID")+ " ) "+
						" ) a "+
					"where RowNum = 1 "+
					"and projected_availability <> 0 "+
					") as availability_info "+
				"ON dest_info.Parking_block_id = availability_info.block_id "+
				") aa "+
		"where RowNum1 = 1 "+
		"order by distance desc ";
		
		////system.out.println("Query::::"+query);
		try {
			
			InitialContext ic = new InitialContext();
			DataSource ds = (DataSource) ic.lookup("java:comp/env/jdbc/sqlserv");
			connection = ds.getConnection();
			Statement statement = connection.createStatement();
			ResultSet rs = statement.executeQuery(query);

			int  i = 0;
			while(rs.next())
			{
				finalLocation = rs.getDouble("final_lat") + "," + rs.getDouble("final_long")+ "|" + rs.getInt("final_time");
				session.setAttribute("currBlock", rs.getInt("final_block_id"));
				i++;
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			try {
				connection.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return finalLocation;
	}

	public String baselined(String start,String dest,HttpServletRequest request)
	{
		
		HttpSession session = request.getSession();
		if(session.getAttribute("blockID")==null)
		{
			session.setAttribute("blockID","0");
			session.setAttribute("currBlock","");
		}
		
		
		Connection connection = null;
		String finalLocation = "";
		String startSplit[] = start.split(",");
		String destSplit[] = dest.split(",");
		
		Double startLat = Double.parseDouble(startSplit[0]);
		Double startLong = Double.parseDouble(startSplit[1]);
		
		
		Double destLat = Double.parseDouble(destSplit[0].replace("(","").trim());
		Double destLong = Double.parseDouble(destSplit[1].replace(")","").trim());
		
		String destination = checkBlockMidPoint(destLat, destLong);
		////system.out.println("destination: "+ destination);
		destLat = Double.parseDouble(destination.split(",")[0]);
		destLong = Double.parseDouble(destination.split(",")[1]);
		
		String date = new SimpleDateFormat(ConstData.DATE_FROMAT).format(new Date());
		date = date.replace("2015", "2012");
		//system.out.println("Date: "+date);
		String query = "SELECT " + 
				"block_id"+
				",block_latitude "+
				",block_longitude "+
				"FROM "+
					"(SELECT block_id "+
					",block_latitude "+
					",block_longitude "+
					",distance "+
					", row_number() over ( order by distance ) as RowNum "+
					"FROM distance "+
					"WHERE intersection_latitude = " + startLat +
						" and intersection_longitude = " + startLong +
						" and block_id not in ( "+session.getAttribute("blockID")+ " ) "+
					") a "+
				"WHERE RowNum = 1 ";
		try {
			
			InitialContext ic = new InitialContext();
			DataSource ds = (DataSource) ic.lookup("java:comp/env/jdbc/sqlserv");
			connection = ds.getConnection();
			Statement statement = connection.createStatement();
			ResultSet rs = statement.executeQuery(query);


			int  i = 0;
			while(rs.next())
			{
				finalLocation = rs.getDouble("block_latitude") + "," + rs.getDouble("block_longitude");
				session.setAttribute("currBlock", rs.getInt("block_id"));
				i++;
			}
			
			String walkingQuery = "select Time from WalkingTime " + 
					  "where Parking_block_latitude = " + finalLocation.split(",")[0] + " and Parking_block_longitude = " + finalLocation.split(",")[1] +
					  " and Dest_block_latitude = " + destLat +" and Dest_block_longitude = " + destLong;
			rs = statement.executeQuery(walkingQuery);
			
			while(rs.next())
			{
				finalLocation = finalLocation + "|" + rs.getInt("Time");
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			try {
				connection.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return finalLocation;
		
	}

}
