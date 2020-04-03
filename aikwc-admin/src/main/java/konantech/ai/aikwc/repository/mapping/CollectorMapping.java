package konantech.ai.aikwc.repository.mapping;

import java.time.LocalDateTime;

import konantech.ai.aikwc.entity.Site;

public interface CollectorMapping {
	String getPk();
	String getCode();
	String getName();
	LocalDateTime getCtrtStart();
	LocalDateTime getCtrtEnd();
	String getUseyn();
	String getClassName();
	String getStatus();
	String getHashed();
	String getSite();
	String getSitename();
	String getGrp();
	String getGrpname();
}