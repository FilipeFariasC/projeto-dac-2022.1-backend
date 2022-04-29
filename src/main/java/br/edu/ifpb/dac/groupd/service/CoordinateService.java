package br.edu.ifpb.dac.groupd.service;

import org.springframework.stereotype.Service;

import br.edu.ifpb.dac.groupd.model.Coordinate;

@Service
public class CoordinateService {
	
	private final Double earthRadius = 6_371e3;
	
	private Double degreesToRadians(Double degrees) {
		return degrees * Math.PI / 180;
	}
	
	public Double calculateDistance(Coordinate initial, Coordinate ending) {
		Double degreeLatitude = degreesToRadians( initial.getLatitude() - ending.getLatitude() );
		Double degreeLongitude = degreesToRadians( initial.getLongitude() - ending.getLongitude() );
		
		Double latitudeInitial = degreesToRadians(initial.getLatitude());
		Double latitudeEnding = degreesToRadians(ending.getLatitude());
		
		Double temp = Math.pow( Math.sin( degreeLatitude / 2) , 2) +
					  Math.pow( Math.sin( degreeLongitude / 2), 2) * 
					  Math.cos(latitudeInitial) * Math.cos(latitudeEnding);
		
		return Math.atan2(Math.sqrt(temp), Math.sqrt(1-temp)) * 2 * earthRadius;
	}
	
	public static void main(String[] args) {
		CoordinateService service = new CoordinateService();
		Coordinate coordinate1 = new Coordinate(-7.897862766682271, -37.11818933486939);
		Coordinate coordinate2 = new Coordinate(-7.89783619900981, -37.11825907230378);
		
		System.out.println(service.calculateDistance(coordinate1, coordinate2));
	}
}
