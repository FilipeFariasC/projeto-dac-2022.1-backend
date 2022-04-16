package br.edu.ifpb.dac.groupd.model.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.edu.ifpb.dac.groupd.model.Bracelet;
import br.edu.ifpb.dac.groupd.model.Location;

@Repository
public interface BraceletRepository extends JpaRepository<Bracelet, Long> {
	
	Bracelet findByname(String nome);
	//List<Fences> findByiFences();
	List<Location> findByiLocations();
	

}
