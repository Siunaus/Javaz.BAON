/**
 * 
 */
package javaz.baon.manager.http.xml;

import javaz.baon.enums.DataTypes;

/**
 * @author Zero
 * @mail baozilaji@126.com
 * xml文档的field节点
 * Aug 13, 2014
 */
public class FieldNode {
	/**
	 * 协议域的数据类型
	 */
	DataTypes type;
	/**
	 * 协议域的名称
	 */
	String name="";
	/**
	 * 协议域的描述
	 */
	String desc="";
	public FieldNode(String name,DataTypes type,String desc){
		this.name=name;
		this.type=type;
		this.desc=desc;
	}
}
