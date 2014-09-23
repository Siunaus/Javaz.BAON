/**
 * 
 */
package javaz.baon.beans;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javaz.baon.enums.DataTypes;
import javaz.utils.string.StringUtil;

import org.apache.log4j.Logger;

/**
 * @author Zero
 * @mail baozilaji@126.com
 *
 * Aug 11, 2014
 */
public class BAONProtocol implements BAONIProtocol{
	static Logger logger = Logger.getLogger("BAON::BAONProtocol");
	Map<String, BAONIFiled> map=new HashMap<String, BAONIFiled>();
	/**
	 * 一条协议的所有协议域
	 */
	protected List<BAONIFiled> fields=new ArrayList<BAONIFiled>();
	/* (non-Javadoc)
	 * @see javaz.baon.beans.BAONIProtocol#addField(javaz.baon.beans.BAONIFiled)
	 */
	@Override
	public void addField(BAONIFiled filed) {
		this.fields.add(filed);
	}
	/*
	 * (non-Javadoc)
	 * @see javaz.baon.beans.BAONIProtocol#cloneProtocol()
	 */
	@Override
	public BAONIProtocol cloneProtocol(){
		BAONIProtocol rt=new BAONProtocol();
		for(BAONIFiled filed:fields){
			rt.addField(filed.cloneField());
		}
		return rt;
	}
	/* (non-Javadoc)
	 * @see javaz.baon.beans.BAONIProtocol#desc2Bytes(java.io.DataOutputStream)
	 */
	@Override
	public void desc2Bytes(DataOutputStream dos) throws IOException {
		for(BAONIFiled field:fields){
			field.desc2Bytes(dos);
		}
		dos.writeByte((byte)(DataTypes.END.ordinal()<<4));
	}
	/* (non-Javadoc)
	 * @see javaz.baon.beans.BAONIProtocol#parseProtocol(java.io.DataInputStream)
	 */
	@Override
	public void parseProtocol(DataInputStream dis) throws IOException {
		for(BAONIFiled field:fields){
			field.bytes2Content(dis);
			map.put(field.getFieldName(), field);
		}
	}
	/* (non-Javadoc)
	 * @see javaz.baon.beans.BAONIProtocol#flushProtocol(java.io.DataOutputStream)
	 */
	@Override
	public void flushProtocol(DataOutputStream dos) throws IOException {
		for(BAONIFiled field:fields){
			BAONIFiled temp=map.get(field.getFieldName());
			if(temp!=null){
				if(temp instanceof BAONIArray){//如果是数组类型，需要将协议格式赋予临时协议域对象
					temp.setValue(field.getValue());
				}
				temp.content2Bytes(dos);
			}else{
				//输出默认值
				if(field.getFielType()==DataTypes.BYTE){
					temp=new BAONByte((byte)0);
				}else if(field.getFielType()==DataTypes.SHORT){
					temp=new BAONShort((short)0);
				}else if(field.getFielType()==DataTypes.INT){
					temp=new BAONInt(0);
				}else if(field.getFielType()==DataTypes.FLOAT){
					temp=new BAONFloat(0f);
				}else if(field.getFielType()==DataTypes.LONG){
					temp=new BAONLong(0l);
				}else if(field.getFielType()==DataTypes.STRING){
					temp=new BAONString("");
				}else if(field instanceof BAONArray){
					temp=new BAONArray("");
				}
				if(temp!=null){
					logger.warn(StringUtil.format("field {0} has no value.has been seted default value.", field.getFieldName()));
					temp.content2Bytes(dos);
				}else{
					logger.error("not supported field->"+field);
				}
			}
		}
	}
	/*
	 * (non-Javadoc)
	 * @see javaz.baon.beans.BAONIProtocol#getValue(java.lang.String)
	 */
	@Override
	public BAONIFiled getValue(String name) {
		return map.get(name);
	}
	/* (non-Javadoc)
	 * @see javaz.baon.beans.BAONIProtocol#putValue(java.lang.String, javaz.baon.beans.BAONIFiled)
	 */
	@Override
	public void putValue(String name, BAONIFiled field) {
		map.put(name, field);
	}
}
