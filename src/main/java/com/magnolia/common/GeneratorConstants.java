package com.magnolia.common;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Properties;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;
import java.util.Set;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;

import com.magnolia.exception.GeneratorException;


public class GeneratorConstants {
	
	//vm模板所在目录
	public static String VM_BASE_PATH = "/vmfile";

	//初始化vm的设置参数
	public static Properties VM_PROPERTIES = new Properties();
	static {
		VM_PROPERTIES.setProperty("file.resource.loader.class","org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
		VM_PROPERTIES.setProperty(Velocity.ENCODING_DEFAULT, "UTF-8");
		VM_PROPERTIES.setProperty(Velocity.INPUT_ENCODING, "UTF-8");
		VM_PROPERTIES.setProperty(Velocity.OUTPUT_ENCODING, "UTF-8");
	}
	
	//初始化vm的Context参数
	public static VelocityContext VELOCITY_CONTEXT = new VelocityContext();
	
	public static ResourceBundle generatorRb;
	static{
		try{
			String generatorPropertiesPath = Class.class.getClass().getResource("/").getPath() +"/generator.properties";
			generatorRb = new PropertyResourceBundle(new BufferedInputStream(new FileInputStream(generatorPropertiesPath)));	
			for(String key:generatorRb.keySet()){  //velocity中.不能用于变量中，它代表的是对象所属关系
				VELOCITY_CONTEXT.put(key.replace(".", "_"), generatorRb.getString(key));
			}
		}catch(Exception e){
			throw new GeneratorException(e);
		}
	}
	
	
	/**公司域名（反）*/
	public static final String COMPANY_DOMAIN = generatorRb.getString("company.domain");
	
	public static final String PROJECT_BASEPKG = generatorRb.getString("project.basepkg");
	/**项目名*/
	public static final String PROJECT_NAME = generatorRb.getString("project.name");
	/**项目名（全小写）*/
	public static final String PROJECT_NAME_VAR = generatorRb.getString("project.name").toLowerCase();
	/**项目base pkg路径*/
	public static final String BASE_PACKAGE = COMPANY_DOMAIN + "."+PROJECT_BASEPKG;
	/**项目描述*/
	public static final String PROJECT_DESC = generatorRb.getString("project.desc");
	/**项目数据库名*/
	public static final String PROJECT_DB_NAME = generatorRb.getString("project.db.name");
	/**项目应用完整名*/
	public static final String PROJECT_APPLICATION_NAME = generatorRb.getString("project.application.name");
	/**maven groupId*/
	public static final String PROJECT_GROUPID = generatorRb.getString("project.groupId");
	/**maven artifactId*/
	public static final String PROJECT_ARTIFACTID  = generatorRb.getString("project.artifactId");
	/**项目版本*/
	public static final String PROJECT_VERSION = generatorRb.getString("project.version");
	/**java版本*/
	public static final String JAVA_VERSION = generatorRb.getString("java.version");
	/**指定启用主函数类*/
	public static final String APPLICATION_MAINCLASS = generatorRb.getString("application.mainClass");
	/**Application启动端口*/
	public static final String APPLICATION_SERVER_PORT = generatorRb.getString("application.server.port");
	/**要读取的表以及表对应的java程序子package*/
	public static final String SELECTED_TABLES_WITH_SUB_PKG = generatorRb.getString("generator.selectedTablesWithSubPkg"); 

	/**存放生成文件的路径*/
	public static final String FILE_BASE_PATH = generatorRb.getString("generator.file.basePath"); 
	/**数据库中codetable表名*/
	public static final String PROJECT_DB_CODE_TABLE = generatorRb.getString("project.db.codetable");
	/**是否用到MQ*/
	public static final boolean NEED_MQ = Boolean.valueOf(generatorRb.getString("generator.project.application.needMQ").toLowerCase());
	/**是否需要统一权限平台sso登录*/
	public static final boolean NEED_SSO = Boolean.valueOf(generatorRb.getString("generator.project.application.needSSO").toLowerCase());

	/**java src base path*/
	public static final String BASE_PACKAGE_PATH = FILE_BASE_PATH + "java\\" + (BASE_PACKAGE).replace(".", "\\") + "\\";
	/**config pkg path*/
	public static final String CONFIG_FILES_FULL_PATH = FILE_BASE_PATH + "config\\";
	/**resources path*/
	public static final String RESOURCES_FULL_PATH = FILE_BASE_PATH + "main\\resources\\";
	
	
	public static final String CREATOR = "creator";
	public static final String MODIFIER = "modifier";
	public static final String CREATE_TIME = "create_time";
	public static final String MODIFY_TIME = "last_modified_time";
	public static final String MODIFY_DB_TIME = "last_modified_db_time";  //TODO 
	
	public static final String[] CREATE_MODIFIER_COLLUMNS = new String[]{CREATOR,MODIFIER};
	public static final String[] TIME_COLLUMNS = new String[]{CREATE_TIME,MODIFY_TIME};	
	
	public static final Set<String> CREATE_COLLUMN_SET = new HashSet<String>(Arrays.asList(new String[]{CREATOR,CREATE_TIME}));
	public static final Set<String> MODIFY_COLLUMN_SET = new HashSet<String>( Arrays.asList(new String[]{MODIFIER,MODIFY_TIME}));			

	//公共字段
	public static final  Set<String> COMMON_COLLUMN_SET = new HashSet<String>();

	static {		
		COMMON_COLLUMN_SET.addAll(CREATE_COLLUMN_SET);
		COMMON_COLLUMN_SET.addAll(MODIFY_COLLUMN_SET);
	}

	public static final Set<String> createTimeCollumnSet = new HashSet<String>(Arrays.asList(new String[]{CREATE_TIME}));	
	//表里最后一个字段
	public static final Set<String> modifitimeCollumnSet = new HashSet<String>(Arrays.asList(new String[]{MODIFY_TIME}));	
	
	//时间字段
	public static final Set<String> timeCollumnSet = new HashSet<String>();		
	static{
		timeCollumnSet.addAll(createTimeCollumnSet);
		timeCollumnSet.addAll(modifitimeCollumnSet);
	}
	
	public static final String CODE_COLLUMN_END = "Code";
	public static final String DESC_COLLUMN_END = "Desc";
	
	//获取当前时间的函数
	public static final String  GET_CURRENT_TIME =  "now()";
	public static final String  IS_AUTOINCREMENT = "YES";
	public static final String  IS_NOT_AUTOINCREMENT = "NO";
	
//    static public String firstLetterCaps ( String data )
//    {
//        String firstLetter = data.substring(0,1).toUpperCase();
//        String restLetters = data.substring(1).toLowerCase();
//        return firstLetter + restLetters;
//    }
//    static public String removeUnderScores (String data)
//    {
//        String temp = null;
//        StringBuffer out = new StringBuffer();
//        temp = data;
//
//        StringTokenizer st = new StringTokenizer(temp, "_");
//
//        while (st.hasMoreTokens())
//        {
//            String element = (String) st.nextElement();
//            out.append ( firstLetterCaps(element));
//        }
//
//        return out.toString();
//    }
//	
//	public static void main(String[] args) {
//		System.out.println(removeUnderScores("last_modified_db_time"));
//	}
	
}
