/**
 * 
 */
package javaz.baon.beans;

import java.util.List;
import java.util.Map;

/**
 * @author Zero
 * @mail baozilaji@126.com
 *
 * Aug 11, 2014
 */
public interface BAONIArray extends BAONFieldCollection {
	/**
	 * 获取数组的内容
	 * @return
	 */
	public List<Map<String, BAONIFiled>> getContent();
	/**
	 * 设置协议数据内容
	 * @param content
	 */
	public void setContent(List<Map<String, BAONIFiled>> content);
}
