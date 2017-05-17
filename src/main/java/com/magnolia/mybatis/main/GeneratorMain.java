package com.magnolia.mybatis.main;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.sql.DatabaseMetaData;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;

import com.magnolia.common.DBUtils;
import com.magnolia.common.FileUtils;
import com.magnolia.common.GeneratorConstants;
import com.magnolia.dto.TableInfoDTO;
import com.magnolia.exception.GeneratorException;
import com.magnolia.mybatis.generator.GenerateBaseClasses;
import com.magnolia.mybatis.generator.GenerateCommonClasses;
import com.magnolia.mybatis.generator.GenerateCommonFiles;
import com.magnolia.mybatis.generator.GenerateController;
import com.magnolia.mybatis.generator.GenerateDTO;
import com.magnolia.mybatis.generator.GenerateEntity;
import com.magnolia.mybatis.generator.GenerateMybatisMapper;
import com.magnolia.mybatis.generator.GenerateMybatisXml;
import com.magnolia.mybatis.generator.GenerateRequestAndResponse;
import com.magnolia.mybatis.generator.GenerateService;
import com.magnolia.mybatis.generator.GenerateServiceImpl;


/**
 * 本程序可以根据数据库表和字段，生成基于SpringMVC架构的项目基础代码 ；<br/>
 * 包括controller(包含request和response包装类)\service\entity\dto\mybatis mapper\mybatis mapper xml等；<br/>
 * mybatis支持分页查询，分页采用com.github.pagehelper.pagehelper；<br/>
 * 每张表生成如下方法：单个新增、批量新增、单个删除、批量删除、单个修改、单个查询、批量查询、分页查询；<br/>
 * 数据库表命名方式建议为:t_${projectName}_${wordA}_${wordB};<br/>
 * 数据库表物理主键明明方式建议为:表名_id,否则程序可能会不识别<br/>
 * 数据库表其他字段也简单按照下划线来分割字符窜，系统生成的java属性会是驼峰格式的<br/>
 * 数据库表里所有的编码字段建议以code结尾，程序会自动在dto中生成其中文描述字段，比如wordA_code生产的java属性分别为wordACode和wordADesc<br/>
 * 数据库里应该有一张code_table表，命名为t_codetable;<br/>
 * API设计符合RESTFULL要求；<br/>
 * 支持swagger展示API；<br/>
 */

public class GeneratorMain {

	public static void main(String[] args) {
		try {
			String dbPropertiesPath = Class.class.getClass().getResource("/").getPath() +"/db.properties";
			ResourceBundle dbRb = new PropertyResourceBundle(new BufferedInputStream(new FileInputStream(dbPropertiesPath)));
			Properties props = new Properties();
			props.setProperty("user", dbRb.getString("mysql.user"));
			props.setProperty("password", dbRb.getString("mysql.password"));
			props.setProperty("remarks", dbRb.getString("mysql.remarks"));
			props.setProperty("useInformationSchema", dbRb.getString("mysql.useInformationSchema"));
			DatabaseMetaData dbMetaData = DBUtils.getDatabaseMetaData("MYSQL",props,dbRb.getString("mysql.url"),dbRb.getString("mysql.driverClassName"));
			
			List<TableInfoDTO> tableInfoDTOList = new ArrayList<TableInfoDTO>();
			TableInfoDTO tableInfoDTO = null;

			String[] selectedTablesWithSubPkgArray = GeneratorConstants.SELECTED_TABLES_WITH_SUB_PKG.trim().split(";");
			String[] selectedTablesWithSubPkgSingleArray = null;
			String[] needsArray = null;
			for (int i = 0; i < selectedTablesWithSubPkgArray.length; i++) {
				selectedTablesWithSubPkgSingleArray = selectedTablesWithSubPkgArray[i].split(",");
				if(selectedTablesWithSubPkgSingleArray.length == 2){
					tableInfoDTO = new TableInfoDTO(selectedTablesWithSubPkgSingleArray[0],selectedTablesWithSubPkgSingleArray[1]);						
				}else if(selectedTablesWithSubPkgSingleArray.length == 3){
					if(StringUtils.isNotBlank(selectedTablesWithSubPkgSingleArray[2])){
						needsArray = selectedTablesWithSubPkgSingleArray[2].split("\\|");
						tableInfoDTO = new TableInfoDTO(selectedTablesWithSubPkgSingleArray[0],selectedTablesWithSubPkgSingleArray[1],needsArray);								
					}else{
						tableInfoDTO = new TableInfoDTO(selectedTablesWithSubPkgSingleArray[0],selectedTablesWithSubPkgSingleArray[1]);
					}
				}else{
					throw new GeneratorException("使用generator.selectedTablesWithSubPkg的格式不正确，请检查");
				}
				tableInfoDTOList.add(tableInfoDTO);
			}
			
			Set<String> packagesSet = new HashSet<String>();
			for (TableInfoDTO _tableInfoDTO : tableInfoDTOList) {
				System.out.println(_tableInfoDTO.toString());
				packagesSet.add(GeneratorConstants.BASE_PACKAGE + "." +_tableInfoDTO.getSubPkg());
			}
			
			FileUtils.deleteDir(new File(GeneratorConstants.FILE_BASE_PATH));  //删除目录以及所有子目录 
			FileUtils.createDir(new File(GeneratorConstants.FILE_BASE_PATH)); //创建目录
			String[] subDirs = new String[]{"java\\"+GeneratorConstants.BASE_PACKAGE.replace(".", "\\"),"main\\resources"};
			for (String subDir : subDirs) {  //创建java目录和配置文件目录
				FileUtils.createDir(GeneratorConstants.FILE_BASE_PATH,subDir);
			}

//			FileUtils.createDir(GeneratorConstants.FILE_BASE_PATH,GeneratorConstants.PROJECT_APPLICATION_NAME);
			
			GenerateEntity generateEntity = new GenerateEntity(dbMetaData,tableInfoDTOList);
			generateEntity.generate();		
			
			GenerateDTO generateDTO = new GenerateDTO(dbMetaData,tableInfoDTOList);
			generateDTO.generate();
			
			GenerateRequestAndResponse generateDTOAndRequestAndResponse = new GenerateRequestAndResponse(dbMetaData,tableInfoDTOList);
			generateDTOAndRequestAndResponse.generate();
			
	        GenerateMybatisMapper generateMybatisMapper = new GenerateMybatisMapper(dbMetaData,tableInfoDTOList);  
	        generateMybatisMapper.generate();  		

	        GenerateMybatisXml generateMybatisXml = new GenerateMybatisXml(dbMetaData,tableInfoDTOList);  
	        generateMybatisXml.generate();     
	        
	        GenerateService generateService = new GenerateService(dbMetaData,tableInfoDTOList);  
	        generateService.generate();   
	        //要在service后面生成
	        GenerateServiceImpl generateServiceImpl = new GenerateServiceImpl(dbMetaData,tableInfoDTOList);  
	        generateServiceImpl.generate();  

	        GenerateBaseClasses generateBaseClasses = new GenerateBaseClasses();
	        generateBaseClasses.generate();
	        
	        GenerateController generateController = new GenerateController(dbMetaData,tableInfoDTOList);  
	        generateController.generate(); 
	        
	        GenerateCommonFiles generateCommonFiles = new GenerateCommonFiles();
	        generateCommonFiles.generate();
	        
	        GenerateCommonClasses generateCommonClasses = new GenerateCommonClasses(packagesSet,tableInfoDTOList);
	        generateCommonClasses.generate();

		} catch (Exception e) {
			e.printStackTrace();
		}
        
	}

}
