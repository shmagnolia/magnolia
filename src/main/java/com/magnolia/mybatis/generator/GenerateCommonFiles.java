package com.magnolia.mybatis.generator;

import java.io.File;
import java.io.StringWriter;

import org.apache.velocity.Template;
import org.apache.velocity.app.VelocityEngine;

import com.magnolia.base.BaseGenerator;
import com.magnolia.base.IBaseGenerator;
import com.magnolia.common.FileUtils;
import com.magnolia.common.GeneratorConstants;

public class GenerateCommonFiles extends BaseGenerator implements IBaseGenerator{
	
        
	public GenerateCommonFiles() throws Exception{
		
	}

	
	@Override
	public void generate() throws Exception {
		String fileName = null;
		Template template = null;
		StringWriter writer = null; 
		
		File file = new File(GeneratorConstants.CONFIG_FILES_FULL_PATH);
		if(!file.exists()){
			FileUtils.createDir(file);
		}
		
		ve = new VelocityEngine();
	    //设置参数
	    ve.init(GeneratorConstants.VM_PROPERTIES);
		
		//生成logback.xml
		fileName = "logback.xml";
		template = ve.getTemplate("vmfile/config/"+fileName+".vm","UTF-8");       
		writer = new StringWriter();
		
        template.merge(GeneratorConstants.VELOCITY_CONTEXT, writer);
		FileUtils.outputToFile(file.getPath(), fileName, writer.toString());
		
		//生成配置文件
		fileName = "application.yml";
		template = ve.getTemplate("vmfile/config/"+fileName+".vm","UTF-8");       
        writer = new StringWriter();  
        
        ve.mergeTemplate("vmfile/config/"+fileName+".vm", "utf-8", GeneratorConstants.VELOCITY_CONTEXT, writer);
        
//        template.merge(GeneratorConstants.VELOCITY_CONTEXT, writer);
		FileUtils.outputToFile(GeneratorConstants.CONFIG_FILES_FULL_PATH, fileName, writer.toString());
	
		//TODO 生成远程配置中心配置
		
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		
		ve = new VelocityEngine();
	    //设置参数
	    ve.init(GeneratorConstants.VM_PROPERTIES);

		//生成pom.xml
		fileName = "pom.xml";
		template = ve.getTemplate("vmfile/pom/"+fileName+".vm","UTF-8");       
		writer = new StringWriter();
		ve.mergeTemplate("vmfile/pom/"+fileName+".vm", "utf-8", GeneratorConstants.VELOCITY_CONTEXT, writer);
        template.merge(GeneratorConstants.VELOCITY_CONTEXT, writer);
		FileUtils.outputToFile(GeneratorConstants.FILE_BASE_PATH, fileName, writer.toString());	
		
	}

	
	
}
