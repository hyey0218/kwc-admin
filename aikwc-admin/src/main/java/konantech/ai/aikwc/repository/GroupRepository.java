package konantech.ai.aikwc.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import konantech.ai.aikwc.entity.Group;

@Component
public interface GroupRepository extends JpaRepository<Group, Integer> {

	
	@Modifying
	@Transactional
	@Query(value = " update kwc_group set code = :code, name = :name where pk = :pk ", nativeQuery = true)
	public void updateGroup(int pk,String code, String name);
}
