/**
 * 
 */
package javaz.baon.manager.http.xml;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import org.apache.log4j.Logger;

import javaz.baon.beans.BAONArray;
import javaz.baon.beans.BAONByte;
import javaz.baon.beans.BAONFloat;
import javaz.baon.beans.BAONIFiled;
import javaz.baon.beans.BAONIProtocol;
import javaz.baon.beans.BAONInt;
import javaz.baon.beans.BAONLong;
import javaz.baon.beans.BAONProtocol;
import javaz.baon.beans.BAONShort;
import javaz.baon.beans.BAONString;
import javaz.baon.enums.DataTypes;
import javaz.baon.manager.http.ParseSence;

/**
 * @author Zero
 * @mail baozilaji@126.com
 * xml文档的protocol节点
 * Aug 13, 2014
 */
public class ProtocolNode {
	static Logger logger=Logger.getLogger(ProtocolNode.class);
	/**
	 * 协议id
	 */
	short id;
	/**
	 * 协议名称
	 */
	String name="";
	/**
	 * 协议描述
	 */
	String desc="";
	/**
	 * 是否下行协议
	 */
	boolean downCmd;
	/**
	 * 所有协议域节点
	 */
	List<FieldNode> fields=new ArrayList<FieldNode>();
	public ProtocolNode(String name,String desc){
		this.name=name;
		this.desc=desc;
	}
	/**
	 * 添加协议域
	 * @param fieldNode
	 */
	public void addField(FieldNode fieldNode){
		this.fields.add(fieldNode);
	}
	/**
	 * @param id the id to set
	 */
	public void setId(short id) {
		this.id = id;
	}
	/**
	 * @param downCmd the downCmd to set
	 */
	public void setDownCmd(boolean downCmd) {
		this.downCmd = downCmd;
	}
	/**
	 * xml的protocolNode对象转BAONProtocol对象
	 * @return
	 */
	public BAONIProtocol toBAONIProtocol(){
		BAONIProtocol rt=new BAONProtocol();
		boolean isInLoop=false;
		Stack<ParseSence> stack=new Stack<ParseSence>();
		for(int k=0,l=fields.size();k<l;k++){
			FieldNode fieldNode=fields.get(k);
			if(fieldNode.type==DataTypes.LOOPSTART){
				logger.debug("start a loop.");
				stack.push(new ParseSence(new BAONArray(fieldNode.name), isInLoop));
				isInLoop=true;
				continue;
			}else if(fieldNode.type==DataTypes.LOOPEND){
				logger.debug("end a loop");
				ParseSence sence=stack.pop();
				isInLoop=sence.isInLoop;
				rt.addField((BAONIFiled)sence.array);
				continue;
			}else if(fieldNode.type==DataTypes.END){
				logger.debug("never goes here.");
			}else{
				BAONIFiled recordField=null;
				if(fieldNode.type==DataTypes.BYTE){
					logger.debug("is BYTE");
					recordField=new BAONByte(fieldNode.name,(byte)0);
				}else if(fieldNode.type==DataTypes.SHORT){
					logger.debug("is SHORT");
					recordField=new BAONShort(fieldNode.name,(short)0);
				}else if(fieldNode.type==DataTypes.INT){
					logger.debug("is INT");
					recordField=new BAONInt(fieldNode.name,0);
				}else if(fieldNode.type==DataTypes.FLOAT){
					logger.debug("is FLOAT");
					recordField=new BAONFloat(fieldNode.name,0f);
				}else if(fieldNode.type==DataTypes.LONG){
					logger.debug("is LONG");
					recordField=new BAONLong(fieldNode.name,0l);
				}else if(fieldNode.type==DataTypes.STRING){
					logger.debug("is STRING");
					recordField=new BAONString(fieldNode.name,"");
				}else {
					logger.debug("error data type:"+fieldNode.type);
				}
				if(recordField!=null){
					if(isInLoop){
						stack.peek().array.addField(recordField);
					}else{
						rt.addField(recordField);
					}
				}
			}
		}
		return rt;
	}
}
