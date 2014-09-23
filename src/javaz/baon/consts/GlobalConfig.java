/**
 * 
 */
package javaz.baon.consts;

/**
 * 常量配置类
 * @author Zero
 * @mail baozilaji@126.com
 * Aug 11, 2014
 */
public interface GlobalConfig {
	/**变量名只支持16个字符，因为只用4bit来存储它*/
	public static final byte MAX_VARIABLE_LENGTH=0xF;
	/**协议魔数*/
	public static final byte[] MIGIC_NUMBER={'Z','e','r','o'};
	/**
	 * 默认下行协议健
	 */
	public static final String KEY_DEFAULT_DOWN_PROTOCOL_ID_KEY="default_down_protocol_id";
	/**
	 * 协议名称定义－下行状态
	 */
	public static final String KEY_DOWN_PROTOCOL_STATE_NAME="down_protocol_state_name";
	/**
	 * 协议名称定义－下行消息
	 */
	public static final String KEY_DOWN_PROTOCOL_MSG_NAME="down_protocol_msg_name";
}
