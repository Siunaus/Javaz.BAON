/**
 * 
 */
package javaz.baon.manager.http;

import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import javaz.baon.beans.BAONArray;
import javaz.baon.beans.BAONByte;
import javaz.baon.beans.BAONFloat;
import javaz.baon.beans.BAONIArray;
import javaz.baon.beans.BAONIFiled;
import javaz.baon.beans.BAONIProtocol;
import javaz.baon.beans.BAONInt;
import javaz.baon.beans.BAONLong;
import javaz.baon.beans.BAONProtocol;
import javaz.baon.beans.BAONShort;
import javaz.baon.beans.BAONString;
import javaz.baon.consts.GlobalConfig;
import javaz.baon.enums.DataTypes;
import javaz.baon.manager.http.xml.FieldNode;
import javaz.baon.manager.http.xml.ProtocolDoc;
import javaz.baon.manager.http.xml.ProtocolNode;
import javaz.baon.protocol.http.HttpProtocol;
import javaz.utils.PathUtil;
import javaz.utils.properties.DefaultProperties;
import javaz.utils.string.StringUtil;
import javaz.utils.xml.XmlUtil;

import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.Element;

/**
 * @author Zero
 * @mail baozilaji@126.com
 * 负责协议文件的解析以及协议配置的维护
 * Aug 11, 2014
 */
