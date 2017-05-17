package com.magnolia.mybatis.generator;

import java.io.File;
import java.io.StringWriter;
import java.util.List;
import java.util.Set;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;

import com.magnolia.base.BaseGenerator;
import com.magnolia.base.IBaseGenerator;
import com.magnolia.common.FileUtils;
import com.magnolia.common.GeneratorConstants;
import com.magnolia.common.VelocityContextUtils;
import com.magnolia.dto.TableInfoDTO;

public class GenerateCommonClasses extends BaseGenerator implements IBaseGenerator{
	
	
	public GenerateCommonClasses(Set<String> packagesSet,List<TableInfoDTO> tableInfoDTOList){
		this.packagesSet = packagesSet;
		this.setTableInfoDTOList(tableInfoDTOList); 
	}

	private Set<String> packagesSet;
	
	public Set<String> getPackagesSet() {
		return packagesSet;
	}
	public void setPackagesSet(Set<String> packagesSet) {
		this.packagesSet = packagesSet;
	}


	@Override
	public void generate() throws Exception {
		generateApplication();
		generateHeaders();
		generateConfigClasses();
		generateHandler();
		
    	if(GeneratorConstants.NEED_SSO){
    		generateController();
        	generateInterceptor();
    	}
		
	}
	
	public void generateApplication() throws Exception{
		File file = new File(GeneratorConstants.BASE_PACKAGE_PATH);
		FileUtils.createDir(file);
		String fileName = null;
		Template template = null;
		StringWriter writer = null; 
		ve = new VelocityEngine(GeneratorConstants.VM_PROPERTIES);
	    //设置参数
	    ve.init();
		
		//Application启动类
		fileName = "Application.java";
		template = ve.getTemplate("vmfile/java/"+fileName+".vm");       
		writer = new StringWriter();
        template.merge(GeneratorConstants.VELOCITY_CONTEXT, writer);
		FileUtils.outputToFile(file.getPath()+"\\", GeneratorConstants.PROJECT_NAME+fileName, writer.toString());	
		
	}
	
	public void generateHeaders() throws Exception{
		File file = new File(GeneratorConstants.BASE_PACKAGE_PATH + "common".replace(".", "\\") + "\\");
		FileUtils.createDir(file);
		
		String fileName = null;
		Template template = null;
		StringWriter writer = null; 
		
		ve = new VelocityEngine(GeneratorConstants.VM_PROPERTIES);
	    //设置参数
	    ve.init();
	    
		fileName = "Headers.java";
		template = ve.getTemplate("vmfile/java/common/"+fileName+".vm");       
		writer = new StringWriter();
        template.merge(GeneratorConstants.VELOCITY_CONTEXT, writer);
		FileUtils.outputToFile(file.getPath()+"\\", GeneratorConstants.PROJECT_NAME+fileName, writer.toString());	
	}
	
