<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page trimDirectiveWhitespaces="true" %>
<%@ page session="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ include file="./head.jsp"%>
<html>
<head>
<script type="text/javascript">
//批量上传图片
function uploadPic() {
	//alert(1);
	//上传图片 异步的  	Jquery.form.js
	var options = {
		url : "/upload/uploadPics.do",
		type : "post",
		dataType : "json",
		//datas就是后台传来的urls
		success : function(data) {
			alert(data);
			//多图片回显,增加tr行和td列,原本此行是不存在的，在回显时给加上去，然后在第二个列（td）中加上两个input标签，第一个是遍历回显图片
			//第二个input设置成hidden用于提交数据
			var html = '<tr>'
					+ '<td width="20%" class="pn-flabel pn-flabel-h"></td>'
					+ '<td width="80%" class="pn-fcontent">';
			for (var i = 0; i < data.length; i++) {
				html += '<img width="100" height="100" src="' + data[i] + '" />'
						+ '<input type="hidden" name="imgUrl" value="' + data[i] + '"/>'
			}
			html += '<a href="javascript:;" class="pn-opt" onclick="jQuery(this).parents(\'tr\').remove()">删除</a>'
					+ '</td>' + '</tr>';
			//回显
			$("#tab_2").append(html);

		}
	}
	$("#jvForm").ajaxSubmit(options);
}

</script>


</head>
<body>
<h2>Hello World!</h2>
<body>  
<c:forEach items="${imagesPathList}" var="image">  
    <img src="${basePath}${image}"><br/>  
</c:forEach>  


<form action="${basePath}/upload/uploadFck.do" method="post" enctype="multipart/form-data">  
    <label>用户名：</label><input type="text" name="name"/><br/>  
    <label>密 码：</label><input type="password" name="password"/><br/>  
    <label>头 像1</label><input type="file" name="file" multiple="multiple" onchange="uploadPic()"/><br/>  
    <label>头 像2</label><input type="file" name="file"/><br/>  
    <input type="submit" value="提  交"/>  
</form>  

 <!-- 加载编辑器的容器 -->
    <script id="container" name="content" type="text/plain">
        这里写你的初始化内容
    </script>
    <!-- 配置文件 -->
    <script type="text/javascript" src="/utf8-jsp/ueditor.config.js"></script>
    <!-- 编辑器源码文件 -->
    <script type="text/javascript" src="/utf8-jsp/ueditor.all.js"></script>
    <!-- 实例化编辑器 -->
    <script type="text/javascript">
        var ue = UE.getEditor('container');
    </script>
</body>
</html>
