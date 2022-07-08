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
	
//	@Query(value="SELECT u.fences FROM User u WHERE email = :email")
	@Query(nativeQuery = true,
		   value="SELECT f.* FROM fences f "+
		   		 "JOIN user_fence uf ON uf.fence_id = f.fence_id "+
				 "JOIN users u ON u.user_id = uf.user_id "+
		   		 "WHERE u.email = :email")
	Page<Fence> findByUserEmail(@Param("email") String email, Pageable pageable); 
}
