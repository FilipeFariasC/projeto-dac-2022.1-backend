package br.edu.ifpb.dac.groupd.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import br.edu.ifpb.dac.groupd.enums.FenceStatus;

@Entity
@Table(name="fence")
public class Fence  implements Serializable{
	
	private static final long serialVersionUID = 1200735563660141090L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="fence_id")
	private Long id;
	
	@NotNull
	@Embedded
	private Coordinate coordinates;
	
	
	private LocalDateTime start;
	
	private LocalDateTime finish;
	
	@Enumerated(EnumType.STRING)
	private FenceStatus status;
	
	@ManyToMany
	@JoinTable(name="fence_bracelet",
			joinColumns = @JoinColumn(name="fence_id"),
			inverseJoinColumns = @JoinColumn(name="bracelet_id"))
	private Set<Bracelet> bracelets;
	
	@NotNull
	@Min(1)
	private Double radius;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Coordinate getCoordinates() {
		return coordinates;
	}

	public void setCoordinates(Coordinate coordinates) {
		this.coordinates = coordinates;
	}

	public LocalDateTime getStart() {
		return start;
	}

	public void setStart(LocalDateTime start) {
		this.start = start;
	}

	public LocalDateTime getFinish() {
		return finish;
	}

	public void setFinish(LocalDateTime finish) {
		this.finish = finish;
	}

	public FenceStatus getStatus() {
		return status;
	}

	public void setStatus(FenceStatus status) {
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
	
	
}
