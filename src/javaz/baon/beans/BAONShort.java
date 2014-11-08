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
public class BAONShort extends BAONSingleField<Short> {
	/**
	 * 默认构造
	 */
	public BAONShort(){
		this((short)0);
	}
	public BAONShort(short value){
		super(DataTypes.SHORT, "", value);
	}
	/**
	 * @param fieldName
	 * @param value
	 */
	public BAONShort(String fieldName, Short value) {
		super(DataTypes.SHORT, fieldName, value);
	}
	/*
	 * (non-Javadoc)
	 * @see javaz.baon.beans.BAONField#cloneField()
	 */
	@Override
	public BAONIFiled cloneField() {
		return new BAONShort(fieldName, _value);
	}
	/* (non-Javadoc)
	 * @see javaz.baon.beans.BAONIFiled#content2Bytes(java.io.DataOutputStream)
	 */
	public void content2Bytes(DataOutputStream dos) throws IOException {
		dos.writeShort(_value);
		CommUtil.logInfo(logger, StringUtil.format("writeShort[{0}]:{1}", fieldName, _value));
	}
	/* (non-Javadoc)
	 * @see javaz.baon.beans.BAONIFiled#bytes2Content(java.io.DataInputStream)
	 */
	public void bytes2Content(DataInputStream dis) throws IOException {
		_value=dis.readShort();
		CommUtil.logInfo(logger, StringUtil.format("readShort[{0}]:{1}", fieldName, _value));
	}
}
