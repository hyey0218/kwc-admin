package konantech.ai.aikwc.repository;

import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import konantech.ai.aikwc.entity.KTask;

public interface KTaskRepository extends JpaRepository<KTask, Integer> {

	
	public List<KTask> findAllByCollector(@Param("collector") String collector);
	
	public Long countByCollector(@Param("collector") String collector);
	
	public List<KTask> findByUseyn(@Param("useyn") String useyn);
	
	@Query(value="select t.*, c.name from kwc_task t, kwc_collector c"
			+ " where t.collector = c.pk" 
			, nativeQuery = true)
	public List<Map<String, String>> findByUseynWithCollName();
	
	
}
