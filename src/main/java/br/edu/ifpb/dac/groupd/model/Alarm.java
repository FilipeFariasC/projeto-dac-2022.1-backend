package br.edu.ifpb.dac.groupd.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import br.edu.ifpb.dac.groupd.validation.contraints.ValidTimestamp;

@Entity
@Table(name="alarm")
public class Alarm implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 9023853450528858907L;
	
	@Id
	@Column(name="alarm_id")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;

	private Fence fence;
	
	@NotNull
	@Column(name="data_de_registro_date")
	@ValidTimestamp
	private LocalDateTime dataDeRegistro;
	
	private Boolean visto;
	
	
	private Location loation;

	
	public Fence getFence() {
		return fence;
	}

	public void setFence(Fence fence) {
		this.fence = fence;
	}

	public LocalDateTime getData() {
		return dataDeRegistro;
	}

	public void setData(LocalDateTime dataDeResitro) {
		this.dataDeRegistro = dataDeResitro;
	}

	public Boolean getVisto() {
		return visto;
	}

	public void setVisto(Boolean visto) {
		this.visto = visto;
	}

	public Location getLoation() {
		return loation;
	}

	public void setLoation(Location loation) {
		this.loation = loation;
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Override
	public int hashCode() {
		return Objects.hash(dataDeRegistro, fence, loation, visto);
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
		return Objects.equals(dataDeRegistro, other.dataDeRegistro) && Objects.equals(fence, other.fence)
				&& Objects.equals(loation, other.loation) && Objects.equals(visto, other.visto);
	}

	@Override
	public String toString() {
		return "Alarm [fence=" + fence + ", data=" + dataDeRegistro + ", visto=" + visto + ", loation=" + loation + "]";
	}
	
	

}
