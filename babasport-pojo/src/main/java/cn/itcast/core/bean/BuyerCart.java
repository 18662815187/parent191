package cn.itcast.core.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * 购物车
 * 
 * @author john
 *
 */
public class BuyerCart implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	// 1.商品结果集List<BuyerItem>,初始化arrayList用于防止购物车为空值时添加到购物车报空指针
	private List<BuyerItem> items = new ArrayList<>();

	// 添加购物项到购物车
	public void addItem(BuyerItem item) {
		// 判断同款
		if (items.contains(item)) {
			// 如果现在的购物项包含准备加入的购物项则合并添加数量
			for (BuyerItem it : items) {
				if (it.equals(item)) {
					// 叠加数量
					Integer r = item.getAmount() + it.getAmount();
					// 重新设置数量
					it.setAmount(r);
				}
			}
		} else {
			items.add(item);
		}

	}

	public List<BuyerItem> getItems() {
		return items;
	}

	public void setItems(List<BuyerItem> items) {
		this.items = items;
	}

	// 2.小计 (数量、金额、运费、总计)
	// 商品数量
	//这个注解代表JSON和javaBean转换时忽略
	@JsonIgnore
	public Integer getProductAmount() {
		Integer result = 0;
		// 计算过程
		for (BuyerItem buyerItem : items) {
			result += buyerItem.getAmount();
		}
		return result;
	}

	// 商品金额
	//这个注解代表JSON和javaBean转换时忽略
	@JsonIgnore
	public Float getProductPrice() {
		Float result = 0f;
		// 计算过程
		for (BuyerItem buyerItem : items) {
			result += buyerItem.getAmount() * buyerItem.getSku().getPrice();
		}
		return result;
	}

	// 运费
	//这个注解代表JSON和javaBean转换时忽略
	@JsonIgnore
	public Float getFee() {
		Float result = 0f;
		if (getProductPrice() < 79) {
			// 总价低于79则默认8块钱运费
			result = 8f;
		}
		return result;
	}

	// 总金额
	//这个注解代表JSON和javaBean转换时忽略
	@JsonIgnore
	public Float getTotalPrice() {
		return getProductPrice() + getFee();
	}
}
