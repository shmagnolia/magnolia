package com.magnolia.dto;

public class TableInfoDTO {


	public TableInfoDTO(String tableName,String subPkg,String[] needFlags){
		this.tableName = tableName;
		this.subPkg = subPkg;
		String[] flag = null;
		for (String needFlag : needFlags) {
			flag = needFlag.split("=");
			this.setNeed(flag[0], Boolean.valueOf(flag[1]));
		}
		
	}
	public TableInfoDTO(String tableName,String subPkg){
		this.tableName = tableName;
		this.subPkg = subPkg;	
		
		this.needAdd = true;
		this.needBatchAdd = true;
		this.needDelete = true;
		this.needBatchDelete = true;
		this.needUpdate = true;
		this.needBatchUpdate = true;
		this.needSelectEntity = true;
		this.needSelectDto = true;
		this.needSelectList = true;
		this.needSelectPageList = true;
	}
	
	private void setNeed(String name,boolean flag){
		if(name.equalsIgnoreCase("needAdd")){
			this.setNeedAdd(flag);
		}else if(name.equalsIgnoreCase("needBatchAdd")){
			this.setNeedBatchAdd(flag);
		}else if(name.equalsIgnoreCase("needDelete")){
			this.setNeedDelete(flag);
		}else if(name.equalsIgnoreCase("needBatchDelete")){
			this.setNeedBatchDelete(flag);
		}else if(name.equalsIgnoreCase("needUpdate")){
			this.setNeedUpdate(flag);
		}else if(name.equalsIgnoreCase("needBatchUpdate")){
			this.setNeedBatchUpdate(flag);
		}else if(name.equalsIgnoreCase("needSelectEntity")){
			this.setNeedSelectEntity(flag);
		}else if(name.equalsIgnoreCase("needSelectDto")){
			this.setNeedSelectDto(flag);
		}else if(name.equalsIgnoreCase("needSelectList")){
			this.setNeedSelectList(flag);
		}else if(name.equalsIgnoreCase("needSelectPageList")){
			this.setNeedSelectPageList(flag);
		}else{
			//do nothing
		}
	}
	
	
	/**
	 * 表名
	 */
	private String tableName;
	/**
	 * 表对应java文件所在的package
	 */
	private String subPkg;
	
	/** 增加 */
	private boolean needAdd = true;
	/** 批量增加*/
	private boolean needBatchAdd = true;
	/** 删除*/
	private boolean needDelete = true;
	/** 批量删除*/
	private boolean needBatchDelete = true;
	/** 修改*/
	private boolean needUpdate = true;
	/** （批量修改）*/
	private boolean needBatchUpdate = true;
	/** 查询entity*/
	private boolean needSelectEntity = true;
	/** 查询dto*/
	private boolean needSelectDto = true;
	/** 查询List*/
	private boolean needSelectList = true;
	/** 分页查询List*/
	private boolean needSelectPageList = true;


	public boolean isNeedAdd() {
		return needAdd;
	}

	public void setNeedAdd(boolean needAdd) {
		this.needAdd = needAdd;
	}

	public boolean isNeedBatchAdd() {
		return needBatchAdd;
	}

	public void setNeedBatchAdd(boolean needBatchAdd) {
		this.needBatchAdd = needBatchAdd;
	}

	public boolean isNeedDelete() {
		return needDelete;
	}

	public void setNeedDelete(boolean needDelete) {
		this.needDelete = needDelete;
	}

	public boolean isNeedBatchDelete() {
		return needBatchDelete;
	}

	public void setNeedBatchDelete(boolean needBatchDelete) {
		this.needBatchDelete = needBatchDelete;
	}

	public boolean isNeedUpdate() {
		return needUpdate;
	}

	public void setNeedUpdate(boolean needUpdate) {
		this.needUpdate = needUpdate;
	}

	public boolean isNeedBatchUpdate() {
		return needBatchUpdate;
	}

	public void setNeedBatchUpdate(boolean needBatchUpdate) {
		this.needBatchUpdate = needBatchUpdate;
	}

	public boolean isNeedSelectEntity() {
		return needSelectEntity;
	}

	public void setNeedSelectEntity(boolean needSelectEntity) {
		this.needSelectEntity = needSelectEntity;
	}

	public boolean isNeedSelectDto() {
		return needSelectDto;
	}

	public void setNeedSelectDto(boolean needSelectDto) {
		this.needSelectDto = needSelectDto;
	}

	public boolean isNeedSelectList() {
		return needSelectList;
	}

	public void setNeedSelectList(boolean needSelectList) {
		this.needSelectList = needSelectList;
	}

	public boolean isNeedSelectPageList() {
		return needSelectPageList;
	}

	public void setNeedSelectPageList(boolean needSelectPageList) {
		this.needSelectPageList = needSelectPageList;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public String getSubPkg() {
		return subPkg;
	}

	public void setSubPkg(String subPkg) {
		this.subPkg = subPkg;
	}
	

	
}
