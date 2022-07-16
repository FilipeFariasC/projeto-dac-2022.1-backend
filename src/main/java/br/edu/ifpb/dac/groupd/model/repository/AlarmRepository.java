package br.edu.ifpb.dac.groupd.model.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import br.edu.ifpb.dac.groupd.model.entities.Alarm;

@Repository
public interface AlarmRepository extends JpaRepository<Alarm, Long> {
	List<Alarm> findBySeen(Boolean seen);
	
	@Query("SELECT a FROM Alarm a WHERE a.fence.id = :fenceId")
	Page<Alarm> findByFence(@Param("fenceId") Long fenceId, Pageable pageable);
	
	@Query("SELECT a FROM Alarm a WHERE a.location.bracelet.id = :braceletId")
	Page<Alarm> findByBracelet(@Param("braceletId") Long braceletId, Pageable pageable);
	
	@Query("SELECT a FROM Alarm a WHERE a.location.id = :locationId")
	Optional<Alarm> findByLocation(@Param("locationId") Long locationId);
}
