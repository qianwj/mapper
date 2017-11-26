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

    <title>Nest -- 搜索</title>

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

<div class="modal fade" id="myModal" tabindex="-1" role="dialog" style="overflow:scroll">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                    <span class="sr-only">Close</span>
                </button>
                <h4 class="modal-title" id="modalTitle"></h4>
            </div>
            <div class="modal-body" id="modalBody">

            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-secondary" data-dismiss="modal">Close</button>
            </div>
        </div><!-- /.modal-content -->
    </div><!-- /.modal-dialog -->
</div>
<!-- ================================== nav bar ======================================== -->
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
                <li class="active"><a href="">搜索</a></li>
                <li><a href="/nest-spider/webpage/domain_list.html">网站列表</a></li>
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
    <form class="form-horizontal" id="webpageForm" action="/nest-spider/webpage/list.do">
        <div class="form-group col-xs-4">
            <label for="query" class="col-sm-4 control-label white">关键词：</label>
            <div class="col-sm-6">
                <input type="text" class="form-control" id="query" name="query" value="${query}" required>
            </div>
        </div>
        <div class="form-group col-xs-5">
            <label for="domain" class="col-sm-4 control-label white">域名<span style="color: red;">(支持模糊)</span>：</label>
            <div class="col-sm-7">
                <input type="text" class="form-control" id="domain" name="domain" value="${domain}" required>
            </div>
        </div>
        <div class="col-md-3">
            <button type="submit" class="btn btn-primary" id="priceSubmit">搜索</button>
            <a href="javascript:void(0);" class="btn btn-danger" onclick="clearForm();">重置</a>
        </div>
    </form>
    <table class="table table-border table-hover">
        <thead class="thead-inverse white">
        <tr>
            <th>#</th>
            <th>标题</th>
            <th>网站</th>
            <th>时间</th>
            <th>查看</th>
            <th>转到</th>
            <th>删除</th>
        </tr>
        </thead>
        <tbody class="grey">
        <c:if test="${resultList ne null}">
           <c:forEach items="${resultList}" var="webpage" varStatus="index">
               <tr>
            	<th>${index.count}</th>
            	<td>${webpage.title}</td>
            	<td>${webpage.domain}</td>
            	<td>${webpage.gathertime}</td>
            	<td>
                	<button onclick="show_detail('${webpage.id}')" class="btn btn-info">Show</button>
            	</td>
            	<td>
                	<a href="../show/show_webpage.html?id=${webpage.id}" class="btn btn-primary" target="_blank">Go</a>
            	</td>
            	<td>
                	<button onclick="rpcAndShowData('#delWebpage',{id:'${webpage.id}'})" class="btn btn-danger"> 删除</button>
            	</td>
        </tr>
           </c:forEach>
        </c:if>
        </tbody>
    </table>
    
    <c:if test="${tp ne null}"> 
        <div class="container grey">
        	<div class="pull-left">
            	<div class="" id="">&nbsp;&nbsp;共 ${tp.totalRow }条记录/共${tp.pageCount }页</div>
        	</div>
    	</div>

    	<div class="text-center">
        	<div class="" id="">
            	<ul class="pagination">
            		<c:if test="${tp.currentPage ne 1 }">
            			<li class=""><a href="?page=1${tp.otherParam }">首页</a></li>
            			<c:if test="${tp.pageCount ge 3 }">
            				<li class=""><a href="?page=${tp.currentPage-1 }${tp.otherParam }"><span aria-hidden="true">&laquo;</span></a></li>
            			</c:if>
            			<c:if test="${tp.pageCount le (3-1) }">
            				<li class="disabled"><a href="#"><span aria-hidden="true">&laquo;</span></a></li>
            			</c:if>
            		</c:if>
                	<c:if test="${tp.currentPage eq 1 }">
                		<li class="disabled"><a href="#">首页</a></li>
						<li class="disabled"><a href="#"><span aria-hidden="true">&laquo;</span></a></li>
                	</c:if>
                	<c:forEach begin="${tp.pageRange[0] }" end="${tp.pageRange[1] }" step="1" varStatus="pIndex" >
                		<c:if test="${tp.currentPage eq pIndex.index }">
                			<li class="active"><a href="?page=${pIndex.index }${tp.otherParam }">${pIndex.index }</a></li>
                		</c:if>
                		<c:if test="${tp.currentPage ne pIndex.index }">
                			<li><a href="?page=${pIndex.index }${tp.otherParam }">${pIndex.index }</a></li>
                		</c:if>
                	</c:forEach>
                	<c:if test="${tp.currentPage ne tp.pageCount }">
                		<c:if test="${tp.pageCount ge 3 }">
                			<li><a href="?page=${tp.currentPage + 1 }${tp.otherParam }"><span aria-hidden="true">&raquo;</span></a></li>
                		</c:if>
                		<c:if test="${tp.pageCount le (3-1) }">
                			<li class="disabled"><a href="#"><span aria-hidden="true">&raquo;</span></a></li>
                		</c:if>
                		<li><a href="?page=${tp.pageCount }${tp.otherParam }">末页</a></li>
                	</c:if>
                	<c:if test="${tp.currentPage eq tp.pageCount }">
						<li class="disabled"><a href="#"><span aria-hidden="true">&raquo;</span></a></li>
						<li class="disabled"><a href="#">末页</a></li>
					</c:if>
            	</ul>
        	</div>
    	</div>
    </c:if>
</div>

<div class="container navbar-static-bottom">
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
    function show_detail(id) {
        rpc('/nest-spider/webpage/findWebpage.do', {id : id}, function (data) {
            $('#modalTitle').text(data.result.title);
            var body = $("#modalBody");
            body.html('');
            body.append('<h4>正文</h4>\n' +
                '        <p>' + data.result.content + '</p>\n' +
                '        <h4>关键词</h4>');
            if(data.result.keywords != undefined)
                $.each(data.result.keywords, function (i, item) {
                    body.append(item + ', ')
                });
            body.append('<h4>摘要</h4>');
            if(data.result.summary != undefined)
                body.append('<p>' + data.result.summary.join(' ,') + '</p>');
            body.append('<h4>发布时间</h4>\n' +
                '        <span>' + data.result.publishTime + '</span>\n' +
                '        <h4>动态字段</h4>');
            if (data.result.dynamicFields != undefined) {
                $.each(data.result.dynamicFields, function (k, v) {
                    modalBody.append("<p>" + k + " : " + v + "</p>");
                });
            }
            $('#myModal').modal('show');
        })
    }

    function clearForm(){
        $("#query").val(null);
        $("#domain").val(null);
    }
</script>
</body>
</html>