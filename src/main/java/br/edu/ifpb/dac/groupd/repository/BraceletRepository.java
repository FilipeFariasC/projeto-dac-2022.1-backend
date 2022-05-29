package br.edu.ifpb.dac.groupd.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import br.edu.ifpb.dac.groupd.model.Bracelet;

@Repository
public interface BraceletRepository extends JpaRepository<Bracelet, Long> {
	Optional<Bracelet> findByName(String name);
	
	@Query("SELECT u.bracelets FROM User u WHERE u.email = :username ORDER BY u.id")
	List<Bracelet> findByUsername(@Param("username") String username);
}