public class HttpProtocolManager {
	static Logger logger=Logger.getLogger(HttpProtocolManager.class);
	private static HttpProtocolManager instance=new HttpProtocolManager();
	private HttpProtocolManager(){
		load();
	}
	public static HttpProtocolManager getIntance(){
		return instance;
	}
	/**
	 * 所有的http协议
	 */
	Map<Short, HttpProtocol> allHttpProtocol=new HashMap<Short, HttpProtocol>();
	private static final String KEY_HTTP_PROTOCOL_FILE="http_protocol_file";
	/**
	 * 加载协议
	 */
	private void load(){
		String protocolFileName=DefaultProperties.getInstance().getProperty(KEY_HTTP_PROTOCOL_FILE, "protocols.xml");
		if(protocolFileName.endsWith(".xml")){
			loadFromXmlToMemery(protocolFileName);
			logger.debug(StringUtil.format("load xml protocol file {0} finished.", protocolFileName));
		}else if(protocolFileName.endsWith(".pd")){
			String protocolFilePath=StringUtil.format("{0}/{1}", PathUtil.getRootClassPath(),DefaultProperties.getInstance().getProperty(KEY_HTTP_PROTOCOL_FILE, "protocols.xml"));
			loadFromPdToMemery(protocolFilePath);
			logger.debug(StringUtil.format("load pd protocol file {0} finished.", protocolFilePath));
		}else{
			logger.error(StringUtil.format("not supported file:{0}", protocolFileName));
		}
	}
	/**
	 * 从xml文件加载协议文档生成ProtocolDoc对象
	 * @param xmlFilePath
	 */
	public ProtocolDoc loadFromXml(String xmlFilePath){
		Document doc=XmlUtil.parseXml(xmlFilePath);
		if(doc==null){
			logger.error(StringUtil.format("can find protocol file:{0}", xmlFilePath));
			return null;
		}
		ProtocolDoc rt=new ProtocolDoc();
		Element root = doc.getRootElement();
		ProtocolNode head=getNode(root, "head");
		rt.setHead(head);
		ProtocolNode upCommonNode=getNode(root, "upcommon");
		rt.setUpCommon(upCommonNode);
		ProtocolNode downCommonNode=getNode(root, "downcommon");
		rt.setDownCommon(downCommonNode);
		Element sets=root.element("sets");
		for(@SuppressWarnings("unchecked")
		Iterator<Element> it=sets.elements().iterator();it.hasNext();){
			Element element=it.next();
			ProtocolNode protocol=getNode(element);
			boolean isDown="true".equalsIgnoreCase(XmlUtil.getAttribute(element, "isdown", "false"));
			protocol.setId(XmlUtil.getShort(element, "id", (short)-1));
			protocol.setDownCmd(isDown);
			rt.addProtocol(protocol);
		}
		return rt;
	}
	private ProtocolNode getNode(Element element,String nodeName){
		Element head = element.element(nodeName);
		return getNode(head);
	}
	private ProtocolNode getNode(Element element){
		ProtocolNode node=new ProtocolNode(XmlUtil.getAttribute(element, "name", ""), XmlUtil.getAttribute(element, "desc", ""));
		parseProtocolNode(node, element);
		return node;
	}
	@SuppressWarnings("unchecked")
	private void parseProtocolNode(ProtocolNode node,Element element){
		List<Element> subList=element.elements("field");
		for(int i=0,j=subList.size();i<j;i++){
			Element subElement=subList.get(i);
			String fieldName=XmlUtil.getAttribute(subElement, "name", "");
			DataTypes fieldType=DataTypes.valueOf(XmlUtil.getAttribute(subElement, "type", ""));
			String fieldDesc=XmlUtil.getAttribute(subElement, "desc", "");
			FieldNode field=new FieldNode(fieldName, fieldType, fieldDesc);
			node.addField(field);
		}
	}
	/**
	 * 从pd文件里加载协议
	 * @param pdFilePath
	 */
	private void loadFromPdToMemery(String pdFilePath){
		FileInputStream fis=null;
		DataInputStream dis=null;
		try {
			fis=new FileInputStream(pdFilePath);
			dis=new DataInputStream(fis);
			
			//头
			BAONIProtocol head=new BAONProtocol();
			parseNode(dis, head);
			
			//上行公共部分
			BAONIProtocol upCommon=new BAONProtocol();
			parseNode(dis, upCommon);
			
			//下行公共部分
			BAONIProtocol downCommon=new BAONProtocol();
			parseNode(dis, downCommon);
			
			//协议个数
			short cmdCnt=dis.readShort();
			for(int i=0;i<cmdCnt;i++){
				short temp = dis.readShort();
				short cmdId = (short)(temp&0x7fff);
				boolean isDown = (temp>>15)==1?true:false;
				BAONIProtocol protocol=new BAONProtocol();
				logger.debug(StringUtil.format("parse cmd {0}", cmdId));
				parseNode(dis, protocol);
				//TODO head upcommon downcommon 都没有clone
				HttpProtocol httpProtocol=new HttpProtocol(GlobalConfig.MIGIC_NUMBER, cmdId, head, isDown?downCommon:upCommon,protocol);
				allHttpProtocol.put(cmdId, httpProtocol);
			}
		} catch (Exception e) {
			logger.error("协议解析失败", e);
		}finally{
			if(fis!=null){
				try {
					fis.close();
				} catch (IOException e) {
					logger.warn("关闭流失败", e);
				}
			}
			if(dis!=null){
				try {
					dis.close();
				} catch (IOException e) {
					logger.warn("关闭流失败", e);
				}
			}
		}
	}
	private void parseNode(DataInputStream dis,BAONIProtocol protocol)throws IOException{
		boolean isInLoop=false;
		Stack<ParseSence> stack=new Stack<ParseSence>();
		byte dNameLength=0;
		while(true){
			byte b=dis.readByte();
			//first 4 bits is data type
			byte dType=(byte)((b>>4)&0xF);
			//last 4 bits is data name length
			dNameLength=(byte)(b&0xF);
			if(dType==DataTypes.LOOPSTART.ordinal()){// start a loop
				logger.info("start a loop.");
				//save the sence
				String fieldName=parseDataName(dis,dNameLength);
				BAONIArray field=new BAONArray(fieldName);
				stack.push(new ParseSence(field, isInLoop));
				isInLoop=true;
				continue;
			}else if(dType==DataTypes.LOOPEND.ordinal()){// end a loop
				logger.info("end a loop.");
				//recover the sence
				ParseSence sence=stack.pop();
				isInLoop=sence.isInLoop;
				protocol.addField((BAONIFiled)sence.array);
				continue;
			}else if(dType==DataTypes.END.ordinal()){// parse end
				logger.info("parse finished.");
				break;
			}else{
				BAONIFiled field=null;
				if(dType==DataTypes.BYTE.ordinal()){//byte
					logger.info("is byte.");
					field=new BAONByte((byte)0);
				}else if(dType==DataTypes.SHORT.ordinal()){//short
					logger.info("is short.");
					field=new BAONShort((short)0);
				}else if(dType==DataTypes.INT.ordinal()){//int
					logger.info("is int.");
					field=new BAONInt(0);
				}else if(dType==DataTypes.FLOAT.ordinal()){//float
					logger.info("is float.");
					field=new BAONFloat(0f);
				}else if(dType==DataTypes.LONG.ordinal()){
					logger.info("is long");
					field=new BAONLong(0l);
				}else if(dType==DataTypes.STRING.ordinal()){//string
					logger.info("is string.");
					field=new BAONString("");
				}else{//not defined
					logger.info("error data type:"+dType);
				}
				if(field!=null){
					if(dNameLength>0){//data type name's length is great than zero
						field.setFieldName(parseDataName(dis,dNameLength));//why add 1?because of dType takes 1 byte.
					}
					if(isInLoop){
						stack.peek().array.addField(field);
					}else{
						protocol.addField(field);
					}
				}
			}
		}
	}
	/*
	 * convert byte array to string
	 */
	private java.lang.String parseDataName(DataInputStream dis,byte length)throws IOException{
		byte[] name=new byte[length];
		dis.readFully(name);
		StringBuilder sb=new StringBuilder();
		for(int i=0,end=length;i<end;i++){
			sb.append((char)name[i]);
		}
		return sb.toString();
	}
	/**
	 * 直接将xml协议文档加载到内存
	 * @param protocolFileName
	 */
	private void loadFromXmlToMemery(String protocolFileName) {
		Document doc=XmlUtil.parseXmlFromInputStream(PathUtil.getResourceInputStream(protocolFileName));
		if(doc==null){
			logger.error(StringUtil.format("can not find protocol file:{0}", protocolFileName));
			return;
		}
		BAONIProtocol head=new BAONProtocol();
		Element root = doc.getRootElement();
		Element headNode=root.element("head");
		logger.debug("parse head.");
		parseNode(headNode, head);
		BAONIProtocol upCommon=new BAONProtocol();
		Element upCommonNode=root.element("upcommon");
		logger.debug("parse upcommon.");
		parseNode(upCommonNode, upCommon);
		BAONIProtocol downCommon=new BAONProtocol();
		Element downCommonNode=root.element("downcommon");
		logger.debug("parse downcommon.");
		parseNode(downCommonNode, downCommon);
		@SuppressWarnings("unchecked")
		List<Element> sets=root.element("sets").elements();
		for(Element element:sets){
			short id=XmlUtil.getShort(element, "id");
			boolean isDown=Boolean.parseBoolean(XmlUtil.getAttribute(element, "isdown", "false"));
			BAONIProtocol protocol=new BAONProtocol();
			logger.debug(StringUtil.format("parse cmd {0}", id));
			parseNode(element, protocol);
			//TODO head upcommon downcommon 都没有clone
			HttpProtocol httpProtocol=new HttpProtocol(GlobalConfig.MIGIC_NUMBER, id, head, isDown?downCommon:upCommon,protocol);
			allHttpProtocol.put(id, httpProtocol);
		}
	}
	private void parseNode(Element parent,BAONIProtocol protocol){
		boolean isInLoop=false;
		Stack<ParseSence> stack=new Stack<ParseSence>();
		for(@SuppressWarnings("unchecked")
			Iterator<Element> it=parent.elementIterator();it.hasNext();){
			Element element=it.next();
			DataTypes type=DataTypes.valueOf(XmlUtil.getAttribute(element, "type"));
			String name=XmlUtil.getAttribute(element, "name");
			if(type==DataTypes.LOOPSTART){
				logger.debug("start a loop.");
				stack.push(new ParseSence(new BAONArray(name), isInLoop));
				isInLoop=true;
				continue;
			}else if(type==DataTypes.LOOPEND){
				logger.debug("end a loop");
				ParseSence sence=stack.pop();
				isInLoop=sence.isInLoop;
				if(isInLoop){
					stack.peek().array.addField((BAONIFiled)sence.array);
				}else{
					protocol.addField((BAONIFiled)sence.array);
				}
				continue;
			}else if(type==DataTypes.END){
				logger.error("never goes here.");
			}else{
				BAONIFiled recordField=null;
				if(type==DataTypes.BYTE){
					logger.debug("is BYTE");
					recordField=new BAONByte(name,(byte)0);
				}else if(type==DataTypes.SHORT){
					logger.debug("is SHORT");
					recordField=new BAONShort(name,(short)0);
				}else if(type==DataTypes.INT){
					logger.debug("is INT");
					recordField=new BAONInt(name,0);
				}else if(type==DataTypes.FLOAT){
					logger.debug("is FLOAT");
					recordField=new BAONFloat(name,0f);
				}else if(type==DataTypes.LONG){
					logger.debug("is LONG");
					recordField=new BAONLong(name,0l);
				}else if(type==DataTypes.STRING){
					logger.debug("is STRING");
					recordField=new BAONString(name,"");
				}else {
					logger.error(StringUtil.format("error data type:{0}", type));
				}
				if(recordField!=null){
					if(isInLoop){
						stack.peek().array.addField(recordField);
					}else{
						protocol.addField(recordField);
					}
				}
			}
		}
	}
	/**
	 * 获取指定id的协议对象
	 * @param cmdId
	 * @return
	 */
	public HttpProtocol getHttpProtocol(short cmdId){
		return allHttpProtocol.get(cmdId).cloneHttpProtocol();
	}
}
