package com.edu.uic.cs581.database;

import java.util.ArrayList;

import com.edu.uic.cs581.beans.Coordinates;
import com.edu.uic.cs581.beans.DistanceMap;
import com.edu.uic.cs581.constants.ConstData;


public class Weight {

	
	public DistanceMap pointWeight(Double latitude, Double longitude, ArrayList<Coordinates> point)
	{
		DistanceMap d1 ;
		Double dist;
		ArrayList<DistanceMap> distPoint = new ArrayList<DistanceMap>();
		DistanceMap coodinate = new DistanceMap();
		
		for(Coordinates i : point)
		{
			d1 = new DistanceMap();
			dist=ConstData.INTERSECTION_DISTANCE_MULTIPLIER * Math.sqrt(((i.getLongitude() - longitude)*(i.getLongitude() - longitude))
					+((i.getLatitude() - latitude)*(i.getLatitude() - latitude)));
			//d1.point.latitude = i.latitude;
			d1.getPoint().setLatitude(i.getLatitude());
			d1.getPoint().setLongitude(i.getLongitude());
			
			d1.setDist(dist);
		
			distPoint.add(d1);
			//System.out.println(d1.getPoint().getLatitude()+" , "+d1.getPoint().getLongitude()+" , "+d1.getDist());
			
			
		}
		
		dist = distPoint.get(0).getDist();
		
		coodinate.setDist(distPoint.get(0).getDist());
		coodinate.getPoint().setLatitude(distPoint.get(0).getPoint().getLatitude());
		coodinate.getPoint().setLongitude(distPoint.get(0).getPoint().getLongitude());
		for(DistanceMap i : distPoint)
		{
			
			if (dist > i.getDist())
			{
				dist = i.getDist();
				coodinate.getPoint().setLatitude(i.getPoint().getLatitude());
				coodinate.getPoint().setLongitude(i.getPoint().getLongitude());
				coodinate.setDist(i.getDist());
			}
		}

		return coodinate;
	}
	
	
}
