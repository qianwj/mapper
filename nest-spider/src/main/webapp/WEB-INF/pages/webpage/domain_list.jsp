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

    <title>Nest -- 网站列表</title>

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
<body  style="background-image: url(../imgs/background/BingWallpaper-2017-10-09.jpg)">

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
               <li><a href="/nest-spider/webpage/search_list.html">搜索</a></li>
                <li class="active"><a href="">网站列表</a></li>
                <li><a href="/nest-spider/task/task_list.html">查看进度</a></li>
                <li><a href="/nest-spider/spider/edit_spiderinfo.html">模板编辑</a></li>
                <li><a href="/nest-spider/spiderinfo/spiderinfo_list.html">模板列表</a></li>
                <li><a href="update_data.html">更新数据</a></li>
                <li><a href="/nest-spider/spider/quartz_list.html">定时任务管理</a></li>
            </ul>

        </div><!--/.navbar-collapse -->
    </div>
</div>

<div class="container">
    <table class="table table-hover">
        <thead class="white">
        <tr>
            <th>#</th>
            <th>网站域名</th>
            <th>资讯数</th>
            <th>查看列表</th>
            <th>删除</th>
            <th>导出数据</th>
        </tr>
        </thead>
        <tbody class="grey">
        <c:forEach items="${domainList}" var="domain" varStatus="index">
           <tr>
             <th scope="row">${index.count}</th>
             <td>${domain.key}</td>
             <td>${domain.value}</td>
             <td><a class="btn btn-info"
                   href="/nest-spider/webpage/list.do?domain=${domain.key}">查看资讯列表</a>
             </td>
             <td><a class="btn btn-danger"
                   onclick="rpcAndShowData('/nest-spider/spiderinfo/delByDomain.do', {domain: '${domain.key}'});">删除网站数据</a>
             </td>
             <td><a class="btn btn-info"
                   href="/nest-spider/webpage/exportWebpageJSONByDomain.do?domain=${domain.key}">导出该网站数据JSON</a>
             </td>
           </tr>
        </c:forEach>
        </tbody>
    </table>
</div>
<div class="container" id="wordCloudBody" style="height:400px"></div>

<div class="container navbar-fixed-bottom">
    <footer style="color:white">
        <p class="pull-right"><a href="search_list.html#" style="color:inherit">Back to top</a></p>
        <p style="color:inherit">© 2017 Company, Inc. · <a href="#Nest-Tech" style="color:inherit">Nest Technology Create by Software Engineer 1517 Qian Wenjin</a></p>
    </footer>
</div>

<!-- Bootstrap core JavaScript
    ================================================== -->
<!-- Placed at the end of the document so the pages load faster -->
<script src="../js/jquery.min.js"></script>
<script src="../js/bootstrap.min.js"></script>
<script src="../js/jquery.validate.min.js"></script>
<script src="../js/basic.js"></script>
</body>
</html>