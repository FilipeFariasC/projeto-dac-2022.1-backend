package br.edu.ifpb.dac.groupd.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.edu.ifpb.dac.groupd.model.Fence;

public interface FenceRepository extends JpaRepository<Fence, Long> {
}
