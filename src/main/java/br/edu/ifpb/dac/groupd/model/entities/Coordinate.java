package br.edu.ifpb.dac.groupd.model.entities;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Embeddable
public class Coordinate  implements Serializable{
	private static final long serialVersionUID = 1213024495538646400L;

	@NotNull
	@Min(-90)
	@Max(90)
	@Column(name="latitude", columnDefinition="NUMERIC(10,8)")
	private Double latitude;
	
	@NotNull
	@Min(-180)
	@Max(180)
	@Column(name="longitude", columnDefinition="NUMERIC(11,8)")
	private Double longitude;
	
	public Coordinate() {}
	public Coordinate(@NotNull @Min(-90) @Max(90) Double latitude, @NotNull @Min(-180) @Max(180) Double longitude) {
		super();
		this.latitude = latitude;
		this.longitude = longitude;
	}

	public Double getLatitude() {
		return latitude;
	}

	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}

	public Double getLongitude() {
		return longitude;
	}

	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}
	@Override
	public String toString() {
		return "Coordinate [latitude=" + latitude + ", longitude=" + longitude + "]";
	}
	@Override
	public int hashCode() {
		return Objects.hash(latitude, longitude);
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Coordinate other = (Coordinate) obj;
		return Objects.equals(latitude, other.latitude) && Objects.equals(longitude, other.longitude);
	}
	
}
