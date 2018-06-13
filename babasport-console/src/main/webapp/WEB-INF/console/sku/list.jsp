<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="../head.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>babasport-list</title>
<script type="text/javascript">
	//修改
	function updateSku(skuId) {
		//$("#a").find("input").attr("disabled",true);
		//$("input[name='ids']").attr("disabled",false);
		
		$("input[class='a']").attr("disabled", true);
		$(['#m','#p','#s','#l','#f'].join(skuId+',')+skuId).attr("disabled", false).val();
		//市场价
		//$("#m" + skuId).attr("disabled", false);
		//售价
		//$("#p" + skuId).attr("disabled", false);
		//库存
		//$("#s" + skuId).attr("disabled", false);
		//购买限制
		//$("#l" + skuId).attr("disabled", false);
		//运费
		//$("#f" + skuId).attr("disabled", false);
	}
	//保存
	function addSku(skuId) {
		//$(['#m','#p','#s','#l','#f'].join(skuId+',')+skuId).attr("disabled", true).val();
		//设disabled同时取值
		//市场价
		var m = $("#m" + skuId).attr("disabled", true).val();
		//售价
		var p = $("#p" + skuId).attr("disabled", true).val();
		//库存
		var s = $("#s" + skuId).attr("disabled", true).val();
		//购买限制
		var l = $("#l" + skuId).attr("disabled", true).val();
		//运费
		var f = $("#f" + skuId).attr("disabled", true).val();

		var url = "/sku/addSku.do";
		var params = {
			"marketPrice" : m,
			"price" : p,
			"stock" : s,
			"upperLimit" : l,
			"deliveFee" : f,
			"id" : skuId
		};
		//var params1 = '{"marketPrice" : m,"price" : p,"stock" : s,"upperLimit" : l,"deliveFee" : f,"id" : skuId}';
		//异步保存
		$.post(url, params, function(data) {
			alert(data.message);
		}, "json")
	}
</script>
</head>
<body>
	<div class="box-positon">
		<div class="rpos">当前位置: 库存管理 - 列表</div>
		<div class="clear"></div>
	</div>
	<div class="body-box">
		<form method="post" id="tableForm">
			<table cellspacing="1" cellpadding="0" border="0" width="100%"
				class="pn-ltable">
				<thead class="pn-lthead">
					<tr>
						<th width="20"><input type="checkbox"
							onclick="Pn.checkbox('ids',this.checked)" /></th>
						<th>商品编号</th>
						<th>商品颜色</th>
						<th>商品尺码</th>
						<th>市场价格</th>
						<th>销售价格</th>
						<th>库 存</th>
						<th>购买限制</th>
						<th>运 费</th>
						<th>是否赠品</th>
						<th>操 作</th>
					</tr>
				</thead>
				<tbody id="a" class="pn-ltbody">
					<c:forEach items="${skus}" var="sku">
						<tr bgcolor="#ffffff" onmouseover="this.bgColor='#eeeeee'"
							onmouseout="this.bgColor='#ffffff'">
							<td align="center"><input type="checkbox" name="ids"
								value="73"/></td>
							<td>${sku.productId}</td>
							<td align="center">${sku.color.name}</td>
							<td align="center">${sku.size}</td>
							<td align="center"><input class="a" type="text"
								id="m${sku.id}" value="${sku.marketPrice}" disabled="disabled"
								size="10" /></td>
							<td align="center"><input class="a" type="text"
								id="p${sku.id}" value="${sku.price}" disabled="disabled"
								size="10" /></td>
							<td align="center"><input class="a" type="text"
								id="s${sku.id}" value="${sku.stock}" disabled="disabled"
								size="10" /></td>
							<td align="center"><input class="a" type="text"
								id="l${sku.id}" value="${sku.upperLimit}" disabled="disabled"
								size="10" /></td>
							<td align="center"><input class="a" type="text"
								id="f${sku.id}" value="${sku.deliveFee}" disabled="disabled"
								size="10" /></td>
							<td align="center"><c:if test="${sku.isfree}">是</c:if> <c:if
									test="${!sku.isfree}">否</c:if></td>
							<td align="center"><a
								href="javascript:updateSku('${sku.id}')" class="pn-opt">编辑</a> |
								<a href="javascript:addSku('${sku.id}')" class="pn-opt">保存</a> |
								<a href="#" class="pn-opt">删除</a></td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
		</form>
	</div>
	<div style="margin-top: 15px;">
		<input class="del-button" type="button" value="删除"
			onclick="optDelete();" />
	</div>
</body>
</html>