package com.magnolia.mybatis.generator;

import java.io.File;
import java.io.StringWriter;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.util.List;
import java.util.Properties;

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
  
public class GenerateService extends BaseGenerator implements IBaseGenerator{  

	
    public GenerateService(DatabaseMetaData dbMetaData,List<TableInfoDTO> tableInfoDTOList) {  
    	this.setDbMetaData(dbMetaData);
    	this.setTableInfoDTOList(tableInfoDTOList); 
    }  
      
    public void generate() throws Exception{  
        String entity = null;
        String entityVar = null;
        String dto = null;
        String dtoVar = null;
        String className = null;
        String path = null;
        File file = null;
        
        ResultSet _resultSetColumn = null;
        String isAutoIncrement = null;
        String primaryKeyJava = null; //物理主键对应的java属性
        String primaryKeyJavaType = null; //物理主键对应的java属性的类型
        String firstColumnJava = null;  //第一列，如果找不到物理主键，就取第一列作为主键
        String firstColumnJavaType = null; //第一列对应的java属性的类型
        
		String fileName = null;
		Template template = null;
		StringWriter writer = null; 
		ve = new VelocityEngine(GeneratorConstants.VM_PROPERTIES);
	    //设置参数
	    ve.init();
	    VelocityContext velocityContext = VelocityContextUtils.copy(GeneratorConstants.VELOCITY_CONTEXT);
        for(TableInfoDTO tableInfoDTO : getTableInfoDTOList()) {  
        	path = GeneratorConstants.BASE_PACKAGE_PATH +"\\"+tableInfoDTO.getSubPkg() +"\\service\\";
        	file = new File(path);
        	FileUtils.createDir(file);
            entity = FormatUtils.getFormatString(tableInfoDTO.getTableName(), true); 
            dto = entity + "DTO";
            entityVar = entity.substring(0,1).toLowerCase()+entity.substring(1);
            dtoVar = entityVar + "DTO";
            className =  "I" +entity + "Service";

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
            
            velocityContext.put("subPkg", tableInfoDTO.getSubPkg());
            velocityContext.put("entity",entity );
            velocityContext.put("dto", dto);
            velocityContext.put("entityVar", entityVar);
            velocityContext.put("dtoVar", dtoVar);
            velocityContext.put("primaryKeyJava", primaryKeyJava);
            velocityContext.put("primaryKeyJavaType", primaryKeyJavaType);
            velocityContext.put("PrimaryKeyJava",FormatUtils.firstLetterCaps(primaryKeyJava));
            
    		fileName = "Service.java";
    		template = ve.getTemplate("vmfile/java/mvc/"+fileName+".vm");       
    		writer = new StringWriter();
            template.merge(velocityContext, writer);
    		FileUtils.outputToFile(path, className, writer.toString());	
            
            
            if(tableInfoDTO.isNeedAdd()){      	
            }

            if(tableInfoDTO.isNeedBatchAdd()){       	
            }

            if(tableInfoDTO.isNeedDelete()){         	
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
  
    
    public static void main(String[] args) throws Exception{
        //初始化参数
        Properties properties=new Properties();
        //设置velocity资源加载方式为file
        properties.setProperty("resource.loader", "file");
        //设置velocity资源加载方式为file时的处理类
        properties.setProperty("file.resource.loader.class","org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
        //实例化一个VelocityEngine对象
        VelocityEngine velocityEngine= new VelocityEngine(properties);
        
        //实例化一个VelocityContext
        VelocityContext context=new VelocityContext();
        //向VelocityContext中放入键值
        context.put("project.name.var", "11111");
        context.put("application", "22222");
        context.put("project.application.needSSO ", "true");
        context.put("project.address", "陕西西安"); 
        context.put("blog", "http://blogjava.net/sxyx2008");
        //实例化一个StringWriter
        StringWriter writer=new StringWriter();
        //从vm目录下加载hello.vm模板,在eclipse工程中该vm目录与src目录平级
        velocityEngine.mergeTemplate("vmfile/hello.vm", "UTF-8", context, writer);
        System.out.println(writer.toString());
        
    }
    
}  