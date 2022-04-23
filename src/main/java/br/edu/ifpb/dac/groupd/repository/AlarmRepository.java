package br.edu.ifpb.dac.groupd.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import br.edu.ifpb.dac.groupd.model.Alarm;
import br.edu.ifpb.dac.groupd.model.Fence;


public interface AlarmRepository extends JpaRepository<Alarm, Long> {
	List<Alarm> findByseen(Boolean seen);
	List<Alarm> findByfence(Fence fence);
	Fence  findByFence(Fence fence);

}
