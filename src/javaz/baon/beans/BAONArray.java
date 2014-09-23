/**
 * 
 */
package javaz.baon.beans;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javaz.baon.enums.DataTypes;
import javaz.utils.CommUtil;
import javaz.utils.string.StringUtil;

/**
 * @author Zero
 * @mail baozilaji@126.com
 *
 * Aug 11, 2014
 */
@SuppressWarnings("unchecked")
public class BAONArray extends BAONField<List<BAONIFiled>> implements BAONIArray{
	List<Map<String, BAONIFiled>> _content=new ArrayList<Map<String, BAONIFiled>>();
	/**
	 * 默认构造
	 */
	public BAONArray(){
		this("");
	}
	public BAONArray(String fieldName){
		this.fieldName=fieldName;
		this._value=new ArrayList<BAONIFiled>();
	}
	/* (non-Javadoc)
	 * @see javaz.baon.beans.BAONIProtocol#addField(javaz.baon.beans.BAONIFiled)
	 */
	@Override
	public void addField(BAONIFiled filed) {
		_value.add(filed);
	}
	/* (non-Javadoc)
	 * @see javaz.baon.beans.BAONIArray#getContent()
	 */
	@Override
	public List<Map<String, BAONIFiled>> getContent() {
		return _content;
	}
	/* (non-Javadoc)
	 * @see javaz.baon.beans.BAONIArray#setContent(java.util.List)
	 */
	@Override
	public void setContent(List<Map<String, BAONIFiled>> content) {
		_content=content;
	}
	/* (non-Javadoc)
	 * @see javaz.baon.beans.BAONField#cloneField()
	 */
	@Override
	public BAONIFiled cloneField() {
		BAONArray rt=new BAONArray(fieldName);
		for(BAONIFiled filed:_value){
			rt.addField(filed.cloneField());
		}
		return rt;
	}
	/* (non-Javadoc)
	 * @see javaz.baon.beans.BAONIFiled#desc2Bytes(java.io.DataOutputStream)
	 */
	@Override
	public void desc2Bytes(DataOutputStream dos) throws IOException {
		int nameLength=fieldName.length();
		dos.writeByte((byte)((DataTypes.LOOPSTART.ordinal()<<4)|nameLength));
		if(nameLength>0){
			dos.write(fieldName.getBytes());
		}
		for(BAONIFiled field:_value){
			field.desc2Bytes(dos);
		}
		dos.writeByte((byte)(DataTypes.LOOPEND.ordinal()<<4));
	}
	/* (non-Javadoc)
	 * @see javaz.baon.beans.BAONIFiled#content2Bytes(java.io.DataOutputStream)
	 */
	@Override
	public void content2Bytes(DataOutputStream dos) throws IOException {
		dos.writeShort(_content.size());
		if(CommUtil.isEmpityList(_value)){
			logger.warn(StringUtil.format("{0} field has no sub fields.", getFieldName()));
		}else{
			for(Map<String, BAONIFiled> map:_content){
				for(BAONIFiled field:_value){
					BAONIFiled temp=(BAONIFiled)map.get(field.getFieldName());
					if(temp!=null){
						temp.content2Bytes(dos);
					}else{
						logger.error(StringUtil.format("field {0} has no value.", field.getFieldName()));
					}
				}
			}
		}
	}
	/* (non-Javadoc)
	 * @see javaz.baon.beans.BAONIFiled#bytes2Content(java.io.DataInputStream)
	 */
	@Override
	public void bytes2Content(DataInputStream dis) throws IOException {
		short length=dis.readShort();
		for(int i=0;i<length;i++){
			Map<String, BAONIFiled> map = new HashMap<String, BAONIFiled>();
			for(int j=0,k=_value.size();j<k;j++){
				BAONIFiled field=null;
				try {
					field=_value.get(j).getClass().newInstance();
				} catch (Exception e) {
					logger.error(StringUtil.format("class {0} has no default construct.", _value.get(i).getClass().getName()), e);
				}
				field.bytes2Content(dis);
				map.put(_value.get(j).getFieldName(), field);
			}
			_content.add(map);
		}
	}
}
