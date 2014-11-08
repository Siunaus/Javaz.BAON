/**
 * 
 */
package javaz.baon.beans;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import javaz.baon.enums.DataTypes;
import javaz.utils.CommUtil;
import javaz.utils.string.StringUtil;

/**
 * @author Zero
 * @mail baozilaji@126.com
 *
 * Aug 11, 2014
 */
@SuppressWarnings("unchecked")
public class BAONString extends BAONSingleField<String> {
	/**
	 * 默认构造
	 */
	public BAONString(){
		this("");
	}
	/**
	 * 
	 * @param value
	 */
	public BAONString(String value){
		super(DataTypes.STRING, "", value);
	}
	/**
	 * @param fieldName
	 * @param value
	 */
	public BAONString(String fieldName, String value) {
		super(DataTypes.STRING, fieldName, value);
	}
	/*
	 * (non-Javadoc)
	 * @see javaz.baon.beans.BAONField#cloneField()
	 */
	@Override
	public BAONIFiled cloneField() {
		return new BAONString(fieldName, _value);
	}
	/* (non-Javadoc)
	 * @see javaz.baon.beans.BAONIFiled#content2Bytes(java.io.DataOutputStream)
	 */
	public void content2Bytes(DataOutputStream dos) throws IOException {
		dos.writeUTF(_value);
		CommUtil.logInfo(logger, StringUtil.format("writeUTF[{0}]:{1}", fieldName, _value));
	}
	/* (non-Javadoc)
	 * @see javaz.baon.beans.BAONIFiled#bytes2Content(java.io.DataInputStream)
	 */
	public void bytes2Content(DataInputStream dis) throws IOException {
		_value=dis.readUTF();
		CommUtil.logInfo(logger, StringUtil.format("readUTF[{0}]:{1}", fieldName, _value));
	}
}
