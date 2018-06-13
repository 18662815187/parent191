<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="../head.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>babasport-list</title>
<script type="text/javascript">
	//全选,此js的意思是，找到name值为传进来的name的input标签，设置选中属性true/flase，这个选中属性在
	//下方点击事件中使用this.checked来获取true、flase
	function checkBox(name, checked) {
		$("input[name=" + name + "]").attr("checked", checked);
		//$("input[name=" + name +"]").prop("checked",checked);
	}
	//批量删除,需要配合form标签使用，在需要提交的部分用form包裹，设置id=“jvForm”,在js控制此id的form提交
	//快捷键Ctrl+K直接跳转到该方法
	//传入搜索条件和分页页码,这样做是为了删除之后显示的依然是通过条件搜索过的页面
	function optDelete(id,isshow,optionName,pageNo,flag) {
		if(id!==null&&flag!=null&&flag==1){
			//这两行是用于数据后方的删除按钮,需传入id,这两行必须放最前面
			var delByID = $('#' + id);
			delByID.attr("checked",true);
		}
		//请至少选择一个,只取被选中的，使用checked区分,判断长度是否为0，如为0提示至少选一个
		var size = $("input[name=ids]:checked").size();
		if (size == 0) {
			alert("请至少选择一个");
			return;
		}
		//你确定删除吗？
		if (!confirm("你确定删除吗?")) {
			return;
		}
		$("#jvForm").attr(
				"action",
				"/type/deletes.do?optionName=" + optionName + "&isshow=" + isshow
						+ "&pageNo=" + pageNo);
		$("#jvForm").attr("method", "post").submit();
	}
	function optDeleteOne(id, isshow, optionName, pageNo) {
		if (!confirm("你确定删除吗?")) {
			return;
		}
		$("#jvForm").attr(
				"action",
				"/type/delete.do?id=" + id + "&isshow=" + isshow + "&pageNo="
						+ pageNo);
		$("#jvForm").attr("method", "post").submit();
	}

	function toEdit(id, isshow, optionName, pageNo) {
		$("#jvForm").attr(
				"action",
				"/type/toEdit.do?id=" + id + "&isshow=" + isshow
						+ "&optionName=" + optionName + "&pageNo=" + pageNo);

		$("#jvForm").attr("method", "post").submit();
	}
	function toAdd(isshow, optionName, pageNo) {
		$("#roptF").attr(
				"action",
				"/type/toAdd.do?optionName=" + optionName + "&isshow" + isshow
						+ "&pageNo=" + pageNo);
		$("#roptF").attr("method", "post").submit();
	}
</script>
</head>
<body>
	<div class="box-positon">
		<div class="rpos">当前位置: 分类管理 - 列表</div>
		<form class="ropt" id="roptF">
			<input class="add" type="button" value="添加" onclick="toAdd('${isshow}','${optionName}','${pagination.pageNo}');"/>
				<!-- onclick="javascript:window.location.href='toAdd.do?optionName=${optionName}&isshow=${isshow}&pageNo=${pageNo}'" -->
		</form>
		<div class="clear"></div>
	</div>
	<div class="body-box">
		<form action="/type/list.do" method="post" style="padding-top: 5px;">
			品牌名称: <input type="text" name="optionName" value="${optionName}" />
			<input type="hidden" value="${flag}" />
			<select name="isshow">
				<option value="1" <c:if test="${isshow}">selected="selected"</c:if>>是</option>
				<option value="0" <c:if test="${!isshow}">selected="selected"</c:if>>否</option>
			</select> <input type="submit" class="query" value="查询" />
		</form>
		<form id="jvForm">
			<table cellspacing="1" cellpadding="0" border="0" width="100%"
				class="pn-ltable">
				<thead class="pn-lthead">
					<tr>
						<th width="20"><input type="checkbox"
							onclick="checkBox('ids',this.checked)" /></th>
						<th>分类ID</th>
						<th>分类名称</th>
						<th>上级分类</th>
						<th>是否可用</th>
						<th>操作选项</th>
					</tr>
				</thead>
				<tbody class="pn-ltbody">
					<c:forEach items="${pagination.list}" var="data">
						<tr bgcolor="#ffffff" onmouseout="this.bgColor='#ffffff'"
							onmouseover="this.bgColor='#eeeeee'">

							<td align="center"><input type="checkbox" value="${data.id}"
								name="ids" id="${data.id}"/></td>
							<td align="center">${data.id}</td>
							<td align="center">${data.optionName}</td>
							<td align="center"><c:choose>
									<c:when test="${data.praentName==null}">本类为父级</c:when>
									<c:when test="${data.praentName==''}">本类为父级</c:when>
									<c:otherwise>${data.praentName}</c:otherwise>
								</c:choose></td>
							<td align="center"><c:if test="${data.isshow}">是</c:if> <c:if
									test="${!data.isshow}">否</c:if></td>
							<td align="center"><a class="pn-opt" href="#"
								onclick="toEdit('${data.id}','${isshow}','${optionName}','${pagination.pageNo}');return false;">修改</a>
								| <a class="pn-opt" href="#"
								onclick="optDelete('${data.id}','${isshow}','${optionName}','${pagination.pageNo}','1');return false;">删除</a>
								<%-- 								<a class="pn-opt" onclick="if(!confirm('您确定删除吗？')) {return false;}" href="delete.do?id=${data.id}&name=${optionName}&isDisplay=${isshow}&pageNo=${pagination.pageNo}">删除</a> --%>

								<!-- 							<a class="pn-opt" --> <%-- 								href="/type/toEdit.do?id=${data.id}&name=${optionName}&isshow=${isshow}&pageNo=${pagination.pageNo}">修改</a> --%>
							</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
		</form>
		<div class="page pb15">
			<span class="r inb_a page_b"> <c:forEach
					items="${pagination.pageView}" var="page">
					${page}
			</c:forEach>
			</span>
		</div>
		<div style="margin-top: 15px;">
			<input class="del-button" type="button" value="删除"
				onclick="optDelete('${data.id}','${isshow}','${optionName}','${pagination.pageNo}');" />
		</div>
	</div>
</body>
</html>