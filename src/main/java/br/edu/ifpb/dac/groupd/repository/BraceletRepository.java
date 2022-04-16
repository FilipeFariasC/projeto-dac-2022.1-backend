package br.edu.ifpb.dac.groupd.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.edu.ifpb.dac.groupd.model.Bracelet;

@Repository
public interface BraceletRepository extends JpaRepository<Bracelet, Long> {
	Optional<Bracelet> findByName(String name);
}
