package com.magnolia.mybatis.generator;

import java.io.File;
import java.io.StringWriter;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;

import com.magnolia.base.BaseGenerator;
import com.magnolia.base.IBaseGenerator;
import com.magnolia.common.FileUtils;
import com.magnolia.common.FormatUtils;
import com.magnolia.common.GeneratorConstants;
import com.magnolia.common.VelocityContextUtils;
import com.magnolia.dto.TableInfoDTO;

public class GenerateDTO extends BaseGenerator implements IBaseGenerator{
	
	public GenerateDTO(DatabaseMetaData dbMetaData,List<TableInfoDTO> tableInfoDTOList){
		this.setDbMetaData(dbMetaData);
		this.setTableInfoDTOList(tableInfoDTOList);
	}
	
	public void generate() throws Exception{
        StringBuilder dtoBody = null;
        String path = null;
        File file = null;
        String entity = null;
        String entityVar = null;
        String className = null;
        ResultSet resultSetColumn = null;
        String dbColumnName = null;
        String columnName = null;
        String descColumnName = null;
        String accessDomain = "private";
        String remark = null;
        
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
        	path = GeneratorConstants.BASE_PACKAGE_PATH +"\\"+tableInfoDTO.getSubPkg() + "\\dto\\";
        	file = new File(path);
        	FileUtils.createDir(file); 
        	entity = FormatUtils.getClassFormatString(tableInfoDTO.getTableName(),true);
        	entityVar = entity.substring(0,1).toLowerCase()+entity.substring(1);
        	className = entity + "DTO";

			dtoBody = new StringBuilder();
			resultSetColumn = getDbMetaData().getColumns(null, null, tableInfoDTO.getTableName(), null); 
			while(resultSetColumn.next()) {
            	dbColumnName = resultSetColumn.getString("COLUMN_NAME"); 
            	columnName = FormatUtils.getCollumnFormatString(dbColumnName, false);
            	if(columnName.toLowerCase().endsWith(GeneratorConstants.CODE_COLLUMN_END.toLowerCase()) && 
                		!columnName.toLowerCase().equals(GeneratorConstants.CODE_COLLUMN_END.toLowerCase())){
                	descColumnName = columnName.substring(0, columnName.length() - GeneratorConstants.CODE_COLLUMN_END.length()) + GeneratorConstants.DESC_COLLUMN_END;
                	remark = resultSetColumn.getString("REMARKS").replace(" ", "").replace("\t", "").replace("\r", "").replace("\n", "、");
                	dtoBody.append("    @ApiModelProperty(\""+remark+"描述\")\n");
                	dtoBody.append("    "+accessDomain + " String "+descColumnName +";\n");  
                 }
             }
			
			velocityContext.put("subPkg", tableInfoDTO.getSubPkg());
            velocityContext.put("entity", entity);
            velocityContext.put("entityVar", entityVar);
            velocityContext.put("dtoBody", dtoBody.toString());
            velocityContext.put("tableRemarks", tableRemarksMap.get(tableInfoDTO.getTableName().toLowerCase()));
            
    		fileName = "DTO.java";
    		template = ve.getTemplate("vmfile/java/mvc/"+fileName+".vm");       
    		writer = new StringWriter();
            template.merge(velocityContext, writer);
    		FileUtils.outputToFile(path, className, writer.toString());	

		}
	}
	

}
