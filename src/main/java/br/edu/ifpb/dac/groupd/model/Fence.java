package br.edu.ifpb.dac.groupd.model;

import java.io.Serializable;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import br.edu.ifpb.dac.groupd.interfaces.Timer;

@Entity
@Table(name="t_fence")
public class Fence implements Serializable, Timer{
	
	private static final long serialVersionUID = 1200735563660141090L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="fence_id")
	private Long id;
	
	@NotNull
	@Embedded
	private Coordinate coordinate;
	
	@Column(name="start_time"
//			, columnDefinition = "TIME"
			)
	private LocalTime startTime;
	
	@Column(name="finish_time"
//			, columnDefinition = "TIME"
			)
	private LocalTime finishTime;
	
	@NotNull
	@Column(name="status"
//	, columnDefinition = "BIT"
	)
	private Boolean status;
	
	@NotNull
	@Min(1)
	@Column(name="radius", columnDefinition = "NUMERIC(4,2)")
	private Double radius;
	
	@Valid
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "fence_bracelet",
	joinColumns = @JoinColumn(name = "fence_id"),
	inverseJoinColumns = @JoinColumn(name = "bracelet_id"))
	private Set<@Valid Bracelet> bracelets = new HashSet<>();
	

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
	public LocalTime getStartTime() {
		return startTime;
	}

	public void setStartTime(LocalTime startTime) {
		this.startTime = startTime;
	}

	public LocalTime getFinishTime() {
		return finishTime;
	}

	public void setFinishTime(LocalTime finishTime) {
		this.finishTime = finishTime;
	}

	public Boolean isStatus() {
		return status;
	}

	public void setStatus(Boolean status) {
		this.status = status;
	}

	public Set<Bracelet> getBracelets() {
		return bracelets;
	}

	public void setBracelets(Set<Bracelet> bracelets) {
		this.bracelets = bracelets;
	}

	public Double getRadius() {
		return radius;
	}

	public void setRadius(Double radius) {
		this.radius = radius;
	}
	
	public void addBracelet(Bracelet bracelet) {
		this.bracelets.add(bracelet);
		bracelet.getFences().add(this);
	}
	
	public void removeBracelet(Bracelet bracelet) {
		this.bracelets.remove(bracelet);
		bracelet.getFences().remove(this);
	}
	
}
