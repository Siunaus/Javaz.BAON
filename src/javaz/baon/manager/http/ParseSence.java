/**
 * 
 */
package javaz.baon.manager.http;

import javaz.baon.beans.BAONIArray;

/**
 * 
 * @author Zero
 * @mail baozilaji@126.com
 *
 * Aug 13, 2014
 */
public class ParseSence {
	public BAONIArray array;
	public boolean isInLoop;
	public ParseSence(BAONIArray array,boolean isInLoop){
		this.array=array;
		this.isInLoop=isInLoop;
	}
}
