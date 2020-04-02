package konantech.ai.aikwc.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import konantech.ai.aikwc.entity.Collector;


@Repository
public interface CollectorRepository extends JpaRepository<Collector, Integer> {

	
	@Query(value="select kc.*, "
			+ " kg.code as g_code, kg.name as g_name, ks.code as s_code, ks.name as s_name"
			+ " from v_collector kc, kwc_group kg, kwc_site ks"
			+ " where kc.site = ks.pk and ks.grp = kg.pk"
			, nativeQuery = true)
	public List<Collector> findAllWithJoin();
	
	public List<Collector> findByUseyn(@Param("useyn") String useyn);
	
	@Query(value="select `pk`, `status` from v_collector", nativeQuery = true)
	public List<Collector> findStatus();
	
	
	@Query(value="select * from v_collector"
			+ " where useyn='Y' and site = :site" 
			, nativeQuery = true)
	public List<Collector> findAllInSite(@Param("site") String site);
	
	public List<Collector> findBySite(@Param("site") String site);
	
	@Query(value="select * from kwc.v_collector where site IN("
			+ "select pk from kwc_site s "
			+ "where grp IN (select pk from kwc_group where agency= :agency) )"
			, nativeQuery = true)
	public List<Collector> findInAgency(@Param("agency") int agency);
	
	
	@Modifying
	@Transactional
	@Query(value = " update v_collector set status = :stat where pk = :pk ", nativeQuery = true)
	public void updateStatus(@Param("pk") int pk, @Param("stat") String status);
}
