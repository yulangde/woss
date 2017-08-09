package com.briup.test;

import java.util.List;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.junit.Test;

public class Dom4JTest {
	@Test
	public void dom4j() throws DocumentException{
		//1.构建解析器
		SAXReader reader=new SAXReader();
		//2.开始解析
		Document document=reader.read("src/config.xml");
		//3.获取根标签
		Element root=document.getRootElement();
		//打印根标签
		System.out.println("根元素："+root.getName());
		//获取根标签下的所有子标签
		List <Element> elements=root.elements();
		for(Element element:elements){
			System.out.println("根标签下的子元素："+element.getName());
			List<Attribute> list=element.attributes();
			for(Attribute attribute:list){
				System.out.println(attribute.getName()+"="+attribute.getValue());
			}
			List<Element> clildElements=element.elements();
			for(Element element2:clildElements){
				System.out.println(element2.getName()+";"+element2.getTextTrim());
			}
		}
		
		
	}
}
