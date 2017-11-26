<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html lang="zh-CN">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="description" content="">
    <meta name="author" content="Qian Wenjin">
    <link rel="shortcut icon" href="#">

    <title>Nest -- 模板列表</title>

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
<!-- ====================================== modal ============================================= -->
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
                <li><a href="/nest-spider/webpage/search_list.html">搜索</a></li>
                <li><a href="/nest-spider/webpage/domain_list.html">网站列表</a></li>
                <li><a href="/nest-spider/task/task_list.html">查看进度</a></li>
                <li><a href="/nest-spider/spider/edit_spiderinfo.html">模板编辑</a></li>
                <li class="active"><a href="">模板列表</a></li>
                <li><a href="update_data.html">更新数据</a></li>
                <li><a href="/nest-spider/spider/quartz_list.html">定时任务管理</a></li>
            </ul>

        </div><!--/.navbar-collapse -->
    </div>
</div>

<!-- =============== container =================== -->
<div class="container">
    <form class="form-horizontal" id="spiderInfoForm" action="/nest-spider/spiderinfo/listSpiderinfo.do" style="padding:2%;">
        <div class="form-group col-xs-4">
            <label for="page" class="col-sm-4 control-label white">页码：</label>
            <div class="col-sm-6">
                <input type="number" class="form-control" id="page" name="page" value="${page}" required>
            </div>
        </div>
        <div class="form-group col-xs-5">
            <label for="domain" class="col-sm-4 control-label white">域名：</label>
            <div class="col-sm-7">
                <input type="text" class="form-control" id="domain" name="domain" value="${domain}" required>
            </div>
        </div>
        <button type="submit" class="btn btn-default">搜索</button>
    </form>
</div>

<div style="padding-left: 0%; margin-left: 8%">
    <button type="button" onclick="start_checked()">启动选中</button>
    <button type="button" onclick="checkAll()">全选</button>
</div>

<div class="container">
    <table class="table table-hover">
        <thead class="thead-inverse">
        <tr class="white">
            <th>#</th>
            <th>网站域名</th>
            <th>网站名称</th>
            <th>查看数据</th>
            <th>编辑</th>
            <th>删除</th>
            <th>定时任务</th>
        </tr>
        </thead>
        <tbody class="grey">
        <c:forEach items="${spiderInfoList}" var="info" varStatus="index">
           <tr>
              <th><label>
                <input type="checkbox" data-infoid="${info.id}">${index.count}
              </label></th>
              <td>${info.domain}</td>
              <td>${info.siteName}</td>
              <td></td>
              <td>
                <a class="btn btn-info"
                   href="/nest-spider/spiderinfo/editSpiderInfo.do?spiderInfoId=${info.id}">编辑</a>
              </td>
              <td>
                <button onclick="rpcAndShowData('/nest-spider/spiderinfo/delById.do',{id:'${info.id}'})"
                        class="btn btn-danger">
                    删除
                </button>
              </td>
              <td>
                <a href="/nest-spider/quartz/createQuartz.do?infoId=${info.id}"
                   class="btn btn-default">创建定时任务</a>
              </td>
           </tr>
        </c:forEach>
        
        </tbody>
    </table>
</div>

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
<script src="../js/my.js"></script>
<script type="text/javascript">

    function checkAll() {
        $('input:checkbox').each(function () {
            $(this).attr('checked', true);
        })
    }

    function start_checked() {
        var idList = [];
        $('input:checkbox:checked').each(function () {
            idList.push($(this).attr('data-infoid'));
        });
        rpcAndShowData('/nest-spider/spider/startAll', {spiderInfoIdList : idList.join(',')});
    }
</script>
</body>
</html>