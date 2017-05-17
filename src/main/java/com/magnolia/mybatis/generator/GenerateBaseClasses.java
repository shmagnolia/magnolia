package com.magnolia.mybatis.generator;

import java.io.File;
import java.io.StringWriter;

import org.apache.velocity.Template;
import org.apache.velocity.app.VelocityEngine;

import com.magnolia.base.BaseGenerator;
import com.magnolia.base.IBaseGenerator;
import com.magnolia.common.FileUtils;
import com.magnolia.common.GeneratorConstants;

public class GenerateBaseClasses extends BaseGenerator implements IBaseGenerator{  
  
    
    public GenerateBaseClasses() {  
    }  
      
    public void generate() throws Exception {  
        String thisPackage = GeneratorConstants.BASE_PACKAGE + ".base";
        String path = GeneratorConstants.BASE_PACKAGE_PATH + ".base".replace(".", "\\") + "\\";
    	File file = new File(path);
    	FileUtils.createDir(file);  
        generateBaseController(thisPackage,path);
    	generateBaseEntity(thisPackage,path);
    	generateBaseDTO(thisPackage,path);
    	generateBaseRequest(thisPackage,path);
    	generateBaseResponse(thisPackage,path);
    }
  
    private void generateBaseController(String thisPackage,String path) throws Exception{
    	String fullPath = path + "controller\\";
    	File file = new File(fullPath);
    	FileUtils.createDir(file);  
    	
		String fileName = null;
		Template template = null;
		StringWriter writer = null; 
		ve = new VelocityEngine(GeneratorConstants.VM_PROPERTIES);
	    //设置参数
	    ve.init();
		
		fileName = "BaseController.java";
		template = ve.getTemplate("vmfile/java/base/controller/"+fileName+".vm");       
		writer = new StringWriter();
        template.merge(GeneratorConstants.VELOCITY_CONTEXT, writer);

    	FileUtils.outputToFile(fullPath,GeneratorConstants.PROJECT_NAME+fileName,writer.toString());  
    }


    private void generateBaseEntity(String thisPackage,String path) throws Exception{
    	String fullPath = path + "entity\\";
    	File file = new File(fullPath);
    	FileUtils.createDir(file); 
    	
		String fileName = null;
		Template template = null;
		StringWriter writer = null; 
		ve = new VelocityEngine(GeneratorConstants.VM_PROPERTIES);
	    //设置参数
	    ve.init();
    	
		fileName = "BaseEntity.java";
		template = ve.getTemplate("vmfile/java/base/entity/"+fileName+".vm");       
		writer = new StringWriter();
        template.merge(GeneratorConstants.VELOCITY_CONTEXT, writer);
    	FileUtils.outputToFile(fullPath,GeneratorConstants.PROJECT_NAME+fileName,writer.toString());
    }    
    
    private void generateBaseDTO(String thisPackage,String path) throws Exception{
    	String fullPath = path + "dto\\";
    	File file = new File(fullPath);
    	FileUtils.createDir(file);  
    	
		String fileName = null;
		Template template = null;
		StringWriter writer = null; 
		ve = new VelocityEngine(GeneratorConstants.VM_PROPERTIES);
	    //设置参数
	    ve.init();
	    
		fileName = "BaseDTO.java";
		template = ve.getTemplate("vmfile/java/base/dto/"+fileName+".vm");       
		writer = new StringWriter();
        template.merge(GeneratorConstants.VELOCITY_CONTEXT, writer);
    	FileUtils.outputToFile(fullPath,GeneratorConstants.PROJECT_NAME+fileName,writer.toString());

		fileName = "GeneRespDTO.java";
		template = ve.getTemplate("vmfile/java/base/dto/"+fileName+".vm");       
		writer = new StringWriter();
        template.merge(GeneratorConstants.VELOCITY_CONTEXT, writer);
    	FileUtils.outputToFile(fullPath,GeneratorConstants.PROJECT_NAME+fileName,writer.toString());
    	
		fileName = "ReturnCode.java";
		template = ve.getTemplate("vmfile/java/base/dto/"+fileName+".vm");       
		writer = new StringWriter();
        template.merge(GeneratorConstants.VELOCITY_CONTEXT, writer);
    	FileUtils.outputToFile(fullPath,GeneratorConstants.PROJECT_NAME+fileName,writer.toString());
    	
		fileName = "VersionDTO.java";
		template = ve.getTemplate("vmfile/java/base/dto/"+fileName+".vm");       
		writer = new StringWriter();
        template.merge(GeneratorConstants.VELOCITY_CONTEXT, writer);
    	FileUtils.outputToFile(fullPath,GeneratorConstants.PROJECT_NAME+fileName,writer.toString());

    }    
    
    
    private void generateBaseRequest(String thisPackage,String path) throws Exception{
    	String fullPath = path + "dto\\request\\";
    	File file = new File(fullPath);
    	FileUtils.createDir(file);  
    	
		String fileName = null;
		Template template = null;
		StringWriter writer = null; 
		ve = new VelocityEngine(GeneratorConstants.VM_PROPERTIES);
	    //设置参数
	    ve.init();
	    
		fileName = "BaseRequest.java";
		template = ve.getTemplate("vmfile/java/base/dto/request/"+fileName+".vm");       
		writer = new StringWriter();
        template.merge(GeneratorConstants.VELOCITY_CONTEXT, writer);
    	FileUtils.outputToFile(fullPath,GeneratorConstants.PROJECT_NAME+fileName,writer.toString());
   
       
		fileName = "BasePageListRequest.java";
		template = ve.getTemplate("vmfile/java/base/dto/request/"+fileName+".vm");       
		writer = new StringWriter();
        template.merge(GeneratorConstants.VELOCITY_CONTEXT, writer);
        FileUtils.outputToFile(fullPath,GeneratorConstants.PROJECT_NAME+fileName,writer.toString());  

    	
    }
    

    
    private void generateBaseResponse(String thisPackage,String path)  throws Exception{
    	String fullPath = path + "dto\\response\\";
    	File file = new File(fullPath);
    	FileUtils.createDir(file);  
    	
		String fileName = null;
		Template template = null;
		StringWriter writer = null; 
		ve = new VelocityEngine(GeneratorConstants.VM_PROPERTIES);
	    //设置参数
	    ve.init();
	    
		fileName = "BaseResponse.java";
		template = ve.getTemplate("vmfile/java/base/dto/response/"+fileName+".vm");       
		writer = new StringWriter();
        template.merge(GeneratorConstants.VELOCITY_CONTEXT, writer);
    	FileUtils.outputToFile(fullPath,GeneratorConstants.PROJECT_NAME+fileName,writer.toString());
   
       
		fileName = "BasePageListResponse.java";
		template = ve.getTemplate("vmfile/java/base/dto/response/"+fileName+".vm");       
		writer = new StringWriter();
        template.merge(GeneratorConstants.VELOCITY_CONTEXT, writer);
        FileUtils.outputToFile(fullPath,GeneratorConstants.PROJECT_NAME+fileName,writer.toString());  
        
		fileName = "RespCode.java";
		template = ve.getTemplate("vmfile/java/base/dto/response/"+fileName+".vm");       
		writer = new StringWriter();
        template.merge(GeneratorConstants.VELOCITY_CONTEXT, writer);
        FileUtils.outputToFile(fullPath,GeneratorConstants.PROJECT_NAME+fileName,writer.toString());  
    }  
    

}
