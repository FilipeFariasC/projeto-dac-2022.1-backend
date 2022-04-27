package br.edu.ifpb.dac.groupd.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import br.edu.ifpb.dac.groupd.model.Alarm;


public interface AlarmRepository extends JpaRepository<Alarm, Long> {
	List<Alarm> findBySeen(Boolean seen);
	
	@Query("SELECT a FROM Alarm a WHERE a.fence.id = :fenceId")
	List<Alarm> findByFence(@Param("fenceId") Long fenceId);
	
	@Query("SELECT a FROM Alarm a WHERE a.location.bracelet.id = :braceletId")
	List<Alarm> findByBracelet(@Param("braceletId") Long braceletId);
	
}
