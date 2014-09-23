/**
 * 
 */
package javaz.baon.protocol.http;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;

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
import javaz.baon.enums.HttpDataTypes;
import javaz.utils.string.StringUtil;

import org.apache.log4j.Logger;

/**
 * @author Zero
 * @mail baozilaji@126.com
 *
 * Aug 11, 2014
 */
public class HttpProtocol{
	static Logger logger=Logger.getLogger(HttpProtocol.class);
	/**
	 * 魔数
	 */
	byte[] magic;
	/**
	 * 协议id
	 */
	short cmdId;
	/**
	 * 协议头部分
	 */
	BAONIProtocol head;
	/**
	 * 协议体公共部分
	 */
	BAONIProtocol comm;
	/**
	 * 协议体
	 */
	BAONIProtocol body;
	/**
	 * 默认构造
	 */
	public HttpProtocol(){
		this(GlobalConfig.MIGIC_NUMBER, (short)-1, new BAONProtocol(), new BAONProtocol(), new BAONProtocol());
	}
	/**
	 * 通过指令id构造
	 * @param id
	 */
	public HttpProtocol(short id){
		this(GlobalConfig.MIGIC_NUMBER, id, new BAONProtocol(), new BAONProtocol(), new BAONProtocol());
	}
	/**
	 * 通过幻数和指令id构造
	 * @param magic
	 * @param id
	 */
	public HttpProtocol(byte[] magic,short id){
		this(magic, id, new BAONProtocol(), new BAONProtocol(), new BAONProtocol());
	}
	/**
	 * 
	 * @param magic
	 * @param id
	 * @param head
	 */
	public HttpProtocol(byte[] magic,short id,BAONIProtocol head){
		this(magic, id, head, new BAONProtocol(), new BAONProtocol());
	}
	/**
	 * 
	 * @param magic
	 * @param id
	 * @param head
	 * @param common
	 */
	public HttpProtocol(byte[] magic,short id,BAONIProtocol head,BAONIProtocol common){
		this(magic, id, head, common, new BAONProtocol());
	}
	/**
	 * 
	 * @param magic
	 * @param id
	 * @param head
	 * @param common
	 * @param body
	 */
	public HttpProtocol(byte[] magic,short id,BAONIProtocol head,BAONIProtocol common,BAONIProtocol body){
		this.magic=magic;
		this.cmdId=id;
		this.head=head;
		this.comm=common;
		this.body=body;
	}
	/**
	 * @param magic the magic to set
	 */
	public void setMagic(byte[] magic) {
		this.magic = magic;
	}
	/**
	 * @param cmdId the cmdId to set
	 */
	public void setCmdId(short cmdId) {
		this.cmdId = cmdId;
	}
	/**
	 * @return the cmdId
	 */
	public short getCmdId() {
		return cmdId;
	}
	/**
	 * @return the magic
	 */
	public byte[] getMagic() {
		return magic;
	}
	/**
	 * 
	 * @param dis
	 * @throws IOException
	 */
	public void parseHead(DataInputStream dis) throws IOException{
		head.parseProtocol(dis);
	}
	/**
	 * 
	 * @param dis
	 * @throws IOException
	 */
	public void parseComm(DataInputStream dis) throws IOException{
		comm.parseProtocol(dis);
	}
	/**
	 * 
	 * @param dis
	 * @throws IOException
	 */
	public void parseBody(DataInputStream dis) throws IOException{
		body.parseProtocol(dis);
	}
	/**
	 * 
	 * @param dos
	 * @throws IOException
	 */
	public void flushHead(DataOutputStream dos) throws IOException{
		head.flushProtocol(dos);
	}
	/**
	 * 
	 * @param dos
	 * @throws IOException
	 */
	public void flushComm(DataOutputStream dos) throws IOException{
		comm.flushProtocol(dos);
	}
	/**
	 * 
	 * @param dos
	 * @throws IOException
	 */
	public void flushBody(DataOutputStream dos) throws IOException{
		body.flushProtocol(dos);
	}
	/**
	 * 
	 * @param dataTypes
	 * @param key 
	 * @param value 传的协议数据，此对象不能为空，且应该与协议中定义的数据类型保持对应
	 */
	@SuppressWarnings("unchecked")
	private void putValue(HttpDataTypes dataTypes,String key,Object value){
		if(value==null){
			logger.error(StringUtil.format("{0}'s {1} is null", dataTypes,key));
			return;
		}
		BAONIFiled b=null;
		if(value instanceof Byte){
			b=new BAONByte((Byte)value);
		}else if(value instanceof Short){
			b=new BAONShort((Short)value);
		}else if(value instanceof Integer){
			b=new BAONInt((Integer)value);
		}else if(value instanceof Long){
			b=new BAONLong((Long)value);
		}else if(value instanceof Float){
			b=new BAONFloat((Float)value);
		}else if(value instanceof String){
			b=new BAONString((String)value);
		}else if(value instanceof List<?>){
			b=new BAONArray("");
			((BAONIArray)b).setContent((List<Map<String, BAONIFiled>>)value);
		}else{
			logger.error(StringUtil.format("datatype of {1} in {0} part is not {2}", dataTypes,key,value.toString()));
			return;
		}
		BAONIProtocol p=null;
		switch (dataTypes) {
		case HEAD:
			p=head;
			break;
		case COMM:
			p=comm;
			break;
		case BODY:
			p=body;
			break;
		}
		p.putValue(key, b);
	}
	/**
	 * head放入byte数据
	 * @param key
	 * @param value
	 */
	public void putHeadByteValue(String key,int value){
		putValue(HttpDataTypes.HEAD, key, (byte)value);
	}
	/**
	 * head放入short数据
	 * @param key
	 * @param value
	 */
	public void putHeadShortValue(String key,int value){
		putValue(HttpDataTypes.HEAD, key, (short)value);
	}
	/**
	 * head放入int数据
	 * @param key
	 * @param value
	 */
	public void putHeadIntValue(String key,int value){
		putValue(HttpDataTypes.HEAD, key, value);
	}
	/**
	 * head放入long数据
	 * @param key
	 * @param value
	 */
	public void putHeadLongValue(String key,long value){
		putValue(HttpDataTypes.HEAD, key, value);
	}
	/**
	 * head放入float数据
	 * @param key
	 * @param value
	 */
	public void putHeadFloatValue(String key,float value){
		putValue(HttpDataTypes.HEAD, key, value);
	}
	/**
	 * head放入string数据
	 * @param key
	 * @param value
	 */
	public void putHeadStringValue(String key,String value){
		putValue(HttpDataTypes.HEAD, key, value);
	}
	/**
	 * comm放入byte数据
	 * @param key
	 * @param value
	 */
	public void putCommByteValue(String key,int value){
		putValue(HttpDataTypes.COMM, key, (byte)value);
	}
	/**
	 * comm放入short数据
	 * @param key
	 * @param value
	 */
	public void putCommShortValue(String key,int value){
		putValue(HttpDataTypes.COMM, key, (short)value);
	}
	/**
	 * comm放入int数据
	 * @param key
	 * @param value
	 */
	public void putCommIntValue(String key,int value){
		putValue(HttpDataTypes.COMM, key, value);
	}
	/**
	 * comm放入long数据
	 * @param key
	 * @param value
	 */
	public void putCommLongValue(String key,long value){
		putValue(HttpDataTypes.COMM, key, value);
	}
	/**
	 * comm放入float数据
	 * @param key
	 * @param value
	 */
	public void putCommFloatValue(String key,float value){
		putValue(HttpDataTypes.COMM, key, value);
	}
	/**
	 * comm放入String数据
	 * @param key
	 * @param value
	 */
	public void putCommStringValue(String key,String value){
		putValue(HttpDataTypes.COMM, key, value);
	}
	/**
	 * body放入byte数据
	 * @param key
	 * @param value
	 */
	public void putBodyByteValue(String key,int value){
		putValue(HttpDataTypes.BODY, key, (byte)value);
	}
	/**
	 * body放入short数据
	 * @param key
	 * @param value
	 */
	public void putBodyShortValue(String key,int value){
		putValue(HttpDataTypes.BODY, key, (short)value);
	}
	/**
	 * body放入int数据
	 * @param key
	 * @param value
	 */
	public void putBodyIntValue(String key,int value){
		putValue(HttpDataTypes.BODY, key, value);
	}
	/**
	 * body放入float数据
	 * @param key
	 * @param value
	 */
	public void putBodyFloatValue(String key,float value){
		putValue(HttpDataTypes.BODY, key, value);
	}
	/**
	 * body放入long数据
	 * @param key
	 * @param value
	 */
	public void putBodyLongValue(String key,long value){
		putValue(HttpDataTypes.BODY, key, value);
	}
	/**
	 * body放入string数据
	 * @param key
	 * @param value
	 */
	public void putBodyStringValue(String key,String value){
		putValue(HttpDataTypes.BODY, key, value);
	}
	/**
	 * body放入列表数据
	 * @param key
	 * @param value
	 */
	public void putBodyListValue(String key,List<Map<String, BAONIFiled>> value){
		putValue(HttpDataTypes.BODY, key, value);
	}
	/**
	 * 取协议里的数据
	 * @param dataTypes
	 * @param key
	 * @return
	 */
	private BAONIFiled getValue(HttpDataTypes dataTypes,String key){
		BAONIProtocol p=null;
		switch (dataTypes) {
		case HEAD:
			p=head;
			break;
		case COMM:
			p=comm;
			break;
		case BODY:
			p=body;
			break;
		}
		return p.getValue(key);
	}
	/**
	 * 取head里的byte数据
	 * @param key
	 * @return
	 */
	public byte getHeadByteValue(String key){
		return ((BAONByte)getValue(HttpDataTypes.HEAD, key)).getValue();
	}
	/**
	 * 取head里的short数据
	 * @param key
	 * @return
	 */
	public short getHeadShortValue(String key){
		return ((BAONShort)getValue(HttpDataTypes.HEAD, key)).getValue();
	}
	/**
	 * 取head里的int数据
	 * @param key
	 * @return
	 */
	public int getHeadIntValue(String key){
		return ((BAONInt)getValue(HttpDataTypes.HEAD, key)).getValue();
	}
	/**
	 * 取head里的long数据
	 * @param key
	 * @return
	 */
	public long getHeadLongValue(String key){
		return ((BAONLong)getValue(HttpDataTypes.HEAD, key)).getValue();
	}
	/**
	 * 取head里的float数据
	 * @param key
	 * @return
	 */
	public float getHeadFloatValue(String key){
		return ((BAONFloat)getValue(HttpDataTypes.HEAD, key)).getValue();
	}
	/**
	 * 取head里的string数据
	 * @param key
	 * @return
	 */
	public String getHeadStringValue(String key){
		return ((BAONString)getValue(HttpDataTypes.HEAD, key)).getValue();
	}
	/**
	 * 取comm里的byte数据
	 * @param key
	 * @return
	 */
	public byte getCommByteValue(String key){
		return ((BAONByte)getValue(HttpDataTypes.COMM, key)).getValue();
	}
	/**
	 * 取comm里的short数据
	 * @param key
	 * @return
	 */
	public short getCommShortValue(String key){
		return ((BAONShort)getValue(HttpDataTypes.COMM, key)).getValue();
	}
	/**
	 * 取comm里的int数据
	 * @param key
	 * @return
	 */
	public int getCommIntValue(String key){
		return ((BAONInt)getValue(HttpDataTypes.COMM, key)).getValue();
	}
	/**
	 * 取comm里的long数据
	 * @param key
	 * @return
	 */
	public long getCommLongValue(String key){
		return ((BAONLong)getValue(HttpDataTypes.COMM, key)).getValue();
	}
	/**
	 * 取comm里的float数据
	 * @param key
	 * @return
	 */
	public float getCommFloatValue(String key){
		return ((BAONFloat)getValue(HttpDataTypes.COMM, key)).getValue();
	}
	/**
	 * 取comm里的string数据
	 * @param key
	 * @return
	 */
	public String getCommStringValue(String key){
		return ((BAONString)getValue(HttpDataTypes.COMM, key)).getValue();
	}
	/**
	 * 取body里的byte数据
	 * @param key
	 * @return
	 */
	public byte getBodyByteValue(String key){
		return ((BAONByte)getValue(HttpDataTypes.BODY, key)).getValue();
	}
	/**
	 * 取body里的short数据
	 * @param key
	 * @return
	 */
	public short getBodyShortValue(String key){
		return ((BAONShort)getValue(HttpDataTypes.BODY, key)).getValue();
	}
	/**
	 * 取body里的int数据
	 * @param key
	 * @return
	 */
	public int getBodyIntValue(String key){
		return ((BAONInt)getValue(HttpDataTypes.BODY, key)).getValue();
	}
	/**
	 * 取body里的long数据
	 * @param key
	 * @return
	 */
	public long getBodyLongValue(String key){
		return ((BAONLong)getValue(HttpDataTypes.BODY, key)).getValue();
	}
	/**
	 * 取body里的float数据
	 * @param key
	 * @return
	 */
	public float getBodyFloatValue(String key){
		return ((BAONFloat)getValue(HttpDataTypes.BODY, key)).getValue();
	}
	/**
	 * 取body里的string数据
	 * @param key
	 * @return
	 */
	public String getBodyStringValue(String key){
		return ((BAONString)getValue(HttpDataTypes.BODY, key)).getValue();
	}
	/**
	 * 
	 * @param key
	 * @return
	 */
	public List<Map<String, BAONIFiled>> getBodyListValue(String key){
		return ((BAONIArray)getValue(HttpDataTypes.BODY, key)).getContent();
	}
}
