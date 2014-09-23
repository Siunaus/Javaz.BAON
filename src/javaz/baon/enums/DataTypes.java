package javaz.baon.enums;
/**
 * 
 * @author Zero
 * @mail baozilaji@126.com
 * 数据类型
 * 用4个bit来存储它，所以最多支持2^4=16种数据类型
 * 目前包括：byte，short，int，float，long，string等常规类型
 * 以及：循环开始，循环结束，协议结束等控制符类型
 * Aug 11, 2014
 */
public enum DataTypes {
	/**0 代表 byte*/
	BYTE(false),
	
	/**1 代表 short*/
	SHORT(false),
	
	/**2 代表 int*/
	INT(false),
	
	/**3 代表 float*/
	FLOAT(false),
	
	/**4 代表 long*/
	LONG(false),
	
	/**5 代表 string*/
	STRING(false),
	
	/**6 代表 循环开始*/
	LOOPSTART(true),
	
	/**7 代表 循环结束*/
	LOOPEND(true),
	
	/**8 代表 协议结束*/
	END(true),
	;
	private final boolean isControl;
	private DataTypes(boolean control){
		this.isControl=control;
	}
	/**
	 * 是否是控制符
	 * @return the isControl
	 */
	public final boolean isControl() {
		return isControl;
	}
}
