import javaz.baon.manager.http.HttpProtocolManager;
import javaz.utils.PathUtil;
import junit.framework.TestCase;

import org.junit.Test;

/**
 * 
 */

/**
 * @author Zero
 * @mail baozilaji@126.com
 *
 * Sep 11, 2014
 */
public class TestPdFile extends TestCase{

	@Test
	public void testPdFile() {
		HttpProtocolManager.getIntance().loadFromXml(PathUtil.getRootClassPath()+"protocols.xml").saveToXmlFile("/tmp/protocols_tmp.xml");
		HttpProtocolManager.getIntance().loadFromXml(PathUtil.getRootClassPath()+"protocols.xml").parseToPdFile(PathUtil.getRootClassPath()+"protocols.pd");
	}
	
	@Test
	public void testLoadFromPdFile(){
		HttpProtocolManager.getIntance().getHttpProtocol((short)1);
	}

}
