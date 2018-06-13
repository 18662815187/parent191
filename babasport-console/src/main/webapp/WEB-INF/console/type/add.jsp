<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="../head.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>babasport-add</title>
<script type="text/javascript">
	//上传图片
	function uploadPic() {
		//jquery.form.js
		var options = {
			url : "/upload/uploadPic1.do",
			dataType : "json",
			type : "post",
			success : function(data) {
				//alert(1);
				//返回的数据通过下方控件的id给他们设置值,src需要通过attr属性去设置，val可以直接设置
				//原数据存在图片时，重新上传替换URL需对此两个地方进行回显设置
				$("#allUrl").attr("src", data.url);
				$("#imgUrl").val(data.url);
			}
		}
		//ajax模拟jvForm这个form的提交
		$("#jvForm").ajaxSubmit(options);
	}
</script>
</head>
<body>
	<div class="box-positon">
		<div class="rpos">当前位置: 分类管理 - 添加</div>
		<form class="ropt">
			<input type="submit" onclick="javascript:history.back(-1);"
				value="返回列表" class="return-button" />
		</form>
		<div class="clear"></div>
	</div>
	<div class="body-box" style="float: right">
		<form id="jvForm" action="saveOrUpdate.do" method="post"
			enctype="multipart/form-data">
			<input  type="hidden" value="${data.id}" name="id"/>
			<input  type="hidden" value="${optionName1}" name="optionName1"/>
			<input  type="hidden" value="${isshow1}" name="isshow1"/>
			<input  type="hidden" value="${pageNo1}" name="pageNo1"/>
			<table cellspacing="1" cellpadding="2" width="100%" border="0"
				class="pn-ftable">
				<tbody>
					<tr>
						<td width="20%" class="pn-flabel pn-flabel-h"><span
							class="pn-frequired">*</span> 分类名称:</td>
						<td width="80%" class="pn-fcontent"><input type="text"
							class="required" name="optionName" value="${data.optionName}"
							maxlength="100" /></td>
					</tr>
					<tr>
						<td width="20%" class="pn-flabel pn-flabel-h">上级菜单:</td>
						<td width="80%" class="pn-fcontent"><select name="optionLevel">
								<option value="0">--此类本身为一级菜单--</option>
								<c:forEach items="${datas}" var="data">
									<option value="${data.id}"
										<c:if test="${optionLevel == data.id }">selected="selected"</c:if>>${data.optionName}</option>
								</c:forEach>
						</select>
						</td>
					</tr>
					<tr>
						<td width="20%" class="pn-flabel pn-flabel-h">是否可用:</td>
						<td width="80%" class="pn-fcontent">
						<input type="radio"
							name="isshow" value="1" <c:if test="${data.isshow}">checked="checked"</c:if> />可用 <input
							type="radio" name="isshow" value="0" <c:if test="${!data.isshow}">checked="checked"</c:if>/>不可用</td>
					</tr>
				</tbody>
				<tbody>
					<tr>
						<td class="pn-fbutton" colspan="2"><input type="submit"
							class="submit" value="提交" /> &nbsp; <input type="reset"
							class="reset" value="重置" /></td>
					</tr>
				</tbody>
			</table>
		</form>
	</div>
</body>
</html>