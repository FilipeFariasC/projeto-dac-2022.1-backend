package br.edu.ifpb.dac.groupd.model.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import br.edu.ifpb.dac.groupd.model.entities.Bracelet;

@Repository
public interface BraceletRepository extends JpaRepository<Bracelet, Long> {
	Optional<Bracelet> findByName(String name);
	
//	@Query(value = "SELECT u.bracelets FROM User u WHERE email = :username")
	@Query(nativeQuery = true,
		   value="SELECT b.* FROM bracelets b "+
		   		 "JOIN user_bracelet ub ON ub.bracelet_id = b.bracelet_id "+
				 "JOIN users u ON u.user_id = ub.user_id "+
		   		 "WHERE u.email = :username")
	Page<Bracelet> findAllBraceletsByUsername(@Param("username") String username, Pageable pageable);
}
