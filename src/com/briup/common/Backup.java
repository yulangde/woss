package com.briup.common;


import java.util.Properties;

/**
 * 备份类，备份到src下
 * @author Administrator
 */
public interface Backup extends WossModule{
	/**
	 * 备份文件时，在后面追加
	 */
	public static final boolean STORE_APPEN=true;
	/**
	 * 备份文件时，覆盖
	 */
	public static final boolean STORE_OVERRIED=false;
	/**
	 * 读取备份文件，将备份文件删除
	 */
	public static final boolean LOAD_REMOVE =true;
	/**
	 * 读取备份文件，将备份文件不删除
	 */
	public static final boolean LOAD_UNREMOVE =false;
	
	/**
	 * 备份文件
	 * @param fileName
	 * @param object
	 * @param flag
	 * @throws Exception
	 */
	public void store(String fileName,Object object,boolean flag)throws Exception;
	/**
	 * 读取文件
	 * @param fileName
	 * @param flag
	 * @return
	 * @throws Exception
	 */
	public Object load(String fileName,boolean flag)throws Exception;
}
