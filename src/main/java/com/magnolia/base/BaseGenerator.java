package com.magnolia.base;

import java.sql.DatabaseMetaData;
import java.util.List;

import org.apache.velocity.app.VelocityEngine;

import com.magnolia.dto.TableInfoDTO;


public class BaseGenerator {

	private DatabaseMetaData dbMetaData = null;
	private List<TableInfoDTO> tableInfoDTOList = null;
	
	protected VelocityEngine ve;

	
	public BaseGenerator(){

	}	
	
	public List<TableInfoDTO> getTableInfoDTOList() {
		return tableInfoDTOList;
	}
	public void setTableInfoDTOList(List<TableInfoDTO> tableInfoDTOList) {
		this.tableInfoDTOList = tableInfoDTOList;
	}
	public DatabaseMetaData getDbMetaData() {
		return dbMetaData;
	}
	public void setDbMetaData(DatabaseMetaData dbMetaData) {
		this.dbMetaData = dbMetaData;
	}
	
}
