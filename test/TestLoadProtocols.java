import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javaz.baon.beans.BAONArray;
import javaz.baon.beans.BAONByte;
import javaz.baon.beans.BAONIFiled;
import javaz.baon.beans.BAONInt;
import javaz.baon.beans.BAONShort;
import javaz.baon.manager.http.HttpProtocolManager;
import javaz.baon.parser.http.HttpProtocolParser;
import javaz.baon.protocol.http.HttpProtocol;
import junit.framework.TestCase;

import org.junit.Test;


public class TestLoadProtocols extends TestCase{
	@Test
	public void testLoadProtocols(){
		HttpProtocolManager.getIntance();
		HttpProtocol protocol = HttpProtocolManager.getIntance().getHttpProtocol((short)1);
		protocol.putHeadLongValue("htime", 1000);
		System.out.println(protocol.getHeadLongValue("htime"));
		
		
		HttpProtocol protocol2 = HttpProtocolManager.getIntance().getHttpProtocol((short)2);
		protocol2.putBodyStringValue("name", "zhangdf");
		List<Map<String, BAONIFiled>> list = new ArrayList<Map<String,BAONIFiled>>();
		for(int i=0;i<2;i++){
			Map<String, BAONIFiled> map = new HashMap<String, BAONIFiled>();
			map.put("id", new BAONInt(10+i));
			map.put("sid", new BAONShort((short)(11+i)));
			BAONArray ids = new BAONArray();
			List<Map<String, BAONIFiled>> list_ = new ArrayList<Map<String,BAONIFiled>>();
			for(int j=0;j<2;j++){
				Map<String, BAONIFiled> map_ = new HashMap<String, BAONIFiled>();
				map_.put("skillId", new BAONInt(100+j));
				map_.put("skilllv", new BAONByte((byte)(1+j)));
				list_.add(map_);
			}
			ids.setContent(list_);
			map.put("skills", ids);
			map.put("pp", new BAONByte((byte)2));
			list.add(map);
		}
		protocol2.putBodyListValue("equipList", list);
		protocol2.putBodyByteValue("qq", 3);
		ByteArrayOutputStream baos = new ByteArrayOutputStream(1024);
		DataOutputStream dos = new DataOutputStream(baos);
		try {
			HttpProtocolParser.getIntance().flushHttpProtocol(dos, protocol2);
			byte[] bs = baos.toByteArray();
			for(int i=0;i<bs.length;i++){
				System.out.print(bs[i]+",");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
