package com.lhh.base.entity;

/**
 * 普通实体管理方案
 * @author hwaggLee
 *
 */
public abstract class EntityAbstract implements EntityBase{

	private static final long serialVersionUID = 1L;

	/**
	 * 
	 * 判断两个entity是否是同一个id，为同一条数据
	 * 
	 * 两者比较时必须id不为空，否则默认不相等
	 * @param t
	 * @return :false：不相等
	 */
	public <T extends EntityBase> boolean equalsById(T t){
		if( t == null ) return false;
		if( t.getEntity() == null ) return false;
		if( !t.getEntity().equals(this.getEntity())) return false;
		String id = this.getId();
		if( id == null )return false;
		if( id.equals(t.getId())){
			return true;
		}
		return false;
	}
}
