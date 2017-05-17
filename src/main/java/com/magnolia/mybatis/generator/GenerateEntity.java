package com.magnolia.mybatis.generator;

import java.io.File;
import java.io.StringWriter;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;

import com.magnolia.base.BaseGenerator;
import com.magnolia.base.IBaseGenerator;
import com.magnolia.common.DBUtils;
import com.magnolia.common.FileUtils;
import com.magnolia.common.FormatUtils;
import com.magnolia.common.GeneratorConstants;
import com.magnolia.common.VelocityContextUtils;
import com.magnolia.dto.TableInfoDTO;

public class GenerateEntity extends BaseGenerator implements IBaseGenerator{


	public GenerateEntity(DatabaseMetaData dbMetaData,List<TableInfoDTO> tableInfoDTOList){
		this.setDbMetaData(dbMetaData);
		this.setTableInfoDTOList(tableInfoDTOList);
	}
	
	
	public void generate() throws Exception{
		try {
			ResultSet resultSetColumn = null;

            StringBuilder entityBody = null;
			String accessDomain = "private";
			String className = null;
            String remark = null;
            String columnDBType = null;  
            String columnJavaType = null;

            String path = null;
            File file = null;
            String dbColumnName = null;
            String columnName = null;
    		String fileName = null;
    		Template template = null;
    		StringWriter writer = null; 
    		ve = new VelocityEngine(GeneratorConstants.VM_PROPERTIES);
    	    //设置参数
    	    ve.init();
    	    VelocityContext velocityContext = VelocityContextUtils.copy(GeneratorConstants.VELOCITY_CONTEXT);
    	    
    	    String types[] = new String[]{"TABLE"};
            ResultSet _resultSetTable = null;
    	    Map<String, String> tableRemarksMap = new HashMap<String, String>();
    	    _resultSetTable = getDbMetaData().getTables(null, null, "%", types);
            while(_resultSetTable.next()) { 
            	tableRemarksMap.put((_resultSetTable.getString("TABLE_NAME")).toLowerCase(), _resultSetTable.getString("REMARKS"));
            }
    	    
            for(TableInfoDTO tableInfoDTO : getTableInfoDTOList()) {
            	path = GeneratorConstants.BASE_PACKAGE_PATH +"\\"+tableInfoDTO.getSubPkg()+ "\\entity\\";
            	file = new File(path);
            	FileUtils.createDir(file);
            	className = FormatUtils.getClassFormatString(tableInfoDTO.getTableName(),true);
	            resultSetColumn = getDbMetaData().getColumns(null, null, tableInfoDTO.getTableName(), null);  

	            entityBody = new StringBuilder();
	            while(resultSetColumn.next()) {  
	            	dbColumnName = resultSetColumn.getString("COLUMN_NAME"); 
	                if(!GeneratorConstants.COMMON_COLLUMN_SET.contains(dbColumnName.toLowerCase())){ // 删除审计字段，审计字段从base entity继承
		                remark = resultSetColumn.getString("REMARKS").replace(" ", "").replace("\t", "").replace("\r", "").replace("\n", "、");
		                columnDBType = resultSetColumn.getString("TYPE_NAME");  
		                columnJavaType = DBUtils.getMysqlColumnType(columnDBType);
		                entityBody.append("    @ApiModelProperty(\""+remark+"\")\n");
		                entityBody.append("    "+accessDomain + " ");  
		                entityBody.append(columnJavaType + " ");  
		                columnName = FormatUtils.getCollumnFormatString(dbColumnName, false);
		                entityBody.append(columnName+";    //"+remark+"\n");  	                	
	                }
	            } 

	            velocityContext.put("subPkg", tableInfoDTO.getSubPkg());
	            velocityContext.put("entityBody", entityBody);
	            velocityContext.put("tableRemarks", tableRemarksMap.get(tableInfoDTO.getTableName().toLowerCase()));
	    		
	    		fileName = "Entity.java";
	    		template = ve.getTemplate("vmfile/java/mvc/"+fileName+".vm");       
	    		writer = new StringWriter();
	            template.merge(velocityContext, writer);
	    		FileUtils.outputToFile(path, className, writer.toString());	
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	

}
