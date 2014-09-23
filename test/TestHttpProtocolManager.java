import javaz.baon.manager.http.HttpProtocolManager;
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
		HttpProtocolManager.getIntance().getHttpProtocol((short)1).putHeadLongValue("htime", 100000);
		System.out.println(HttpProtocolManager.getIntance().getHttpProtocol((short)1).getHeadLongValue("htime"));
	}
}
