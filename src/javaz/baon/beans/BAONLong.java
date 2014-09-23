/**
 * 
 */
package javaz.baon.beans;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import javaz.baon.enums.DataTypes;

/**
 * @author Zero
 * @mail baozilaji@126.com
 *
 * Aug 11, 2014
 */
@SuppressWarnings("unchecked")
public class BAONLong extends BAONSingleField<Long> {
	/**
	 * 默认构造
	 */
	public BAONLong(){
		this(0);
	}
	public BAONLong(long value){
		super(DataTypes.LONG, "", value);
	}
	/**
	 * @param fieldName
	 * @param value
	 */
	public BAONLong(String fieldName, Long value) {
		super(DataTypes.LONG, fieldName, value);
	}
	/*
	 * (non-Javadoc)
	 * @see javaz.baon.beans.BAONField#cloneField()
	 */
	@Override
	public BAONIFiled cloneField() {
		return new BAONLong(fieldName, _value);
	}
	/* (non-Javadoc)
	 * @see javaz.baon.beans.BAONIFiled#content2Bytes(java.io.DataOutputStream)
	 */
	public void content2Bytes(DataOutputStream dos) throws IOException {
		dos.writeLong(_value);
	}
	/* (non-Javadoc)
	 * @see javaz.baon.beans.BAONIFiled#bytes2Content(java.io.DataInputStream)
	 */
	public void bytes2Content(DataInputStream dis) throws IOException {
		_value=dis.readLong();
	}
}
