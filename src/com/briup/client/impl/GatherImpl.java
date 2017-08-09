package com.briup.client.impl;

import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;
import java.util.TreeMap;

import com.briup.client.Gather;
import com.briup.common.Backup;
import com.briup.common.Configuration;
import com.briup.common.ConfigurationAWare;
import com.briup.common.Log;
import com.briup.model.BIDR;
  
public class GatherImpl implements Gather,ConfigurationAWare{
	private Backup backup; //定义备份
	private Log log;	//定义日志
	private String path; //定义path
	@Override
	public void init(Properties properties) {
		path=properties.getProperty("src-file"); //获取path
		
	}

	@Override
	/*
	 * 分割用户上线，下线时间表，
	 * 获取用户属性信息，
	 * 同时获得完整记录
	 * */
	public Collection<BIDR> gather() throws Exception {
		//1.构建流，读取一行
		//2.分割
		//3.判断 7/8
		//4.返回集合
		log.getClientLogger().info("开始入库："+"采集模块：");
		//显示当前零点时间
		SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd");
		Date dates=new Date();
		String string=format.format(dates);
		string = string +" 00:00:00";
		Timestamp timestamp = Timestamp.valueOf(string);
		//System.out.println(timestamp);
		
		long sk=0;//文件标记位，从此读取
	//	long end=System.currentTimeMillis();
	//	new SimpleDateFormat("yyyymmddhhmmssSSS").format(new Date());
		List<BIDR> list=new ArrayList<BIDR>();
		Map<String,BIDR> map=new TreeMap<String,BIDR>();
		BufferedReader br=new BufferedReader(new FileReader(path));
		String str="";
		if((backup.load("skip",false)!=null)){
			sk=(long)backup.load("skip",backup.LOAD_UNREMOVE);
		}
		if((backup.load("map", false))!=null){
			map.putAll((Map<? extends String, ? extends BIDR>) backup.load("map", backup.LOAD_UNREMOVE));
		}
		br.skip(sk);
		
		while((str=br.readLine()) != null){
			sk+=(str.length()+2);
			String str1[]=str.split("\\|");
			String aln=str1[0];
			String np=str1[1];
			String num=str1[2];
			String uip=str1[4];
			String date=str1[3];
			Date d=new Date(((Long.parseLong(date))*1000));//精确到毫秒
			if(num.equals("7")){
				BIDR bidr=new BIDR();
				bidr.setAaaLoginName(aln);
				bidr.setNasIp(np);
				bidr.setLoginDate(d);
				bidr.setLoginIp(uip);
				map.put(uip,bidr);
			}else if(num.equals("8")){
				BIDR bidr=map.get(uip);
				map.remove(uip);
				bidr.setLogoutDate(d);
				Date in=bidr.getLoginDate();
				Date out=bidr.getLogoutDate();
			//	System.out.println(in+"     "+out);
				bidr.setTimeDuration(getTD(in,out));
				list.add(bidr);
				//if()
			}
		}
		backup.store("skip", sk, backup.STORE_OVERRIED);
	//	bi.store("map", map,bi.STORE_OVERRIED);
		
		Set<Entry<String, BIDR>> s=map.entrySet();
		Iterator ite =s.iterator();
		while (ite.hasNext()){
			Entry e=(Entry) ite.next();
			BIDR bid=new BIDR();
			BIDR bidr=(BIDR) e.getValue();
			if(bidr.getLoginDate().before(timestamp)){
				bid.setAaaLoginName(bidr.getAaaLoginName());
				bid.setLoginDate(bidr.getLoginDate());
				bid.setLogoutDate(timestamp);
				bid.setLoginIp(bidr.getLoginIp());
				bid.setNasIp(bidr.getNasIp());
				bid.setTimeDuration(getTD(bid.getLoginDate(),bid.getLogoutDate()));
			   	list.add(bid);
				bidr.setLoginDate(timestamp);
			//	System.out.println("bid   "+bid.getLoginDate().getDate());
				//System.out.println("bidr   "+bidr.getLoginDate().getDate());
			}
	//		mapnew.put((String) e.getKey(),bidr);
			//System.out.println(bidr.getLoginDate().getDate());
		}
		backup.store("map", map,backup.STORE_OVERRIED);
		System.out.println("完整:"+list.size()+" 不完整:"+map.size());
		log.getClientLogger().info("完整:"+list.size()+" 不完整:"+map.size()+"入库结束");
		br.close();
		return list;
	}
	
	private long getTD(Date in,Date out) {
		Long in1=in.getTime();
		Long out1=out.getTime();
		Long td=out1-in1;
		return td;
	}

	@Override
	public void setConfiguration(Configuration configuration) {
		// TODO Auto-generated method stub
		log=configuration.getLogger();
		backup=configuration.getBackup();
	}


}
