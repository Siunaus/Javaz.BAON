/**
 * 
 */
package javaz.baon.beans;

import org.apache.log4j.Logger;

import javaz.baon.enums.DataTypes;


/**
 * 协议域对象
 * @author Zero
 * @mail baozilaji@126.com
 *
 * Aug 11, 2014
 */
public abstract class BAONField<T> implements BAONIFiled{
	static Logger logger=Logger.getLogger("BAON::BAONField");
	/**
	 * 数据
	 */
	T _value;
	/**
	 * 协议域类型
	 */
	DataTypes fielType;
	/**
	 * 协议域名称
	 */
	String fieldName="";
	/* (non-Javadoc)
	 * @see javaz.baon.beans.BAONIFiled#getFieldName()
	 */
	public final String getFieldName() {
		return fieldName;
	}
	/* (non-Javadoc)
	 * @see javaz.baon.beans.BAONIFiled#setFieldName(java.lang.String)
	 */
	@Override
	public void setFieldName(String fieldName) {
		this.fieldName=fieldName;
	}
	/* (non-Javadoc)
	 * @see javaz.baon.beans.BAONIFiled#getFielType()
	 */
	@Override
	public final DataTypes getFielType() {
		return fielType;
	}
	/* (non-Javadoc)
	 * @see javaz.baon.beans.BAONIFiled#getValue()
	 */
	@SuppressWarnings("unchecked")
	@Override
	public T getValue() {
		return _value;
	}
	/*
	 * (non-Javadoc)
	 * @see javaz.baon.beans.BAONIFiled#setValue(java.lang.Object)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public <U> void setValue(U value) {
		this._value=(T) value;
	};
	/*
	 * (non-Javadoc)
	 * @see javaz.baon.beans.BAONIFiled#cloneField()
	 */
	abstract public BAONIFiled cloneField();
}
