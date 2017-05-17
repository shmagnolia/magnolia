package com.magnolia.mybatis.generator;

import java.io.File;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import com.magnolia.base.BaseGenerator;
import com.magnolia.base.IBaseGenerator;
import com.magnolia.common.DBUtils;
import com.magnolia.common.FileUtils;
import com.magnolia.common.FormatUtils;
import com.magnolia.common.GeneratorConstants;
import com.magnolia.dto.TableInfoDTO;
  
public class GenerateMybatisXml extends BaseGenerator implements IBaseGenerator{  

	
    public GenerateMybatisXml(DatabaseMetaData dbMetaData,List<TableInfoDTO> tableInfoDTOList) {  
    	this.setDbMetaData(dbMetaData);
    	this.setTableInfoDTOList(tableInfoDTOList); 
    }
	
    /** 
     * @Description: 获取表对应的所有列 
     * @author: ppt 
     * @date: 2015-3-16 上午10:13:17 
     * @param tableInfoDTO.getTableName() 
     * @return: void 
     */  
    public void generate() throws Exception{  
        try {
        	String entity = null;
        	String dto = null;
        	String dtoVar = null;
        	String entityVar = null;
        	String xmlFileName = null;
        	StringBuilder header = null;
        	StringBuilder footer = null;
        	StringBuilder add = null;
        	StringBuilder batchAdd = null;
            String insert = null; 
            String column = null;  
            String values = null;  
            String batchInsert = null;  
            String batchValues = null;  
            StringBuilder delete = null; 
            StringBuilder batchDelete = null;
            StringBuilder update = null;
            String updateConent = null;
            String lastUpdateConent = null;
            StringBuilder select = null;
            StringBuilder selectDto = null;
            StringBuilder dtoResultMap = null;
            String dtoResultMapConent = null;
            StringBuilder selectList = null;	
            StringBuilder pageSelectList = null;
            String columnName = null;
            String descColumnName = null;
            String tableColumnName = null;
            String tableDescColumnName = null;
            String addStatement = null;
            String batchAddstatement = null;
            String tablePrimaryKey = null; //物理主键
            String primaryKeyJava = null; //物理主键对应的java属性
            String primaryKeyJavaType = null; //物理主键对应的java属性的类型
            ResultSet resultSetColumn = null;
            ResultSet _resultSetColumn = null;
            String thisPackage = null;
            String entityPackage = null;
            String dtoPackage = null;
            String path = GeneratorConstants.RESOURCES_FULL_PATH + "mapper\\";
            String selectContent = null;
            String selectDtoContent = null;
            String isAutoIncrement = null;
        	File file = new File(path);
        	FileUtils.createDir(file);
        	int mostLength = 0;
        	String firstColumn = null;
        	String firstColumnJava = null;
        	String firstColumnJavaType = null;
            for(TableInfoDTO tableInfoDTO : getTableInfoDTOList()) { 
            	System.out.println("tableInfoDTO.getTableName():"+tableInfoDTO.getTableName());
            	thisPackage = GeneratorConstants.BASE_PACKAGE+"."+tableInfoDTO.getSubPkg() +".dao";
            	entityPackage = GeneratorConstants.BASE_PACKAGE+"."+tableInfoDTO.getSubPkg() +".entity";
            	dtoPackage = GeneratorConstants.BASE_PACKAGE+"."+tableInfoDTO.getSubPkg() +".dto";
                
                mostLength = 0;
                
                firstColumn = null;
                firstColumnJava = null;
                firstColumnJavaType = null;
                tablePrimaryKey = null;
                primaryKeyJava = null;
                primaryKeyJavaType = null;
                
                _resultSetColumn = getDbMetaData().getColumns(null, null,  tableInfoDTO.getTableName(), null); 
                while (_resultSetColumn.next()) {
    	            if(firstColumnJava == null && _resultSetColumn.isFirst()){
    	            	firstColumn = _resultSetColumn.getString("COLUMN_NAME");
    	            	firstColumnJava =  FormatUtils.getFormatString(firstColumn, false);  
    	            	firstColumnJavaType = DBUtils.getMysqlColumnType(_resultSetColumn.getString("TYPE_NAME"));
    	            }
                    isAutoIncrement = _resultSetColumn.getString("IS_AUTOINCREMENT"); 
                    if(GeneratorConstants.IS_AUTOINCREMENT.equals(isAutoIncrement)){ //根据是否自增来判断是否是物理主键
                    	tablePrimaryKey = _resultSetColumn.getString("COLUMN_NAME");
                    	primaryKeyJava = FormatUtils.getFormatString(tablePrimaryKey, false);  
    	                primaryKeyJavaType = DBUtils.getMysqlColumnType(_resultSetColumn.getString("TYPE_NAME"));
                    }
                }
                if(primaryKeyJava == null || tablePrimaryKey == null){
                	tablePrimaryKey = firstColumn;
                	primaryKeyJava = firstColumnJava;
                	primaryKeyJavaType = firstColumnJavaType;
                }
                
                entity = FormatUtils.getFormatString(tableInfoDTO.getTableName(), true);//表名  
                dto = entity +"DTO";
                entityVar = FormatUtils.getFormatString(tableInfoDTO.getTableName(), false);//表名  
                dtoVar = entityVar +"DTO";
                xmlFileName = entity + "Mapper";//文件名 、接口地址  "I" + 
                header = new StringBuilder("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n");  
                header.append("<!DOCTYPE mapper PUBLIC \"-//mybatis.org//DTD Mapper 3.0//EN\" \"http://mybatis.org/dtd/mybatis-3-mapper.dtd\" >"+ "\n\n");  
                header.append("<mapper namespace=\"");  
                header.append(thisPackage+"."+xmlFileName);  
                header.append("\">\n\n");  
                add = new StringBuilder();  
                //增加  
                add.append("\t<insert id=\"add"+entity+"\" parameterType=\""+entityPackage+"."+entity+"\">\n");  
                insert = "\t\tinsert into " + tableInfoDTO.getTableName() + "(";  
                column = "";  
                values = "";  
                batchAdd = new StringBuilder();  
                //批量增加
                batchAdd.append("\t<insert id=\"batchAdd"+entity+"\" parameterType=\""+"java.util.List"+"\">\n");  
                batchInsert = "\t\tinsert into " + tableInfoDTO.getTableName() + "(";  
                batchValues = "";  
                //删除
                delete = new StringBuilder();  
                delete.append("\t<delete id=\"delete"+entity+"\" parameterType=\""+primaryKeyJavaType+"\">\n");  
                delete.append("\t\tdelete from " + tableInfoDTO.getTableName() + "  where "+tablePrimaryKey+" = #{"+primaryKeyJava+"}\n");  
                delete.append("\t</delete>\n\n");  
                //批量删除  
                batchDelete = new StringBuilder();  
                batchDelete.append("\t<delete id=\"batchDelete"+entity+"\" parameterType=\""+"java.util.List"+"\">\n");  
                batchDelete.append("\t\tdelete from " + tableInfoDTO.getTableName() + "  where "+tablePrimaryKey+" in\n\t\t<foreach collection=\""+primaryKeyJava+"List\" index=\"index\" item=\"item\" open=\"(\" separator=\",\" close=\")\">\n\t\t#{item}\n\t\t</foreach>\n");  
                batchDelete.append("\t</delete>\n\n");  
                //更新  
                update = new StringBuilder();  
                updateConent = "";  
                update.append("\t<update id=\"update"+entity+"\" parameterType=\""+entityPackage+"."+entity+"\">\n");  
                update.append("\t\tupdate " + tableInfoDTO.getTableName() + " set \n");  
                //查询entity
                select = new StringBuilder();  
                select.append("\t<select id=\"get"+entity+"\" parameterType=\""+primaryKeyJavaType+"\" resultType=\""+entityPackage+"."+entity+"\">\n");  
                selectContent = new String();
                selectContent = "\t\tselect\n";  
                //查询list  
                selectList = new StringBuilder();  
                selectList.append("\t<select id=\"get"+entity+"List\" parameterType=\""+entityPackage+"."+entity+"\" resultType=\""+entityPackage+"."+entity+"\">\n");  
                
                dtoResultMap = new StringBuilder();  
                dtoResultMap.append("\t<resultMap id=\""+dtoVar+"\" type=\""+dtoPackage+"."+dto+"\" >\n");
                dtoResultMapConent = "";        
                //查询dto 
                selectDto = new StringBuilder();  
                selectDto.append("\t<select id=\"get"+dto+"\" parameterType=\""+primaryKeyJavaType+"\" resultMap=\""+dtoVar+"\">\n");  
                selectDtoContent = new String();
                selectDtoContent = "\t\tselect\n";  
                
                //分页查询list  
                pageSelectList = new StringBuilder();  
                pageSelectList.append("\t<select id=\"get"+entity+"PageList\" parameterType=\"java.util.Map\" resultMap=\""+dtoVar+"\">\n");  
                
                resultSetColumn = getDbMetaData().getColumns(null, null,  tableInfoDTO.getTableName(), null);  
                while (resultSetColumn.next()) {  
                    columnName = resultSetColumn.getString("COLUMN_NAME");  
                    String columnNameType = DBUtils.getMysqlColumnType(resultSetColumn.getString("TYPE_NAME"));
                	if(columnName.equalsIgnoreCase("last_modified_db_time")){
                		continue;
                	}
                    tableColumnName = columnName;  
                    columnName = FormatUtils.getFormatString(columnName, false);  
                    //增加数据  
                    if( !(tablePrimaryKey.equalsIgnoreCase(tableColumnName) )) {  
                        column += tableColumnName +",";  
                        if(GeneratorConstants.timeCollumnSet.contains(tableColumnName.toLowerCase())){
                            values += GeneratorConstants.GET_CURRENT_TIME + ",";  
                            batchValues += GeneratorConstants.GET_CURRENT_TIME + ","; 
                        }else{ 
                        	if(columnNameType.equalsIgnoreCase("VARCHAR")){
                                values += "IFNULL(#{" + columnName + "},''),";  
                                batchValues += "IFNULL(#{item." + columnName + "},''),";   
                        	}else{
                                values += "#{" + columnName + "},";  
                                batchValues += "#{item." + columnName + "},";   	                     		
                        	}
                        }
                    }  
                    //删除数据  
                    
                    //更新数据 
                    lastUpdateConent = "";
                    if(!GeneratorConstants.CREATE_COLLUMN_SET.contains(tableColumnName.toLowerCase()) 
                    		|| tableColumnName.equalsIgnoreCase(tablePrimaryKey)){  //如果不是“创建”字段或主键字段
                    	if(GeneratorConstants.MODIFY_TIME.equalsIgnoreCase(tableColumnName)){  //最后修改日期
                           lastUpdateConent = "\t\t\t"+tableColumnName  + "="+GeneratorConstants.GET_CURRENT_TIME;     
                    	}else if(GeneratorConstants.MODIFIER.equalsIgnoreCase(tableColumnName)){ //最后修改人
                    		updateConent +=  "\t\t\t"+tableColumnName + "=#{" + columnName +"}," + "\n";  
                    	}else{
                    		updateConent += "\t\t\t<if test=\"" + columnName + "!= null\">" + tableColumnName + "=#{" + columnName +"}," + "</if>\n";     
                    	}
                    }
                    updateConent += lastUpdateConent;
                    //查找数据  
                    selectContent += "\t\t\t"+tableColumnName + getBlank(mostLength,tableColumnName.length())  + "\tAS "+ columnName + ",\n"; 
                    
                    selectDtoContent += "\t\t\tt."+tableColumnName + getBlank(mostLength,tableColumnName.length())  + "\tAS "+ tableColumnName + ",\n";
                    dtoResultMapConent += "\t\t<result column=\""+tableColumnName+"\"  property=\""+FormatUtils.getCollumnFormatString(tableColumnName, false)+"\"/>\n"; 
                    //如果是代码code字段，且不是codetable表，自动增加desc字段的查询
                    if(columnName.toLowerCase().endsWith(GeneratorConstants.CODE_COLLUMN_END.toLowerCase()) &&
                    		!GeneratorConstants.PROJECT_DB_CODE_TABLE.equalsIgnoreCase(tableInfoDTO.getTableName())){
                        descColumnName = columnName.substring(0, columnName.length() - GeneratorConstants.CODE_COLLUMN_END.length()) + GeneratorConstants.DESC_COLLUMN_END;
                        tableDescColumnName = tableColumnName.substring(0, tableColumnName.length() - GeneratorConstants.CODE_COLLUMN_END.length()) + GeneratorConstants.DESC_COLLUMN_END.toLowerCase();
                        
                        selectDtoContent += "\t\t\t(select code_desc from "+GeneratorConstants.PROJECT_DB_CODE_TABLE+" where code_type = '"+columnName+"' and code = t."+tableColumnName+")\tAS "+ tableDescColumnName + ",\n";
                        dtoResultMapConent += "\t\t<result column=\""+tableDescColumnName+"\"  property=\""+descColumnName+"\"/>\n";
                    }
                }  
                
                selectContent = selectContent.substring(0, selectContent.length()-2);  
                selectContent += "\n\t\tfrom " + tableInfoDTO.getTableName() + "\n";  
                selectContent += "\t\twhere "+tablePrimaryKey+" = #{"+primaryKeyJava+"}\n";  
                select.append(selectContent);  
                select.append("\t</select>\n\n");  
                
                selectList.append(selectContent);  
                selectList.append("\t</select>\n\n");  
              
                
                dtoResultMap.append(dtoResultMapConent);
                dtoResultMap.append("\t</resultMap>\n\n");
                
                selectDtoContent = selectDtoContent.substring(0, selectDtoContent.length()-2);  
                selectDtoContent += "\n\t\tfrom " + tableInfoDTO.getTableName() + " t\n";  
 
                pageSelectList.append(selectDtoContent);  
                pageSelectList.append("\t</select>\n\n");  
                
                selectDtoContent += "\t\twhere "+tablePrimaryKey+" = #{"+primaryKeyJava+"}\n";  
                selectDto.append(selectDtoContent);
                selectDto.append("\t</select>\n\n");

                update.append(updateConent);  
                update.append("\n\t\twhere "+tablePrimaryKey+" = #{"+primaryKeyJava+"}\n");  
                update.append("\t</update>\n\n");  
                   
                column = column.substring(0,column.length()-1);  
                values = values.substring(0,values.length()-1);  
                batchValues = batchValues.substring(0,batchValues.length()-1); 
                
                addStatement = insert + column + ") \n\t\tvalues(" + values + ")\n";  
                add.append(addStatement);  
                add.append("\t</insert>\n\n"); 
            
                batchAddstatement = batchInsert + column + ")\n\t\tvalues\n\t\t<foreach collection=\"list\" item=\"item\" index=\"index\" separator=\",\" >\n\t\t(" + batchValues + ")\n";  
                batchAdd.append(batchAddstatement);
                batchAdd.append("\t\t</foreach>\n\t</insert>\n\n");  
                
                if(tableInfoDTO.isNeedAdd()){
                    header.append(add); 
                }
                if(tableInfoDTO.isNeedBatchAdd()){
                    header.append(batchAdd);                  
                }
                if(tableInfoDTO.isNeedDelete()){
                	header.append(delete); 
                }
                if(tableInfoDTO.isNeedBatchDelete()){
                	header.append(batchDelete);
                }
                if(tableInfoDTO.isNeedUpdate()){
                	header.append(update); 
                }
                if(tableInfoDTO.isNeedSelectEntity()){
                	header.append(select); 
                }
                if(tableInfoDTO.isNeedSelectList()){
                    header.append(selectList);                 	
                }
                if(tableInfoDTO.isNeedSelectDto() || tableInfoDTO.isNeedSelectPageList()){
                    header.append(dtoResultMap);                	
                }
                if(tableInfoDTO.isNeedSelectDto()){
                    header.append(selectDto);                	
                }
                if(tableInfoDTO.isNeedSelectPageList()){
                    header.append(pageSelectList);                	
                }

                footer = new StringBuilder("\n</mapper>");  
                header.append(footer);  
                FileUtils.outputToFile(path,xmlFileName+".xml", header.toString());  
        	}
        } catch (SQLException e) {  
            e.printStackTrace();  
        }  
    }  
    
    private String getBlank(int lastLength,int leastlength){
    	String blank = " ";
    	if(lastLength > leastlength){
        	for (int i = 0; i < lastLength - leastlength; i++) {
        		blank += " ";
    		}    		
    	}
    	return blank;
    	
    }
      
}  