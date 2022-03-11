package com.cities.geo.service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.cities.geo.VO.geoname;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import org.apache.commons.text.similarity.LevenshteinDistance;

@Service
public class geoService {

	public int Test(int a, int b) throws Exception
	{		
		return a*b;
	}

	public List<geoname> GetAll(String q, double lat, double lon)
	{
		List<geoname> FL = new ArrayList<geoname>();
		
		FL = ByName(q);
		FL = Score(FL, q, lat, lon);
		
		return FL;
	}
	
	public List<geoname> ByName(String Name)
	{
		String fileName = "test.json";
		List<geoname> citiesList = new ArrayList<geoname>();
		List<geoname> filteredList = new ArrayList<geoname>();
		
		try
		{
			ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
	        CollectionType collectionTypes = mapper.getTypeFactory().constructCollectionType(List.class, geoname.class);
	        citiesList = mapper.readValue(new File(fileName), collectionTypes);

	        for (int i = 0; i < citiesList.size()-1; i++)
	        {
	        	if (citiesList.get(i).getAlt_name().contains(Name))
	        	{
	        		filteredList.add(citiesList.get(i));
				}
	        }
			
		} catch (Exception e)
		{
			System.out.print(e.getMessage());
		}
		
		return filteredList;
	}
	
	public List<geoname> Score(List<geoname> FilList, String Name, double Lat, double Lon)
	{
		double ScLat;
		double ScLon;
		double ScName;
		
		List<geoname> FilListNew = new ArrayList<geoname>();

        for (int i = 0; i < FilList.size(); i++)
        {
        	ScLat = FilList.get(i).getLat()-Lat;
        	ScLat = ScLat/10;
        	if (ScLat < 0)
        		ScLat = 1 + ScLat;
        	else
				ScLat = 1 - ScLat;
        	ScLat = ScLat * 0.33;
        	
        	ScLon = FilList.get(i).getLongt()-Lon;
        	ScLon = ScLon/10;
        	if (ScLon < 0)
        		ScLon = 1 + ScLon;
        	else
				ScLon = 1 - ScLon;
        	ScLon = ScLon * 0.33;
        	
        	if (ScLat < 0)
        		ScLat = 0;
        	if (ScLon < 0)
        		ScLon = 0;
        	
        	ScName = (levensteinRatio(Name, FilList.get(i).getName())*0.34);
        	
        	FilList.get(i).setScore(FilList.get(i).getScore()+ScName+ScLat+ScLon);
        	
        	FilListNew.add(FilList.get(i));
        }
        
		return FilListNew;
	}
    
    public double levensteinRatio(String s1, String s2)
    {
    	LevenshteinDistance lv = new LevenshteinDistance();
        return 1 - ((double) lv.apply(s1, s2)) / Math.max(s1.length(), s2.length());
    }

}
