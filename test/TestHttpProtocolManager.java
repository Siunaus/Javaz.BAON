import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.util.List;
import java.util.Map;

import javaz.baon.beans.BAONIArray;
import javaz.baon.beans.BAONIFiled;
import javaz.baon.manager.http.HttpProtocolManager;
import javaz.baon.parser.http.HttpProtocolParser;
import javaz.baon.protocol.http.HttpProtocol;
import junit.framework.TestCase;

import org.junit.Test;

/**
 * 
 */

/**
 * @author Zero
 * @mail baozilaji@126.com
 *
 * Aug 13, 2014
 */
public class TestHttpProtocolManager extends TestCase{
	@Test
	public void testParseXml() throws Exception {
		HttpProtocol httpProtocol = HttpProtocolManager.getIntance().getHttpProtocol((short)1);
		httpProtocol.putHeadLongValue("htime", 100000);
		System.out.println("-----------"+httpProtocol.getHeadLongValue("htime"));
		
		
		String data = "90,101,114,111,0,2,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,7,122,104,97,110,103,100,102,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,2,0,0,0,10,0,11,0,2,0,0,0,100,1,0,0,0,101,2,2,0,0,0,11,0,12,0,2,0,0,0,100,1,0,0,0,101,2,2,3";
		String bs[]=data.split(",");
		byte b[]=new byte[bs.length];
		for(int i=0; i<bs.length;i++){
			b[i]=Byte.valueOf(bs[i]);
		}
		
		DataInputStream dis = new DataInputStream(new ByteArrayInputStream(b));
		HttpProtocol http = HttpProtocolParser.getIntance().buildHttpProtocol(dis);
		System.out.println(http.getHeadLongValue("htime"));
		System.out.println(http.getBodyStringValue("name"));
		List<Map<String, BAONIFiled>> equipList = http.getBodyListValue("equipList");
		if(equipList!=null&&!equipList.isEmpty()){
			for(Map<String, BAONIFiled> map:equipList){
				System.out.println(map.get("id").getValue());
				System.out.println(map.get("sid").getValue());
				System.out.println("pp:"+map.get("pp").getValue());
				BAONIArray _array = (BAONIArray)map.get("skills");
				List<Map<String, BAONIFiled>> _arrayContent = _array.getContent();
				for(Map<String, BAONIFiled> _map:_arrayContent){
					System.out.println("skillId:"+_map.get("skillId").getValue());
					System.out.println("skilllv:"+_map.get("skilllv").getValue());
				}
			}
		}
		System.out.println(http.getBodyByteValue("qq"));
	}
}
