package konantech.ai.aikwc.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import konantech.ai.aikwc.entity.Agency;

@Repository
public interface AgencyRepository extends JpaRepository<Agency, Integer> {

//	@Query("select ag from agency ag where ag.pk > :agencyNum")
	@Query(value="select * from kwc_agency ag where ag.pk = :pk", nativeQuery = true)
	public Agency findOneByPk(@Param("pk") String pk);

}
