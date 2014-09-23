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
public interface BAONIFiled {
	/**
	 * 获取协议域的数据类型
	 * @return
	 */
	public DataTypes getFielType();
	/**
	 * 获取协议域的变量名称
	 * @return
	 */
	public String getFieldName();
	/**
	 * 设置协议域的变量名
	 * @param fieldName
	 */
	public void setFieldName(String fieldName);
	/**
	 * 获取协议域的变量值
	 * @return
	 */
	public <T> T getValue();
	/**
	 * 设置协议域的变量值
	 * @param value
	 */
	public <T> void setValue(T value);
	/**
	 * 协议域克隆
	 * @return
	 */
	public BAONIFiled cloneField();
	/**
	 * 将协议域描述写入流
	 * @param dos
	 * @throws IOException
	 */
	public abstract void desc2Bytes(DataOutputStream dos)throws IOException;
	/**
	 * 将协议域的值写入流
	 * @param dos
	 * @throws IOException
	 */
	public abstract void content2Bytes(DataOutputStream dos)throws IOException;
	/**
	 * 从流里读取协议域的值
	 * @param dis
	 * @throws IOException
	 */
	public abstract void bytes2Content(DataInputStream dis)throws IOException;
}
