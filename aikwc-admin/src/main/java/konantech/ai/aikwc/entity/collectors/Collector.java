package konantech.ai.aikwc.entity.collectors;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import konantech.ai.aikwc.entity.ECollector;
import konantech.ai.aikwc.entity.Site;
import lombok.Getter;
import lombok.Setter;

@Table(name="v_collector")
@Getter
@Setter
@Entity
@AttributeOverride(name = "pk", column = @Column(name = "pk"))
public class Collector extends ECollector{

	@Override
	public String getPackageClassName() {
		return "konantech.ai.aikwc.entity.collectors" + getClassName();
	}
	
}
