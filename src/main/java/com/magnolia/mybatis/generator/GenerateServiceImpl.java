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
import com.magnolia.common.DBUtils;
import com.magnolia.common.FileUtils;
import com.magnolia.common.FormatUtils;
import com.magnolia.common.GeneratorConstants;
import com.magnolia.common.VelocityContextUtils;
import com.magnolia.dto.TableInfoDTO;
  
public class GenerateServiceImpl extends BaseGenerator implements IBaseGenerator{  
  
    
    public GenerateServiceImpl(DatabaseMetaData dbMetaData,List<TableInfoDTO> tableInfoDTOList) {  
    	this.setDbMetaData(dbMetaData);
    	this.setTableInfoDTOList(tableInfoDTOList); 
    }  
      
    public void generate() throws Exception{  
        String entity = null;  
        String dto = null; 
        String dtoVar = null;

        String entityVar = null;   
        String service = null;  
        String className = null;  
    	String path = null;
    	File file = null;
    	
        ResultSet _resultSetColumn = null;
        String isAutoIncrement = null;
        String primaryKeyJava = null; //物理主键对应的java属性
        String primaryKeyJavaType = null; //物理主键对应的java属性的类型
        String firstColumnJava = null;  //第一列，如果找不到物理主键，就取第一列作为主键
        String firstColumnJavaType = null; //第一列对应的java属性的类型
        
    	String types[] = new String[]{"TABLE"};
        ResultSet resultSetTable = null;
	    Map<String, String> tableRemarksMap = new HashMap<String, String>();
        resultSetTable = getDbMetaData().getTables(null, null, "%", types);
        while(resultSetTable.next()) { 
        	tableRemarksMap.put((resultSetTable.getString("TABLE_NAME")).toLowerCase(), resultSetTable.getString("REMARKS"));
        }
        
		String fileName = null;
		Template template = null;
		StringWriter writer = null; 
		ve = new VelocityEngine(GeneratorConstants.VM_PROPERTIES);
	    //设置参数
	    ve.init();
	    VelocityContext velocityContext = VelocityContextUtils.copy(GeneratorConstants.VELOCITY_CONTEXT);
    	for(TableInfoDTO tableInfoDTO : getTableInfoDTOList()) {  
        	path = GeneratorConstants.BASE_PACKAGE_PATH +"\\"+tableInfoDTO.getSubPkg() + "\\service\\impl\\";
        	file = new File(path);
        	FileUtils.createDir(file);

            firstColumnJava = null;
            firstColumnJavaType = null;
            primaryKeyJava = null;
            primaryKeyJavaType = null;
            
        	_resultSetColumn = getDbMetaData().getColumns(null, null,  tableInfoDTO.getTableName(), null); 
            while (_resultSetColumn.next()) {
	            if(firstColumnJava == null && _resultSetColumn.isFirst()){
	            	firstColumnJava =  FormatUtils.getFormatString(_resultSetColumn.getString("COLUMN_NAME"), false);  
	            	firstColumnJavaType = DBUtils.getMysqlColumnType(_resultSetColumn.getString("TYPE_NAME"));
	            }
                isAutoIncrement = _resultSetColumn.getString("IS_AUTOINCREMENT"); 
                if(GeneratorConstants.IS_AUTOINCREMENT.equals(isAutoIncrement)){ //根据是否自增来判断是否是物理主键
                	primaryKeyJava =  FormatUtils.getFormatString(_resultSetColumn.getString("COLUMN_NAME"), false);  
	                primaryKeyJavaType = DBUtils.getMysqlColumnType(_resultSetColumn.getString("TYPE_NAME"));
                }
            }
            if(primaryKeyJava == null){
            	primaryKeyJava = firstColumnJava;
            	primaryKeyJavaType = firstColumnJavaType;
            }
        	
        	entity = FormatUtils.getFormatString(tableInfoDTO.getTableName(), true);  
        	dto = entity + "DTO";
            entityVar = FormatUtils.getFormatString(tableInfoDTO.getTableName(), false);  
            dtoVar = entityVar + "DTO";
            service = "I"+ entity + "Service";   
            className = service + "Impl";  
          
            velocityContext.put("subPkg", tableInfoDTO.getSubPkg());
            velocityContext.put("entity",entity );
            velocityContext.put("dto", dto);
            velocityContext.put("entityVar", entityVar);
            velocityContext.put("dtoVar", dtoVar);
            velocityContext.put("tableRemarks", tableRemarksMap.get(tableInfoDTO.getTableName().toLowerCase()));
            velocityContext.put("primaryKeyJava", primaryKeyJava);
            velocityContext.put("primaryKeyJavaType", primaryKeyJavaType);
            velocityContext.put("PrimaryKeyJava",FormatUtils.firstLetterCaps(primaryKeyJava));
            
    		fileName = "ServiceImpl.java";
    		template = ve.getTemplate("vmfile/java/mvc/"+fileName+".vm");       
    		writer = new StringWriter();
            template.merge(velocityContext, writer);
    		FileUtils.outputToFile(path, className, writer.toString());	
        
            
            if(tableInfoDTO.isNeedAdd()){            	
            }

            if(tableInfoDTO.isNeedBatchAdd()){           	
            }
            
            if(tableInfoDTO.isNeedBatchDelete()){            	
            }

            if(tableInfoDTO.isNeedBatchDelete()){           	
            }

            if(tableInfoDTO.isNeedUpdate()){            	
            }

            if(tableInfoDTO.isNeedSelectEntity()){              	
            }

            if(tableInfoDTO.isNeedSelectDto()){      	
            }
            
            if(tableInfoDTO.isNeedSelectList()){            	
            }

            if(tableInfoDTO.isNeedSelectPageList()){             	
            }
            
    	}  
    }  
  
}  