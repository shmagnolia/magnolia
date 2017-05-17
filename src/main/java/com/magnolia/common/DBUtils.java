package com.magnolia.common;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;


public class DBUtils {

	/**
	 * 获取数据库信息
	 * @param dbType
	 * @param props
	 * @return
	 * @throws Exception
	 */
	public static DatabaseMetaData getDatabaseMetaData(String dbType,Properties props,String url,String driverClassName) throws Exception{
		if("MYSQL".equals(dbType.toUpperCase())){
	        return getMysqlDatabaseMetaData(props,url,driverClassName);		
		}else{
			return null;
		}
	}	
	
	/**
	 * 获取mysql数据库信息
	 * @param props
	 * @return
	 * @throws Exception
	 */
	public static DatabaseMetaData getMysqlDatabaseMetaData(Properties props,String url,String driverClassName) throws Exception{
		Class.forName(driverClassName);
		Connection conn = DriverManager.getConnection(url,props);
		return conn.getMetaData();
	}
	
	/**
	 * 获取数据库所有表名list
	 * @param dbType
	 * @param dbMetaData
	 * @return
	 */
	public static List<String> getTableNameList(String dbType,DatabaseMetaData dbMetaData) {  
		if("MYSQL".equals(dbType.toUpperCase())){
	        return getMysqlTableNameList(dbMetaData);		
		}else{
			return null;
		}

    }  
	
	/**
	 * 获取mysql数据库所有表名list
	 * @param dbMetaData
	 * @return
	 */
	public static List<String> getMysqlTableNameList(DatabaseMetaData dbMetaData) {  
		List<String> tableList = new ArrayList<String>();
        try {  
            String[] types = { "TABLE" };  
            ResultSet rs = dbMetaData.getTables(null, null, "%", types);  
            String tableName = null;
            while (rs.next()) {  
                tableName = rs.getString("TABLE_NAME");  //表名 
                tableList.add(tableName);
            }  
        } catch (SQLException e) {  
            e.printStackTrace();  
        }
        return tableList;
    } 	
	
	
    /** 
     * 数据库类型转为java类型 
     * @param COLUMN_TYPE 
     * @return 
     */  
    public static String getMysqlColumnType(String COLUMN_TYPE) {  
        String columnType = null;
    	if("VARCHAR".equalsIgnoreCase(COLUMN_TYPE)) {  
    		columnType = "String";  
        } else if("BIGINT".equalsIgnoreCase(COLUMN_TYPE)) {  
        	columnType = "BigInteger";  
        } else if("DATETIME".equalsIgnoreCase(COLUMN_TYPE)) {  
        	columnType = "Date";  
        } else if("DATE".equalsIgnoreCase(COLUMN_TYPE)) {  
        	columnType = "Date";  
        } else if("INT".equalsIgnoreCase(COLUMN_TYPE) || "INT UNSIGNED".equalsIgnoreCase(COLUMN_TYPE)) {  
        	columnType = "Integer";  
        } else if("BIGINT UNSIGNED".equalsIgnoreCase(COLUMN_TYPE)) {  
        	columnType = "BigInteger";  
        } else if("TINYINT UNSIGNED".equalsIgnoreCase(COLUMN_TYPE)) {  
        	columnType = "Short";  
        } else if("DECIMAL".equalsIgnoreCase(COLUMN_TYPE) ) {  
        	columnType = "BigDecimal";  
        } else if("FLOAT".equalsIgnoreCase(COLUMN_TYPE) || "DOUBLE".equalsIgnoreCase(COLUMN_TYPE)) {  
        	columnType = "Double";  
        } else if("TEXT".equalsIgnoreCase(COLUMN_TYPE) || "TINYTEXT".equalsIgnoreCase(COLUMN_TYPE) || "MEDIUMTEXT".equalsIgnoreCase(COLUMN_TYPE) || "LONGTEXT".equalsIgnoreCase(COLUMN_TYPE)) {  
        	columnType = "String";  
        } else if("TIMESTAMP".equalsIgnoreCase(COLUMN_TYPE) || "DATE".equalsIgnoreCase(COLUMN_TYPE)  || "TIME".equalsIgnoreCase(COLUMN_TYPE) || "DATETIME".equalsIgnoreCase(COLUMN_TYPE)) {  
        	columnType = "Date";  
        } else if("TINYINT".equalsIgnoreCase(COLUMN_TYPE)) {  
        	columnType = "Short";  
        } else if("DECIMAL UNSIGNED".equalsIgnoreCase(COLUMN_TYPE)) {  
        	columnType = "BigDecimal";  
        } else if("SMALLINT".equalsIgnoreCase(COLUMN_TYPE)) {  
        	columnType = "Short";  
        } else if("BIT".equalsIgnoreCase(COLUMN_TYPE)) {  
        	columnType = "Short";  
        } else if("CHAR".equalsIgnoreCase(COLUMN_TYPE)) {  
        	columnType = "String";  
        } else if("VARBINARY".equalsIgnoreCase(COLUMN_TYPE)) {  
        	columnType = "Byte";  
        } else if("BLOB".equalsIgnoreCase(COLUMN_TYPE) || "TINYBLOB".equalsIgnoreCase(COLUMN_TYPE) || "LONGBLOB".equalsIgnoreCase(COLUMN_TYPE) || "MEDIUMBLOB".equalsIgnoreCase(COLUMN_TYPE)) {  
        	columnType = "byte[]";  
        }else{
        	
        }
        return columnType;  
    }
	
	
}
