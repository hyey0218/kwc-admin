package konantech.ai.aikwc.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import konantech.ai.aikwc.entity.Group;

@Component
public interface GroupRepository extends JpaRepository<Group, Integer> {

	
	@Modifying
	@Transactional
	@Query(value = " update kwc_group set code = :code, name = :name where pk = :pk ", nativeQuery = true)
	public void updateGroup(int pk,String code, String name);
	
	
	public List<Group> findAllByAgency(@Param("agency") String agency);
}
