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
public class BAONFloat extends BAONSingleField<Float> {
	/**
	 * 默认构造
	 */
	public BAONFloat(){
		this(0f);
	}
	public BAONFloat(float value){
		super(DataTypes.FLOAT, "", value);
	}
	/**
	 * @param fieldName
	 * @param value
	 */
	public BAONFloat(String fieldName, Float value) {
		super(DataTypes.FLOAT, fieldName, value);
	}
	/*
	 * (non-Javadoc)
	 * @see javaz.baon.beans.BAONField#cloneField()
	 */
	@Override
	public BAONIFiled cloneField() {
		return new BAONFloat(fieldName, _value);
	}
	/* (non-Javadoc)
	 * @see javaz.baon.beans.BAONIFiled#content2Bytes(java.io.DataOutputStream)
	 */
	public void content2Bytes(DataOutputStream dos) throws IOException {
		dos.writeFloat(_value);
	}
	/* (non-Javadoc)
	 * @see javaz.baon.beans.BAONIFiled#bytes2Content(java.io.DataInputStream)
	 */
	public void bytes2Content(DataInputStream dis) throws IOException {
		_value=dis.readFloat();
	}
}
