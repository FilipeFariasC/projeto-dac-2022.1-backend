package br.edu.ifpb.dac.groupd.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import br.edu.ifpb.dac.groupd.validation.contraints.ValidTimestamp;

@Entity
@Table(name="t_location")
public class Location implements Serializable{
	
	private static final long serialVersionUID = -5412450641940365923L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="location_id")
	private Long id;
	
	@NotNull
	@Valid
	@Embedded 
	private Coordinate coordinate;
	
	@NotNull
	@Column(name="creation_date")
	@ValidTimestamp
	private LocalDateTime creationDate;
	
	@Valid
	@NotNull
	@OneToOne
	@JoinColumn(name="bracelet_id")
	private Bracelet bracelet;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	public Coordinate getCoordinate() {
		return coordinate;
	}

	public void setCoordinate(Coordinate coordinate) {
		this.coordinate = coordinate;
	}

	public LocalDateTime getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(LocalDateTime creationDate) {
		this.creationDate = creationDate;
	}

	public Bracelet getBracelet() {
		return bracelet;
	}

	public void setBracelet(Bracelet bracelet) {
		this.bracelet = bracelet;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Location other = (Location) obj;
		return Objects.equals(id, other.id);
	}
	
	
	
}
