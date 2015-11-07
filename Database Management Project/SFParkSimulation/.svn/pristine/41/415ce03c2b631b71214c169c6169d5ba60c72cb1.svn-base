package com.edu.uic.cs581.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import javax.naming.InitialContext;
import javax.sql.DataSource;

import com.edu.uic.cs581.beans.Coordinates;
import com.edu.uic.cs581.constants.ConstData;


public class ExtractPoints {

	public static ArrayList<Coordinates> point;
	
	public void getCoordinates()
	{
		
		
		Statement stmt = null;
		Coordinates c1;
		point = new ArrayList<Coordinates>();

			
		Connection con = null;
		
  
		try {
			InitialContext ic = new InitialContext();
			DataSource ds = (DataSource) ic.lookup("java:comp/env/jdbc/sqlserv");
			con = ds.getConnection();
			//con = DriverManager.getConnection(ConstData.DATABASE_URL,ConstData.DATABASE_USER,ConstData.DATABASE_PWD);
			stmt = con.createStatement();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			
		String selectCoordinates = "select distinct latitude, longitude from sf_park_node";
		
		
		try {
			ResultSet rs = stmt.executeQuery(selectCoordinates);
			while(rs.next())
			{
				c1 = new Coordinates();
				c1.setLatitude(rs.getDouble("latitude"));
				c1.setLongitude(rs.getDouble("longitude"));
				point.add(c1);
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
