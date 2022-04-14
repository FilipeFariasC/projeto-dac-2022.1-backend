package br.edu.ifpb.groupd.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.edu.ifpb.dac.groupd.model.Bracelet;

@Repository
public interface BraceletRepository extends JpaRepository<Bracelet, Long> {
	
	Bracelet findByname(String nome);
	Bracelet findByidBracelet(Long idBracelet);
	//List<Fences> findByiFences();
	//List<Locations> findByiLocations();
	

}
