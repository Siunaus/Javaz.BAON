/**
 * 
 */
package javaz.baon.manager.http.xml;

import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javaz.utils.CommUtil;
import javaz.utils.xml.XmlUtil;

import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

/**
 * @author Zero
 * @mail baozilaji@126.com
 * xml协议文档
 * Aug 13, 2014
 */
public class ProtocolDoc {
	static Logger logger=Logger.getLogger(ProtocolDoc.class);
	/**
	 * 头协议
	 */
	ProtocolNode head;
	/**
	 * 上行公共部分
	 */
	ProtocolNode upCommon;
	/**
	 * 下行部分
	 */
	ProtocolNode downCommon;
	/**
	 * 所有普通协议
	 */
	List<ProtocolNode> sets=new ArrayList<ProtocolNode>();
	/**
	 * @param head the head to set
	 */
	public void setHead(ProtocolNode head) {
		this.head = head;
	}
	/**
	 * @param upCommon the upCommon to set
	 */
	public void setUpCommon(ProtocolNode upCommon) {
		this.upCommon = upCommon;
	}
	/**
	 * @param downCommon the downCommon to set
	 */
	public void setDownCommon(ProtocolNode downCommon) {
		this.downCommon = downCommon;
	}
	/**
	 * 
	 * @param node
	 */
	public void addProtocol(ProtocolNode node){
		this.sets.add(node);
	}
	/**
	 * 将协议文档保存为pd文件
	 * @param filePath
	 */
	public void parseToPdFile(String filePath){
		FileOutputStream fos=null;
		DataOutputStream dos=null;
		try {
			fos=new FileOutputStream(filePath);
			dos=new DataOutputStream(fos);
			
			if(head==null)head=new ProtocolNode("head","");
			head.toBAONIProtocol().desc2Bytes(dos);
			if(upCommon==null)upCommon=new ProtocolNode("upCommon", "");
			upCommon.toBAONIProtocol().desc2Bytes(dos);
			if(downCommon==null)downCommon=new ProtocolNode("downCommon", "");
			downCommon.toBAONIProtocol().desc2Bytes(dos);
			
			if(CommUtil.isEmpityList(sets)){
				dos.writeShort(0);
			}else{
				short setsSize=(short)sets.size();
				dos.writeShort(setsSize);
				for(int i=0;i<setsSize;i++){
					ProtocolNode node=sets.get(i);
					int tt=node.downCmd?1:0;
					int temp = (node.id|(tt<<15));
					dos.writeShort(temp);
					
					node.toBAONIProtocol().desc2Bytes(dos);
				}
			}
		} catch (Exception e) {
			logger.error("parse protocoldoc to pd file error.", e);
		}finally{
			if(fos!=null){
				try {
					fos.close();
				} catch (IOException e) {
					logger.error("close data input stream error.", e);
				}
			}
			if(dos!=null){
				try {
					dos.close();
				} catch (Exception e) {
					logger.error("close file input stream error.", e);
				}
			}
		}
	}
	/**
	 * 保存xml文件
	 * @param filePath
	 */
	public void saveToXmlFile(String filePath){
		Document doc=DocumentHelper.createDocument();
		Element root=doc.addElement("protocols");
		Element headElement=root.addElement("head");
		saveNode(headElement, head,true);
		Element upcommonElement=root.addElement("upcommon");
		saveNode(upcommonElement, upCommon,true);
		Element downcommonElement=root.addElement("downcommon");
		saveNode(downcommonElement, downCommon,true);
		
		Element setsElement=root.addElement("sets");
		if(!CommUtil.isEmpityList(sets)){
			for(ProtocolNode node:sets){
				Element subNode=setsElement.addElement("protocol");
				saveNode(subNode, node, false);
			}
		}
		
		XmlUtil.saveXmlFile(doc, filePath);
	}
	private void saveNode(Element element,ProtocolNode node,boolean isCommonOrHead){
		element.addAttribute("name", node.name);
		element.addAttribute("desc", node.desc);
		if(!isCommonOrHead){
			element.addAttribute("id", node.id+"");
			element.addAttribute("isdown", Boolean.valueOf(node.downCmd).toString());
		}
		for(FieldNode fieldNode:node.fields){
			Element sub=element.addElement("field");
			sub.addAttribute("type", fieldNode.type.toString());
			sub.addAttribute("name", fieldNode.name);
			sub.addAttribute("desc", fieldNode.desc);
		}
	}
}
