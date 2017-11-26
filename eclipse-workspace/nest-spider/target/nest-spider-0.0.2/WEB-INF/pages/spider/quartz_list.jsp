<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html lang="zh-CN">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="description" content="">
    <meta name="author" content="Qian Wenjin">
    <link rel="shortcut icon" href="#">

    <title>Nest -- 定时任务管理</title>

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
                <li><a href="/nest-spider/spiderinfo/spiderinfo_list.html">模板列表</a></li>
                <li><a href="#">更新数据</a></li>
                <li class="active"><a href="">定时任务管理</a></li>
            </ul>

        </div><!--/.navbar-collapse -->
    </div>
</div>

<div class="container">
    <div class="jumbotron macarons">
        <p>创建定时任务</p>
        <form class="form-horizontal" id="quartzForm" action="/nest-spider/quartz/createQuartz.do" style="font-size: 16px;">
            <div class="form-group col-xs-5">
                <label for="spiderInfoId" class="col-sm-5 control-label white">爬虫模板ID：</label>
                <div class="col-sm-6">
                    <input type="text" class="form-control" id="infoId" name="infoId" value="${infoId}" placeholder="输入已注册的爬虫模板ID" required>
                </div>
            </div>
            <div class="form-group col-xs-5">
                <label for="interval" class="col-sm-5 control-label white">循环间隔小时数：</label>
                <div class="col-sm-6">
                    <input type="number" class="form-control" id="interval" name="interval" value="${interval}" placeholder="输入循环间隔小时数" required>
                </div>
            </div>
            <div class="col-md-2">
                <button type="submit" class="btn btn-primary">提交</button>
                <a href="javascript:void(0);" class="btn btn-danger" onclick="clearForm();">重置</a>
            </div>
        </form>
    </div>

    <table class="table table-hover" id="jobTable" style="display:none">
        <thead class="white">
        <tr>
            <th>网站名称</th>
            <th>上次执行时间</th>
            <th>下次执行时间</th>
            <th>创建时间</th>
            <th>删除任务</th>
        </tr>
        </thead>
        <tbody class="grey">
        </tbody>
    </table>
</div>

<div class="container navbar-fixed-bottom">
    <footer style="color:white">
        <p class="pull-right"><a href="search_list.html#" style="color:inherit">Back to top</a></p>
        <p style="color:inherit">© 2017 Company, Inc. · <a href="#Nest-Tech" style="color:inherit">Nest Technology Create by Software Engineer 1517 Qian Wenjin</a></p>
    </footer>
</div>

<script src="../js/jquery.min.js"></script>
<script src="../js/bootstrap.js"></script>
<script>
    $.ready(function () {
    	$.get('/nest-spider/quartz/listAll.do', function(data) {
    		if(data == null)
    			return;
    		else
    			$('#jobTable').show();
    		$.each(data, function(i, item){
                $('#jobTable').append('<tr>' +
                    '<td>' + item.value.left.siteName + '</td>' +
                    '<td>' + item.value.right.previousFireTime + '</td>' +
                    '<td>' + item.value.right.nextFireTime + '</td>' +
                    '<td>' + item.value.right.startTime + '</td>' +
                    '<td>' +
                    '   <button onclick="delQuartzJob(' + item.value +')" class="am-btn am-btn-default">删除定时任务</button>' +
                    '</td>' +
                    '</tr>');
            });
    	});
        $('#quartzForm').validate({
            rules : {
               remote : "#checkQuartzJob"
            }, highlight: function (element) {
                $(element).closest('.form-group').addClass('has-error');
            }, success: function (label) {
                label.closest('.form-group').removeClass('has-error');
                label.remove();
            }, errorPlacement: function (error, element) {
                element.parent('div').append(error);
            }
        })
    })

    function clearForm(){
        $("#infoId").val(null);
        $("#interval").val(null);
    }

    function delQuartzJob(infoId) {
        var cc = confirm("是否要删除定时任务");
        if (cc) {
            $.get('#removeQuartzJob', {infoId: infoId}, function () {
                alert("删除定时任务成功");
            });
        }
    }
</script>
</body>
</html>