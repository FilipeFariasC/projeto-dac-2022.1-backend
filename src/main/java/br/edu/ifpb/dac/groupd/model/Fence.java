package br.edu.ifpb.dac.groupd.model;

import static br.edu.ifpb.dac.groupd.validation.validator.ModelValidator.validBracelet;

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
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import br.edu.ifpb.dac.groupd.exception.FenceEmptyException;
import br.edu.ifpb.dac.groupd.exception.NoBraceletAvailableException;
import br.edu.ifpb.dac.groupd.interfaces.Timer;

@Entity
@Table(name="fences")
public class Fence implements Serializable, Timer{
	
	private static final long serialVersionUID = 1200735563660141090L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="fence_id")
	private Long id;
	
	@NotEmpty
	@Size(min=1,max=50)
	@Column(name="name")
	private String name;
	
	@NotNull
	@Embedded
	@Valid
	private Coordinate coordinate;
	
	@Column(name="start_time")
	private LocalTime startTime;
	
	@Column(name="finish_time")
	private LocalTime finishTime;
	
	@NotNull
	@Column(name="active")
	private boolean active = false;
	
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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name.trim();
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

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) throws FenceEmptyException, NoBraceletAvailableException {
		if(active && bracelets.isEmpty()){
			throw new FenceEmptyException(id);
		}
		if(!active) {
			for(Bracelet bracelet : bracelets) {
				if(bracelet.getMonitor() == this) {
					bracelet.setMonitor(null);
				}
			}
			return;
		} 
		boolean hasBraceletAvailable = false;
		for(Bracelet bracelet : bracelets) {
			if(bracelet.getMonitor() == null) {
				bracelet.setMonitor(this);
				hasBraceletAvailable = true;
			}
		}
		if(!hasBraceletAvailable) {
			throw new NoBraceletAvailableException(id);
		}
		
		this.active = active;
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
		if(validBracelet(bracelet)) {
			this.bracelets.add(bracelet);
			bracelet.getFences().add(this);
		}
	}
	
	public void removeBracelet(Bracelet bracelet) {
		this.bracelets.remove(bracelet);
		bracelet.getFences().remove(this);
	}

	@Override
	public String toString() {
		return "Fence [coordinate=" + coordinate.toString() + ", startTime=" + startTime + ", finishTime=" + finishTime
				+ ", active=" + active + ", radius=" + radius + "]";
	}
	
}
