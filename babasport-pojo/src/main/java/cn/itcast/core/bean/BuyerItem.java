package cn.itcast.core.bean;

import java.io.Serializable;

import cn.itcast.core.bean.product.Sku;

/**
 * 购物项
 * @author john
 *
 */
public class BuyerItem implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	//1.skuid  Sku对象有自己的id
	private Sku sku;
	
	//2.Boolean isHave;是否有货
	private Boolean isHave = true;
	
	//3.数量,默认一件
	private Integer amount = 1;

	public Sku getSku() {
		return sku;
	}

	public void setSku(Sku sku) {
		this.sku = sku;
	}

	public Boolean getIsHave() {
		return isHave;
	}

	public void setIsHave(Boolean isHave) {
		this.isHave = isHave;
	}

	public Integer getAmount() {
		return amount;
	}

	public void setAmount(Integer amount) {
		this.amount = amount;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((sku == null) ? 0 : sku.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)//比较地址
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())//class  cn.itcast.core.bean.BuyerItem，使用class比较
			return false;
		BuyerItem other = (BuyerItem) obj;//class相同时会强制转型再执行下面的操作
		if (sku == null) {
			if (other.sku != null)
				return false;
		} else if (!sku.getId().equals(other.sku.getId()))//比较skuid
			return false;
		return true;
	}
	
}
