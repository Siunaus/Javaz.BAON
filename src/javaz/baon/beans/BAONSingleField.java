/**
 * 
 */
package javaz.baon.beans;

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
public abstract class BAONSingleField<T> extends BAONField<T> {
	/**
	 * 
	 * @param fieldType
	 * @param fieldName
	 * @param value
	 */
	public BAONSingleField(DataTypes fieldType,String fieldName,T value){
		this.fielType=fieldType;
		this.fieldName=fieldName;
		this._value=value;
	}
	/* (non-Javadoc)
	 * @see javaz.baon.beans.BAONIFiled#desc2Bytes(java.io.DataOutputStream)
	 */
	public void desc2Bytes(DataOutputStream dos) throws IOException {
		int nameLength=fieldName.length();
		dos.writeByte((byte)((fielType.ordinal()<<4)|nameLength));
		if(nameLength>0){
			dos.write(fieldName.getBytes());
		}
	}
}
