package konantech.ai.aikwc.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import konantech.ai.aikwc.entity.KTask;

public interface KTaskRepository extends JpaRepository<KTask, Integer> {

	
	public List<KTask> findAllByCollector(@Param("collector") String collector);
	
	public Long countByCollector(@Param("collector") String collector);
}
