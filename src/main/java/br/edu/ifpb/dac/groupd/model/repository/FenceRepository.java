package br.edu.ifpb.dac.groupd.model.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import br.edu.ifpb.dac.groupd.model.entities.Fence;

@Repository
public interface FenceRepository extends JpaRepository<Fence, Long> {
	
	@Query(value="SELECT f FROM Fence f WHERE f.user.email = :email")
	Page<Fence> findAllFencesByUser(@Param("email") String email, Pageable pageable);
	
	@Query(value="SELECT f FROM Fence f WHERE f.user.email = :email AND lower(f.name) LIKE lower(concat('%', :name,'%'))")
	Page<Fence> searchUserFenceByName(@Param("email") String email, @Param("name") String name, Pageable pageable); 
}
