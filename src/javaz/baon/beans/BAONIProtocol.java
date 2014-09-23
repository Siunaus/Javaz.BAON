/**
 * 
 */
package javaz.baon.beans;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * @author Zero
 * @mail baozilaji@126.com
 *
 * Aug 11, 2014
 */
public interface BAONIProtocol extends BAONFieldCollection{
	/**
	 * 协议克隆
	 * @return
	 */
	public BAONIProtocol cloneProtocol();
	/**
	 * 将协议域的值写入流
	 * @param dos
	 * @throws IOException
	 */
	public abstract void desc2Bytes(DataOutputStream dos)throws IOException;
	/**
	 * 协议解析
	 * @param dis
	 * @throws IOException
	 */
	public void parseProtocol(DataInputStream dis)throws IOException;
	/**
	 * 输出协议
	 * @param dos
	 * @throws IOException
	 */
	public void flushProtocol(DataOutputStream dos)throws IOException;
	/**
	 * 取协议指定名称的域数据
	 * @return
	 */
	public BAONIFiled getValue(String name);
	/**
	 * 将数据保存到协议体
	 * @param name
	 * @param field
	 */
	public void putValue(String name,BAONIFiled field);
}
