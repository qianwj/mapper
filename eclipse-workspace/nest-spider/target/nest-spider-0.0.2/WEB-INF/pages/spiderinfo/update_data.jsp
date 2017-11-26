<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html lang="zh-CN">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="description" content="">
    <meta name="author" content="Qian Wenjin">
    <link rel="shortcut icon" href="#">

    <title>Nest -- 更新数据</title>

    <!-- Bootstrap core CSS -->
    <link href="../css/bootstrap.css" rel="stylesheet">

    <!-- Custom styles for this template -->
    <link href="../css/offcanvas.css" rel="stylesheet">
    <link href="../css/basic.css" rel="stylesheet">

    <!-- HTML5 shim and Respond.js IE8 support of HTML5 elements and media queries -->
    <!--[if lt IE 9]>
    <script src="http://cdn.bootcss.com/html5shiv/3.7.0/html5shiv.min.js"></script>
    <script src="http://cdn.bootcss.com/respond.js/1.3.0/respond.min.js"></script>
    <![endif]-->
</head>
<body style="background-image: url(../imgs/background/BingWallpaper-2017-10-09.jpg)">
<c:if test="${errorMsg!= null}">
  <script type="text/javascript">
     showModal("更新失败", "未找到该模板");
  </script>
</c:if>
<c:if test="${traceId!= null}">
  <input type="text" id="traceId" value="${traceId }" style="display:none">
  <script type="text/javascript">
     showModal("更新成功", $('#traceId').val());
  </script>
</c:if>
<div class="modal fade" id="confirmModal" tabindex="-1" role="dialog" style="overflow:scroll">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                    <span class="sr-only">Close</span>
                </button>
                <h4 class="modal-title" id="confirmModalTitle"></h4>
            </div>
            <div class="modal-body" id="confirmModalBody">

            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-secondary" data-dismiss="modal" id="cancelButton">取消</button>
                <button type="button" class="btn btn-primary" id="confirmButton">确定</button>
            </div>
        </div><!-- /.modal-content -->
    </div><!-- /.modal-dialog -->
</div>

<!-- ====================================== nav bar ============================================= -->
<div class="navbar navbar-inverse navbar-fixed-top" role="navigation">
    <div class="container">
        <div class="navbar-header">
            <button type="button" class="navbar-toggle" data-toggle="collapse" data-target=".navbar-collapse">
                <span class="sr-only">Toggle navigation</span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
            </button>
            <a class="navbar-brand" href="../index.html">Nest Spider</a>
        </div>
        <div class="navbar-collapse collapse">
            <ul class="nav navbar-nav">
                <li><a href="search_list.html">搜索</a></li>
                <li><a href="domain_list.html">网站列表</a></li>
                <li><a href="task_list.html">查看进度</a></li>
                <li><a href="edit_spiderinfo.html">模板编辑</a></li>
                <li><a href="spiderinfo_list.html">模板列表</a></li>
                <li class="active"><a href="">更新数据</a></li>
                <li><a href="quartz_list.html">定时任务管理</a></li>
            </ul>

        </div><!--/.navbar-collapse -->
    </div>
</div>

<div class="container">
    <form id="updateForm">
        <div class="form-group">
            <label for="spiderInfoIdUpdateBy" class="white">待更新的爬虫模板ID</label>
            <input type="text" class="form-control" id="spiderInfoIdUpdateBy" name="spiderInfoIdUpdateBy"
                   placeholder="待更新的爬虫模板ID"
                   value="${spiderInfoIdUpdateBy}" required>
        </div>
        <fieldset class="form-group">
            <label for="spiderInfoJson" class="white">Json爬虫模板</label>
            <textarea class="form-control" id="spiderInfoJson" rows="10"
                      name="spiderInfoJson" required>${spiderInfoJson}</textarea>
        </fieldset>
        <div class="form-group">
            <label for="callbackUrl" class="white">回调地址</label>
            <input type="text" class="form-control" id="callbackUrl" name="callbackUrl" placeholder="回调地址">
        </div>
        <button type="submit" class="btn btn-danger">提交</button>
    </form>
</div>

<div class="container navbar-fixed-bottom">
    <footer style="color:white;text-align: center">
        <p style="color:inherit">© 2017 Company, Inc. · <a href="#Nest-Tech" style="color:inherit">Nest Technology</a></p>
    </footer>
</div>

<script src="../js/jquery.min.js"></script>
<script src="../js/bootstrap.js"></script>
<script src="../js/jquery.validate.min.js"></script>
<script src="../js/my.js"></script>
</body>
</html>