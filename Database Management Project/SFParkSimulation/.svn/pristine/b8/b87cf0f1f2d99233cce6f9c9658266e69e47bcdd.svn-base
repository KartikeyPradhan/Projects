package com.edu.uic.cs581.database;
import java.sql.*;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;
import javax.swing.*;
public class SqliteConnection {

		static Connection conn = null;
		public static Connection dbConnector()
		{

			try{
				Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
				Connection conn = DriverManager.getConnection("jdbc:sqlserver:kunal_sqlserver");

				return conn;
			}catch(Exception e)
			{
				
				return null;
			}
		}
}