	public void generateConfigClasses() throws Exception{
		File file = new File(GeneratorConstants.BASE_PACKAGE_PATH + "common".replace(".", "\\") +"\\config");
    	FileUtils.createDir(file);
    	
		String fileName = null;
		Template template = null;
		StringWriter writer = null; 	
		ve = new VelocityEngine(GeneratorConstants.VM_PROPERTIES);
	    //设置参数
	    ve.init();

		//APIConstants
		fileName = "APIConstants.java";
		template = ve.getTemplate("vmfile/java/common/config/"+fileName+".vm");       
		writer = new StringWriter();
        template.merge(GeneratorConstants.VELOCITY_CONTEXT, writer);
		FileUtils.outputToFile(file.getPath()+"\\", GeneratorConstants.PROJECT_NAME+fileName, writer.toString());	
	
		
		String typeAliasesPackage = "";
		String basePackages = "";
		for(TableInfoDTO tableInfoDTO:getTableInfoDTOList()){
			typeAliasesPackage += GeneratorConstants.BASE_PACKAGE+tableInfoDTO.getSubPkg()+".entity,";
			basePackages += GeneratorConstants.BASE_PACKAGE+tableInfoDTO.getSubPkg()+".dao,";
		}
		typeAliasesPackage = typeAliasesPackage.substring(0, typeAliasesPackage.length() - 1);
		basePackages = basePackages.substring(0, basePackages.length() - 1);
		
		//MyBatis配置类
		VelocityContext _velocityContext = VelocityContextUtils.copy(GeneratorConstants.VELOCITY_CONTEXT);
		fileName = "MyBatisConfig.java";
		template = ve.getTemplate("vmfile/java/common/config/"+fileName+".vm");       
		writer = new StringWriter();
		
		_velocityContext.put("typeAliasesPackage",typeAliasesPackage);
        template.merge(_velocityContext, writer);
		FileUtils.outputToFile(file.getPath()+"\\", fileName, writer.toString());
		
		//MyBatisMapperScanner
		fileName = "MyBatisMapperScannerConfig.java";
		template = ve.getTemplate("vmfile/java/common/config/"+fileName+".vm");       
		writer = new StringWriter();
		
		_velocityContext.put("basePackages",basePackages);
        template.merge(_velocityContext, writer);
		FileUtils.outputToFile(file.getPath()+"\\", fileName, writer.toString());	
	
    	if(GeneratorConstants.NEED_MQ){
    		fileName = "MqConfig.java";
    		template = ve.getTemplate("vmfile/java/common/config/"+fileName+".vm");       
    		writer = new StringWriter();
            template.merge(GeneratorConstants.VELOCITY_CONTEXT, writer);
    		FileUtils.outputToFile(file.getPath()+"\\", fileName, writer.toString());	
    	}
    	
    	if(GeneratorConstants.NEED_SSO){
    		//WebMvc配置类
    		fileName = "WebMvcConfig.java";
    		template = ve.getTemplate("vmfile/java/common/config/"+fileName+".vm");       
    		writer = new StringWriter();
            template.merge(GeneratorConstants.VELOCITY_CONTEXT, writer);
    		FileUtils.outputToFile(file.getPath()+"\\", fileName, writer.toString());	
    	}
	
	}

	public void generateController() throws Exception{
		if(GeneratorConstants.NEED_SSO){
			File file = new File(GeneratorConstants.BASE_PACKAGE_PATH + "common".replace(".", "\\")+"\\config");
	    	FileUtils.createDir(file);
			
			String fileName = null;
			Template template = null;
			StringWriter writer = null; 
			
			ve = new VelocityEngine(GeneratorConstants.VM_PROPERTIES);
		    //设置参数
		    ve.init();
		    
			fileName = "SsoController.java";
			template = ve.getTemplate("vmfile/java/common/controller/"+fileName+".vm");       
			writer = new StringWriter();
	        template.merge(GeneratorConstants.VELOCITY_CONTEXT, writer);
			FileUtils.outputToFile(file.getPath()+"\\", fileName, writer.toString());	
		    
		}

		
	}
	
    private void generateHandler() throws Exception{
		if(GeneratorConstants.NEED_SSO){
			File file = new File(GeneratorConstants.BASE_PACKAGE_PATH + "common".replace(".", "\\")+"\\handler");
	    	FileUtils.createDir(file);
			String fileName = null;
			Template template = null;
			StringWriter writer = null; 
			
			ve = new VelocityEngine(GeneratorConstants.VM_PROPERTIES);
		    //设置参数
		    ve.init();
		    
			fileName = "GlobalExceptionHandler.java";
			template = ve.getTemplate("vmfile/java/common/handler/"+fileName+".vm");       
			writer = new StringWriter();
	        template.merge(GeneratorConstants.VELOCITY_CONTEXT, writer);
			FileUtils.outputToFile(file.getPath()+"\\", fileName, writer.toString());
		    
		}
    }

    private void generateInterceptor() throws Exception{
		if(GeneratorConstants.NEED_SSO){
			File file = new File(GeneratorConstants.BASE_PACKAGE_PATH + "common".replace(".", "\\")+"\\interceptor");
			FileUtils.createDir(file);
			String fileName = null;
			Template template = null;
			StringWriter writer = null; 
			
			ve = new VelocityEngine(GeneratorConstants.VM_PROPERTIES);
		    //设置参数
		    ve.init();
		    
			fileName = "SsoSecurityInterceptor.java";
			template = ve.getTemplate("vmfile/java/common/interceptor/"+fileName+".vm");       
			writer = new StringWriter();
	        template.merge(GeneratorConstants.VELOCITY_CONTEXT, writer);
			FileUtils.outputToFile(file.getPath()+"\\", fileName, writer.toString());
		    
		} 
    }


}
