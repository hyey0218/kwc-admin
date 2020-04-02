package konantech.ai.aikwc.repository;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import konantech.ai.aikwc.entity.KLog;

public interface KLogRepository extends JpaRepository<KLog, String> {
	
	
	@Query(value = "select * from kwc_log  "
			+ "where `readyn` = 'N' and `agency` = :agency", nativeQuery = true)
	public List<KLog> findByAgencyNotRead(@Param("agency") String agency, Pageable sort);
	
	@Query(value = "select pk,type,agency,log_cont,comment, DATE_FORMAT(create_date, '%Y-%m-%d %h:%i:%s') as create_date"
			+ ", readyn, delyn from kwc_log  "
			+ "where `readyn` = :readyn ", nativeQuery = true)
	public List<Map<String, String>> findByReadyn(@Param("readyn") String readyn, Pageable sort);
	public List<KLog> findByAgencyOrderByCreateDateDesc(@Param("agency") String agency);
	public List<KLog> findAllByOrderByCreateDateDesc();
	
	public Long countByReadyn(@Param("readyn") String readyn);
	
	
	@Modifying
	@Transactional
	@Query(value = " update kwc_log set readyn = 'Y' where readyn='N' ", nativeQuery = true)
	public void updateRead();

}
