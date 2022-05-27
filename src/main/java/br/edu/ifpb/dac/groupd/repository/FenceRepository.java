package br.edu.ifpb.dac.groupd.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.edu.ifpb.dac.groupd.model.Fence;

@Repository
public interface FenceRepository extends JpaRepository<Fence, Long> {
}
