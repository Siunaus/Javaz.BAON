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
public class BAONByte extends BAONSingleField<Byte> {
	/**
	 * 默认构造
	 */
	public BAONByte(){
		this((byte)0);
	}
	public BAONByte(byte value){
		super(DataTypes.BYTE, "", value);
	}
	/**
	 * @param fieldName
	 * @param value
	 */
	public BAONByte(String fieldName, Byte value) {
		super(DataTypes.BYTE, fieldName, value);
	}
	/*
	 * (non-Javadoc)
	 * @see javaz.baon.beans.BAONField#cloneField()
	 */
	@Override
	public BAONIFiled cloneField() {
		return new BAONByte(fieldName, _value);
	}
	/* (non-Javadoc)
	 * @see javaz.baon.beans.BAONIFiled#content2Bytes(java.io.DataOutputStream)
	 */
	public void content2Bytes(DataOutputStream dos) throws IOException {
		dos.writeByte(_value);
	}
	/* (non-Javadoc)
	 * @see javaz.baon.beans.BAONIFiled#bytes2Content(java.io.DataInputStream)
	 */
	public void bytes2Content(DataInputStream dis) throws IOException {
		_value=dis.readByte();
	}
}
