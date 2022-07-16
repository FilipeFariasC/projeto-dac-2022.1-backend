package br.edu.ifpb.dac.groupd.model.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import br.edu.ifpb.dac.groupd.model.entities.Bracelet;
import br.edu.ifpb.dac.groupd.model.entities.Location;

@Repository
public interface LocationRepository extends JpaRepository<Location, Long> {
	List<Location> findByBracelet(Bracelet bracelet);
	
	@Query("SELECT l FROM Location l WHERE l.bracelet.bracelet_id = :braceletId")
	Page<Location> findByBraceletId(@Param("braceletId") Long id, Pageable pageable);
}
