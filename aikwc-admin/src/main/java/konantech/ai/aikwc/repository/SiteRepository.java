package konantech.ai.aikwc.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import konantech.ai.aikwc.entity.Site;

@Component
public interface SiteRepository extends JpaRepository<Site, Integer> {

	@Modifying
	@Transactional
	@Query(value = " update kwc_site set group = :group , code = :code, name = :name where pk = :pk ", nativeQuery = true)
	public int updateSite(int pk, String group, String code, String name);
	
	public List<Site> findByGrp(@Param("grp") String grp);
	
	@Query(value = "select * from kwc_site s  "
			+ "where grp IN (select pk from kwc_group where agency=:agency)", nativeQuery = true)
	public List<Site> getSiteListInAgency(@Param("agency")int agency);
	
}
