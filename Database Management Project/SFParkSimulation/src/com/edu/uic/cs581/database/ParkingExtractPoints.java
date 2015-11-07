package com.edu.uic.cs581.database;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import javax.naming.InitialContext;
import javax.sql.DataSource;

import com.edu.uic.cs581.beans.Coordinates;

public class ParkingExtractPoints {

	
public static ArrayList<Coordinates> midPoint;
	
	public void getCoordinates()
	{
		
		
		Statement stmt = null;
		Coordinates c1;
		midPoint = new ArrayList<Coordinates>();

			
		Connection con = null;
		
  
		try {
			InitialContext ic = new InitialContext();
			DataSource ds = (DataSource) ic.lookup("java:comp/env/jdbc/sqlserv");
			con = ds.getConnection();
			stmt = con.createStatement();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			
		String selectCoordinates = "select distinct dest_block_latitude, dest_block_longitude from midpoint_parking_slot";
		
		
		try {
			ResultSet rs = stmt.executeQuery(selectCoordinates);
			while(rs.next())
			{
				c1 = new Coordinates();
				c1.setLatitude(rs.getDouble("dest_block_latitude"));
				c1.setLongitude(rs.getDouble("dest_block_longitude"));
				midPoint.add(c1);
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			try {
				con.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
	
}
