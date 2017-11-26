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

    <title>Nest -- 查看进度</title>

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

<div id="taskListModal" class="modal fade" tabindex="-1" role="dialog" style="overflow:scroll">
    <div class="modal-dialog modal-lg" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                    <span class="sr-only">Close</span>
                </button>
                <h4 class="modal-title">任务状态列表</h4>
            </div>
            <div class="modal-body">
                <table class="table">
                    <thead class="thead-inverse">
                    <tr>
                        <th>时间</th>
                        <th>状态</th>
                    </tr>
                    </thead>
                    <tbody id="taskListTableBody">
                    </tbody>
                </table>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-secondary" data-dismiss="modal">Close</button>
            </div>
        </div><!-- /.modal-content -->
    </div><!-- /.modal-dialog -->
</div>

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
                <li class="active"><a href="">查看进度</a></li>
                <li><a href="/nest-spider/spider/edit_spiderinfo.html">模板编辑</a></li>
                <li><a href="/nest-spider/spiderinfo/spiderinfo_list.html">模板列表</a></li>
                <li><a href="update_data.html">更新数据</a></li>
                <li><a href="/nest-spider/spider/quartz_list.html">定时任务管理</a></li>
            </ul>
        </div><!--/.navbar-collapse -->
    </div>
</div>

<div class="container">
    <c:if test="${result == null }">
    <div class="alert alert-default">
       <p>当前没有任务存在</p>
    </div>
    </c:if>
    <c:if test="${result != null }">
    <div class="alert alert-success macarons" role="alert">
        <strong>抓取任务数:</strong>${result.count},
        <strong>运行任务数:</strong>${count}
        <button onclick="rpcAndShowData('#delAll')" class="btn btn-danger" style="margin-left: 40%">删除全部已停止爬虫</button>
    </div>
    <table class="table table-hover">
        <thead class="thead-inverse">
        <tr class="white">
            <th>任务ID</th>
            <th>#</th>
            <th>任务名称</th>
            <th>已抓取数量</th>
            <th>抓取状态</th>
            <th>抓取状态</th>
            <th>查看详情</th>
            <th>编辑模板</th>
            <th>查看数据</th>
            <th>停止</th>
            <th>删除</th>
            <th>导出数据</th>
        </tr>
        </thead>
        <tbody class="grey">
        <c:forEach items="${result.res }" var="task" varStatus="index">
        <tr>
            <th scope="row">${task.taskId}</th>
            <th scope="row">${index.count}</th>
            <td>${task.name}</td>
            <td>${task.count}</td>
            <td>${task.state}</td>
            <td>
                <button onclick="showTable('${task.taskId}')" class="btn btn-info">查看状态</button>
            </td>
            <td>
                <a href="/nest-spider/task/findTask.do?taskId=${task.taskId}"
                   class="btn btn-warning" target="_blank">查看详情</a>
            </td>
            <c:if test="${task.state == 'RUNNING'}">
            <td>
                <button onclick="rpcAndShowData('/nest-spider/spider/stop.do',{uuid:'${task.taskId}'})"
                        class="btn btn-danger">停止
                </button>
            </td>
            <td><a class="btn btn-danger disabled">正在抓取</a></td>
            </c:if>
            <c:if test="${task.state == 'STOP'}">
            <td>
                <a class="btn btn-danger disabled">停止</a>
            </td>
            <td>
                <button onclick="rpcAndShowData('/nest-spider/task/delTask.do',{uuid:'${task.taskId}'})"
                        class="btn btn-danger" >删除
                </button>
            </td>
            </c:if>
            <c:if test="${spiderInfoList.get(index.index) != \"null\"}">
            <td>
                <form action="/nest-spider/spider/importSpiderInfo.do" method="post"
                      target="_blank">
                    <input hidden name="spiderInfoJson" value="${spiderInfoList.get(index.index)}">
                    <button class="btn btn-primary" type="submit">编辑模板</button>
                </form>
            </td>
            <td>
                <a href="/nest-spider/webpage/list.do?domain=${task.name}" class="btn btn-primary" target="_blank">查看数据</a>
            </td>
            <td>
                <div class="btn-group dropdown">
                    <button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown"
                            aria-haspopup="true" aria-expanded="false">
                        导出数据<span class="caret"></span>
                    </button>
                    <div class="dropdown-menu">
                        <li href="/nest-spider/webpage/exportTitleContentPairBySpiderUUID.do?uuid=${task.taskId}"><a>下载标题正文对</a></li>
                        <li href="/nest-spider/webpage/exportWebpageJSONBySpiderUUID.do?uuid=${task.taskId}"><a>下载JSON</a></li>
                    </div>
                </div>
            </td>
            </c:if>
            <c:if test="${spiderInfoList.get(index.index) == \"null\"}">
            <td>
                <button class="btn btn-primary disabled">编辑模板</button>
            </td>
            <td>
                <a class="btn btn-primary disabled" target="_blank">查看数据</a>
            </td>
            </c:if>
        </tr>
        </c:forEach>
        </tbody>
    </table>
    </c:if>
</div>

<div class="container navbar-fixed-bottom">
    <footer style="color:white">
        <p class="pull-right"><a href="search_list.html#" style="color:inherit">Back to top</a></p>
        <p style="color:inherit">© 2017 Company, Inc. · <a href="#Nest-Tech" style="color:inherit">Nest Technology</a></p>
    </footer>
</div>

<!-- Bootstrap core JavaScript
    ================================================== -->
<!-- Placed at the end of the document so the pages load faster -->
<script src="../js/jquery.min.js"></script>
<script src="../js/bootstrap.min.js"></script>
<script src="../js/jquery.validate.min.js"></script>
<script src="../js/basic.js"></script>
<script type="text/javascript">
    function showTable(taskId) {
        rpc('#findTask', {taskId : taskId, extra : false}, function (data) {
            $('#taskListTableBody').html('');
            $.each(data.result.descriptions, function (i, item) {
                $('<tr style="word-break:break-all; word-wrap:break-word;"><th scope="row">' + k + '</th><td>' + v + '</td></tr>').appendTo("#taskListTableBody");
            })
            $('#taskListModal').modal('show');
        })
    }
</script>
</body>
</html>