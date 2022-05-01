package br.edu.ifpb.dac.groupd.model;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Entity
@Table(name="alarms")
public class Alarm implements Serializable{
	
	private static final long serialVersionUID = 9023853450528858907L;
	
	@Id
	@Column(name="alarm_id")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;

	@NotNull
	@Column(name="seen")
	private boolean seen;
	
	@Valid
	@NotNull
	@OneToOne
	@JoinColumn(name="fence_id")
	private Fence fence;
	
	@Valid
	@OneToOne
	@JoinColumn(name="location_id")
	private Location location;

	
	public Fence getFence() {
		return fence;
	}

	public void setFence(Fence fence) {
		this.fence = fence;
	}

	public boolean isSeen() {
		return seen;
	}

	public void setSeen(boolean seen) {
		this.seen = seen;
	}

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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
		Alarm other = (Alarm) obj;
		return Objects.equals(id, other.id);
	}
	
}
