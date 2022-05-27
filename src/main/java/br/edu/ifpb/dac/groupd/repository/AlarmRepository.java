package br.edu.ifpb.dac.groupd.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import br.edu.ifpb.dac.groupd.model.Alarm;

@Repository
public interface AlarmRepository extends JpaRepository<Alarm, Long> {
	List<Alarm> findBySeen(Boolean seen);
	
	@Query("SELECT a FROM Alarm a WHERE a.fence.id = :fenceId")
	List<Alarm> findByFence(@Param("fenceId") Long fenceId);
	
	@Query("SELECT a FROM Alarm a WHERE a.location.bracelet.id = :braceletId")
	List<Alarm> findByBracelet(@Param("braceletId") Long braceletId);
	
	@Query("SELECT a FROM Alarm a WHERE a.location.id = :locationId")
	Optional<Alarm> findByLocation(@Param("locationId") Long locationId);
}
