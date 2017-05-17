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
import org.apache.velocity.app.Velocity;
import org.apache.velocity.app.VelocityEngine;

import com.magnolia.base.BaseGenerator;
import com.magnolia.base.IBaseGenerator;
import com.magnolia.common.FileUtils;
import com.magnolia.common.FormatUtils;
import com.magnolia.common.GeneratorConstants;
import com.magnolia.common.VelocityContextUtils;
import com.magnolia.dto.TableInfoDTO;

public class GenerateRequestAndResponse extends BaseGenerator implements IBaseGenerator{
	
	public GenerateRequestAndResponse(DatabaseMetaData dbMetaData,List<TableInfoDTO> tableInfoDTOList){
		this.setDbMetaData(dbMetaData);
		this.setTableInfoDTOList(tableInfoDTOList);
	}
	
	public void generate() throws Exception{
		
    	String types[] = new String[]{"TABLE"};
        ResultSet resultSetTable = null;
	    Map<String, String> tableRemarksMap = new HashMap<String, String>();
        resultSetTable = getDbMetaData().getTables(null, null, "%", types);
        while(resultSetTable.next()) { 
        	tableRemarksMap.put((resultSetTable.getString("TABLE_NAME")).toLowerCase(), resultSetTable.getString("REMARKS"));
        }
		
		generateRequest(tableRemarksMap);
		generateResponse(tableRemarksMap);
	}
	
	public void generateRequest(Map<String, String> tableRemarksMap) throws Exception{
        String path = null;
        File file = null;
        String entity = null;
        String entityVar = null;
    	String dto = null;
    	String dtoVar = null;

        String className = null;
		String fileName = null;
		Template template = null;
		StringWriter writer = null; 
		ve = new VelocityEngine(GeneratorConstants.VM_PROPERTIES);
	    //设置参数
	    ve.init();
	    VelocityContext velocityContext = VelocityContextUtils.copy(GeneratorConstants.VELOCITY_CONTEXT);
	    
        for(TableInfoDTO tableInfoDTO : getTableInfoDTOList()) {
        	path = GeneratorConstants.BASE_PACKAGE_PATH +"\\"+tableInfoDTO.getSubPkg() + "\\dto\\request\\";
        	file = new File(path);
        	FileUtils.createDir(file);
        	entity = FormatUtils.getClassFormatString(tableInfoDTO.getTableName(),true);
        	entityVar = FormatUtils.getClassFormatString(tableInfoDTO.getTableName(),false);
        	
            velocityContext.put("subPkg", tableInfoDTO.getSubPkg());
            velocityContext.put("entity",entity );
            velocityContext.put("entityVar", entityVar);
            velocityContext.put("dto", dto);
            velocityContext.put("dtoVar", dtoVar);
            velocityContext.put("tableRemarks", tableRemarksMap.get(tableInfoDTO.getTableName().toLowerCase()));
            
            className = entity + "Request";
    		fileName = "Request.java";
    		template = ve.getTemplate("vmfile/java/mvc/"+fileName+".vm");       
    		writer = new StringWriter();
            template.merge(velocityContext, writer);
    		FileUtils.outputToFile(path, className, writer.toString());	
    		
            className = entity + "PageListRequest";
    		fileName = "PageListRequest.java";
    		template = ve.getTemplate("vmfile/java/mvc/"+fileName+".vm");       
    		writer = new StringWriter();
            template.merge(velocityContext, writer);
    		FileUtils.outputToFile(path, className, writer.toString());	
		}
           
	}
	
	public void generateResponse(Map<String, String> tableRemarksMap) throws Exception{
        String path = null;
        File file = null;
        String entity = null;
        String entityVar = null;
        String dto = null;
        String dtoVar = null;

        String className = null;
		String fileName = null;
		Template template = null;
		StringWriter writer = null; 
		ve = new VelocityEngine(GeneratorConstants.VM_PROPERTIES);
	    //设置参数
	    ve.setProperty(Velocity.FILE_RESOURCE_LOADER_PATH, GeneratorConstants.VM_BASE_PATH+"/java/mvc");
	    ve.init();
	    VelocityContext velocityContext = VelocityContextUtils.copy(GeneratorConstants.VELOCITY_CONTEXT);
        for(TableInfoDTO tableInfoDTO : getTableInfoDTOList()) {
        	path = GeneratorConstants.BASE_PACKAGE_PATH +"\\"+tableInfoDTO.getSubPkg() + "\\dto\\response\\";
        	file = new File(path);
        	FileUtils.createDir(file);
        	entity = FormatUtils.getClassFormatString(tableInfoDTO.getTableName(),true);
        	entityVar =  entity.substring(0, 1).toLowerCase() + entity.substring(1);
        	dto = entity +"DTO";
        	dtoVar = dto.substring(0, 1).toLowerCase() + dto.substring(1);
        	
            velocityContext.put("subPkg", tableInfoDTO.getSubPkg());
            velocityContext.put("entity",entity );
            velocityContext.put("entityVar", entityVar);
            velocityContext.put("dto", dto);
            velocityContext.put("dtoVar", dtoVar);
            velocityContext.put("tableRemarks", tableRemarksMap.get(tableInfoDTO.getTableName().toLowerCase()));
            
        	className = entity + "Response";
    		fileName = "Response.java";
    		template = ve.getTemplate("vmfile/java/mvc/"+fileName+".vm");       
    		writer = new StringWriter();
            template.merge(velocityContext, writer);
    		FileUtils.outputToFile(path, className, writer.toString());	
    		
            className = entity + "PageListResponse";
    		fileName = "PageListResponse.java";
    		template = ve.getTemplate("vmfile/java/mvc/"+fileName+".vm");       
    		writer = new StringWriter();
            template.merge(velocityContext, writer);
    		FileUtils.outputToFile(path, className, writer.toString());	
		}
       

	}
	

}
