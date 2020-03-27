package konantech.ai.aikwc.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import konantech.ai.aikwc.entity.KLog;

public interface KLogRepository extends JpaRepository<KLog, String> {
	
	
	@Query(value = "select * from kwc_log  "
			+ "where `readyn` = 'N' and `agency` = :agency", nativeQuery = true)
	public List<KLog> findByAgencyNotRead(@Param("agency") String agency, Pageable sort);

}
