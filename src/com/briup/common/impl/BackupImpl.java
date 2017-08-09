package com.briup.common.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Reader;
import java.util.Properties;

import com.briup.common.Backup;
import com.briup.common.Configuration;
import com.briup.common.ConfigurationAWare;
import com.briup.common.Log;

public class BackupImpl implements Backup,ConfigurationAWare{
	//LogImpl logImpl = new LogImpl();//日志文件
	private String parent;
	private Log log;
	@Override
	public void init(Properties properties) {
		// TODO Auto-generated method stub
		parent = (String) properties.get("backups-temp");
	}
	/**
	 * 备份文件,追加/覆盖
	 */
	@Override
	public void store(String fileName, Object object, boolean flag)
			throws Exception {
		log.getClientLogger().info("开始入库："+"文件名："+fileName);
		File file=new File(parent,fileName);
		if(!file.exists())
			file.createNewFile();
		
		if(flag==STORE_APPEN){
			ObjectOutputStream ous=new ObjectOutputStream(new FileOutputStream
(file,true));
			ous.writeObject(object);
			ous.flush();
			ous.close();
		}else if(flag==STORE_OVERRIED){
			ObjectOutputStream ous=new ObjectOutputStream(new FileOutputStream
					(file,true));
			ous.writeObject(object);
			ous.flush();//无则文件中无内容
			ous.close();
		}
		log.getClientLogger().info(fileName+"备份文件");
	}
	/**
	 * 加载备份，删除、不删除文件   ,Object是里面所有的内容
	 */
	@Override
	public Object load(String fileName, boolean flag) throws Exception {
		log.getClientLogger().info("开始入库："+"读取文件名："+fileName);
		File file=new File(parent,fileName);
		if(!file.exists())
			return null;
		
		Object object=null;
		if(flag==LOAD_REMOVE){
			ObjectInputStream ois=new ObjectInputStream(new FileInputStream(file));
			object=ois.readObject();
			file.delete();
			ois.close();
		}else if(flag==LOAD_UNREMOVE){
			ObjectInputStream ois=new ObjectInputStream(new FileInputStream(file));
			object=ois.readObject();
			ois.close();
		}
	//	logImpl.getClientLogger().info(object);
		log.getClientLogger().info(fileName+"备份文件");
		return object;
	}
	@Override
	public void setConfiguration(Configuration configuration) {
		// TODO Auto-generated method stub
		log= configuration.getLogger();
	}
	

}
