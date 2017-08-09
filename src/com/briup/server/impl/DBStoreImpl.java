package com.briup.server.impl;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.PreparedStatement;
import java.util.Collection;
import java.util.Iterator;
import java.util.Properties;

import com.briup.common.impl.BackupImpl;
import com.briup.common.impl.LogImpl;
import com.briup.model.BIDR;
import com.briup.server.DBStore;
import com.briup.common.Backup;
import com.briup.common.Configuration;
import com.briup.common.ConfigurationAWare;
import com.briup.common.DBUtils;
import com.briup.common.Log;


/**
 * DBStore提供了入库模块的规范。
 * 该接口的实现类将BIDR集合持久化。
 * @author briup
 * @version 1.0 2010-9-14
 */
public class DBStoreImpl implements DBStore,ConfigurationAWare{
	//LogImpl logimpl = new LogImpl();
	//BackupImpl bi=new BackupImpl();
	private DBUtils DBUtils =new DBUtils();
	private String driver;
	private String url;
	private String user;
	private String password;
	private int batch_size;
	private Log log;
	private Backup backup;
	
	@Override
	public void init(Properties properties) {
		// TODO Auto-generated method stub
		driver = properties.getProperty("driver");
		url=properties.getProperty("url");
		user=properties.getProperty("user");
		password=properties.getProperty("password");
		batch_size=Integer.parseInt(properties.getProperty("batch-size"));
	}
	
	/**
	 * 将BIDR集合进行持久化 。
	 * @param collection 需要储存的BIDR集合
	 */
	@Override
	public void saveToDB(Collection<BIDR> collection) throws Exception {
		log.getServerLogger().info("持久化开始入库");
		//一个PreparedStatement 只能处理同一中类型的sql语句（ps insert语句除（）里的可以不一样的意外，其他必须都一样，才可以执行同一个PreparedStatement）
	//	String url="jdbc:oracle:thin:@127.0.0.1:1521:XE";
	//	String user="briup";
	//	String password ="briup";
		Connection connection=DBUtils.getConnection(driver,url, user, password);
		log.getServerLogger().info("用户："+user);
		connection.setAutoCommit(false);//设置手动提交,出现异常时，回滚一堆的sql语句保存到rollback文件中
		PreparedStatement ps=null;
		int day=0;//记录时间
		int num=0;//记录已经执行了多少sql语句
		int batchSize=1000;//执行1000条语句就executeBatch一次
		Collection<BIDR> bidrs=(Collection<BIDR>) backup.load("rollback", backup.LOAD_REMOVE);//读取回滚文件，获取之前发生异常信息
		if(bidrs!=null)
			collection.addAll(bidrs);
		try{
			for(BIDR bidr:collection){
				num++;
				if(day!=bidr.getLogoutDate().getDate()){
					//放置在下线时间对应的表中
					day = bidr.getLogoutDate().getDate();
					if (ps != null) {
						ps.executeBatch();
						ps.close();
					}
					String sql="insert into T_DETAIL_"+day+" "
					+ "values(?,?,?,?,?,?)";
					System.out.println(sql);
					ps=connection.prepareStatement(sql);
					log.getServerLogger().info(sql);
				}
			ps.setString(1, bidr.getAaaLoginName());
			ps.setString(2, bidr.getLoginIp());
			ps.setDate(3, (new java.sql.Date (bidr.getLoginDate().getTime())));
			ps.setDate(4, (new java.sql.Date(bidr.getLogoutDate().getTime())));
			ps.setString(5, bidr.getNasIp());
			ps.setLong(6, bidr.getTimeDuration());
			ps.addBatch();
			
			if (num%batchSize == 0 ||
					num == collection.size()) {
				ps.executeBatch();
			}
		}
			connection.commit();
			System.out.println("传输完成");
			System.out.println(num);
			log.getServerLogger().info("结束入库");
		}catch(Exception e){
			e.printStackTrace();
			connection.rollback();
			backup.store("rollback", collection, backup.STORE_OVERRIED);
		}
	}

	@Override
	public void setConfiguration(Configuration configuration) {
		// TODO Auto-generated method stub
		log=configuration.getLogger();
		backup=configuration.getBackup();
	}

}
