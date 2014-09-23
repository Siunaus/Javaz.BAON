/**
 * 
 */
package javaz.baon.parser.http;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import javaz.baon.consts.GlobalConfig;
import javaz.baon.manager.http.HttpProtocolManager;
import javaz.baon.protocol.http.HttpProtocol;
import javaz.utils.string.StringUtil;

import org.apache.log4j.Logger;

/**
 * @author Zero
 * @mail baozilaji@126.com
 * 负责协议和流直接的转化
 * Aug 11, 2014
 */
public class HttpProtocolParser {
	static Logger logger= Logger.getLogger("BAON::HttpProtocolBuilder");
	private static HttpProtocolParser instance=new HttpProtocolParser();
	private HttpProtocolParser(){
	}
	public static HttpProtocolParser getIntance(){
		return instance;
	}
	/**
	 * 生成http协议数据
	 * @param dis
	 * @return
	 * @throws IOException
	 */
	public HttpProtocol buildHttpProtocol(DataInputStream dis)throws IOException{
		byte[] magic=new byte[GlobalConfig.MIGIC_NUMBER.length];
		dis.readFully(magic);
		short cmdId=dis.readShort();
		logger.debug(StringUtil.format("cmd id is:{0}", cmdId));
		HttpProtocol rt = HttpProtocolManager.getIntance().getHttpProtocol(cmdId);
		rt.setMagic(magic);
		rt.setCmdId(cmdId);
		rt.parseHead(dis);
		rt.parseComm(dis);
		rt.parseBody(dis);
		return rt;
	}
	/**
	 * 将协议数据输出到流
	 * @param dos
	 * @param protocol
	 * @throws IOException
	 */
	public void flushHttpProtocol(DataOutputStream dos,HttpProtocol protocol)throws IOException{
		dos.write(protocol.getMagic());
		dos.writeShort(protocol.getCmdId());
		protocol.flushHead(dos);
		protocol.flushComm(dos);
		protocol.flushBody(dos);
	}
}
