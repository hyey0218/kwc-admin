package konantech.ai.aikwc.dao;

import java.lang.reflect.Field;
import java.util.List;

import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import konantech.ai.aikwc.entity.DataEntity;

@Component
public class DataTableDao {

	@Autowired
	EntityManager em;
	
	public void saveDataTable(List<DataEntity> list, String tableName) throws SecurityException, ClassNotFoundException{
		StringBuffer sql = new StringBuffer();
		sql.append("INSERT INTO %s ( %s ) values ( ");
		Field[] fs = Class.forName("konantech.ai.aikwc.entity.DataEntity").getFields();
		String fieldNames = "";
		String fieldValues = "";
		for(Field f : fs) {
			fieldNames += f.getName() + ",";
			
		}
		sql.substring(sql.length()-1, sql.length());
		
		sql.append(") values ( ");
		
		list.forEach((entity)->{
			
		});
	
		
	}
}
