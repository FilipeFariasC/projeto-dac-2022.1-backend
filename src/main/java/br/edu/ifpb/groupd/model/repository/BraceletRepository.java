package br.edu.ifpb.groupd.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.edu.ifpb.dac.groupd.model.Bracelet;

@Repository
public interface BraceletRepository extends JpaRepository<Bracelet, Long> {
	
	Bracelet findByname(String nome);
	//List<Fences> findByiFences();
	//List<Locations> findByiLocations();
	

}
