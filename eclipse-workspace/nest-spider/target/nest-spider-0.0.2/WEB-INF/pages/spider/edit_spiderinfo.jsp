<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html lang="zh-CN">
<head>
    <meta charset="utf-8">
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="description" content="">
    <meta name="author" content="Qian Wenjin">
    <link rel="shortcut icon" href="#">

    <title>Nest -- 模板编辑</title>

    <!-- Bootstrap core CSS -->
    <link href="../css/bootstrap.css" rel="stylesheet">
    <link href="../css/bootstrap.min.css" rel="stylesheet">

    <!-- Custom styles for this template -->
    <link href="../css/offcanvas.css" rel="stylesheet">

    <!-- HTML5 shim and Respond.js IE8 support of HTML5 elements and media queries -->
    <!--[if lt IE 9]>
    <script src="http://cdn.bootcss.com/html5shiv/3.7.0/html5shiv.min.js"></script>
    <script src="http://cdn.bootcss.com/respond.js/1.3.0/respond.min.js"></script>
    <![endif]-->
</head>
<body background="../imgs/background/timg.jpeg">

<!-- ===================================== modal ============================================= -->

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

<div class="modal fade" id="inputModal" tabindex="-1" role="dialog" style="overflow:scroll">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                    <span class="sr-only">Close</span>
                </button>
                <h4 class="modal-title" id="inputModalTitle"></h4>
            </div>
            <div class="modal-body" id="inputModalBody">
                <div class="form-group">
                    <label for="data"></label>
                    <input type="text" class="form-control" id="data" name="data" placeholder="data">
                </div>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-secondary" data-dismiss="modal" id="cancelInputButton">取消</button>
                <button type="button" class="btn btn-primary" id="confirmInputButton" onclick="showSettings();">确定</button>
            </div>
        </div><!-- /.modal-content -->
    </div><!-- /.modal-dialog -->
</div>

<div class="modal fade" id="tableModal" tabindex="-1" role="dialog" style="overflow:scroll">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                    <span class="sr-only">Close</span>
                </button>
                <h4 class="modal-title" id="tableModalTitle"></h4>
            </div>
            <div class="modal-body">
                <table class="table table-hover">
                    <tbody id="tableModalBody">
                    <thead>
                    <tr>
                        <th>字段名称</th>
                        <th>字段值</th>
                    </tr>
                    </thead>
                    </tbody>
                </table>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-secondary" data-dismiss="modal">取消</button>
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
            <a class="navbar-brand" href="../../index.html">Nest Spider</a>
        </div>
        <div class="navbar-collapse collapse">
            <ul class="nav navbar-nav">
                <li><a href="/nest-spider/webpage/search_list.html">搜索</a></li>
                <li><a href="/nest-spider/webpage/domain_list.html">网站列表</a></li>
                <li><a href="/nest-spider/task/task_list.html">查看进度</a></li>
                <li class="active"><a href="">模板编辑</a></li>
                <li><a href="/nest-spider/spiderinfo/spiderinfo_list.html">模板列表</a></li>
                <li><a href="#">更新数据</a></li>
                <li><a href="/nest-spider/spider/quartz_list.html">定时任务管理</a></li>
            </ul>

        </div><!--/.navbar-collapse -->
    </div>
</div>

<!-- ============================= container ==================================== -->
<div class="container">
    <div class="jumbotron col-xs-4 col-sm-5" style="margin-left:1.5%;background-color:rgba(220,220,220,0.74)">
        <form id="spiderInfoForm" role="form" method="post" action="/nest-spider/spiderinfo/testSpiderInfo.do">
            <div class="form-group">
                <label for="siteName">Site</label>
                <input type="text" class="form-control" id="siteName" name="siteName" value="${spiderInfo.siteName}" placeholder="网站名称" required>
            </div>
            <div class="form-group">
                <label for="domain">Domain</label>
                <input type="text" class="form-control" id="domain" name="domain" value="${spiderInfo.domain}" placeholder="网站域名" onblur="onDomainBlur();" required>
            </div>
            <div class="form-group">
                <label for="startUrl">URL</label>
                <input type="text" class="form-control" id="startUrl" name="startUrl" placeholder="起始链接" required>
            </div>
            <p style="font-size:16px;">多个起始地址请使用JSON格式书写,例如['http://news.qq.com','http://news.qq.com/dfs/dfs.fg']</p>
          
            <!-- ================  advance settings ================== -->
            <div class="modal fade" id="advance_settings" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
                <div class="modal-dialog" role="document">
                    <div class="modal-content">
                        <div class="modal-header">
                            <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                            <h4 class="modal-title">高级配置选项</h4>
                        </div>
                        <div class="modal-body">
                            <div class="form-group">
                                <label for="id">ID</label>
                                <input type="text" class="form-control" id="id" name="id" placeholder="模板ID,系统自动填充,请勿手工赋值"
                                       value="${spiderInfo.id}">
                            </div>
                            <div class="form-group">
                                <label for="thread">Thread</label>
                                <input type="number" class="form-control" id="thread" name="thread" placeholder="抓取线程数"
                                       value="${spiderInfo.thread}">
                            </div>
                            <div class="form-group">
                                <label for="retry">Retry</label>
                                <input type="number" class="form-control" id="retry" name="retry" placeholder="失败的网页重试次数"
                                       value="${spiderInfo.retry}">
                            </div>
                            <div class="form-group">
                                <label for="sleep">Sleep</label>
                                <input type="number" class="form-control" id="sleep" name="sleep" placeholder="抓取每个网页睡眠时间"
                                       value="${spiderInfo.sleep}">
                            </div>
                            <div class="form-group">
                                <label for="maxPageGather">Max Pages</label>
                                <input type="number" class="form-control" id="maxPageGather" name="maxPageGather"
                                       placeholder="最大抓取网页数量,0代表不限制" value="${spiderInfo.maxPageGather}">
                                
                            </div>
                            <p><sub>请在导出模板前将最大抓取数量设置为生产环境中正确的值</sub></p>
                            <div class="form-group">
                                <label for="timeout">Timeout</label>
                                <input type="number" class="form-control" id="timeout" name="timeout" placeholder="HTTP链接超时时间"
                                       value="${spiderInfo.timeout}">
                            </div>
                            <div class="form-group">
                                <label for="contentReg">Content RegEx</label>
                                <input type="text" class="form-control" id="contentReg" name="contentReg" placeholder="正文正则表达式"
                                       value="${spiderInfo.contentReg}">
                            </div>
                            <div class="form-group">
                                <label for="contentXPath">Content XPath</label>
                                <input type="text" class="form-control" id="contentXPath" name="contentXPath"
                                       placeholder="正文Xpath"
                                       value="${spiderInfo.contentXPath}">
                            </div>
                            <div class="form-group">
                                <label for="titleReg">Title RegEx</label>
                                <input type="text" class="form-control" id="titleReg" name="titleReg" placeholder="标题正则"
                                       value="${spiderInfo.titleReg}">
                            </div>
                            <div class="form-group">
                                <label for="titleXPath">Title XPath</label>
                                <input type="text" class="form-control" id="titleXPath" name="titleXPath" placeholder="标题xpath"
                                       value="${spiderInfo.titleXPath}">
                            </div>
                            <div class="form-group">
                                <label for="categoryReg">Category RegEx</label>
                                <input type="text" class="form-control" id="categoryReg" name="categoryReg" placeholder="分类信息正则"
                                       value="${spiderInfo.categoryReg}">
                            </div>
                            <div class="form-group">
                                <label for="categoryXPath">Category XPath</label>
                                <input type="text" class="form-control" id="categoryXPath" name="categoryXPath"
                                       placeholder="分类信息XPath" value="${spiderInfo.categoryXPath}">
                            </div>
                            <div class="form-group">
                                <label for="defaultCategory">Default Category</label>
                                <input type="text" class="form-control" id="defaultCategory" name="defaultCategory"
                                       placeholder="默认分类" value="${spiderInfo.defaultCategory}">
                            </div>
                            <div class="form-group">
                                <label for="urlReg">URL RegEx</label>
                                <input type="text" class="form-control" id="urlReg" name="urlReg" placeholder="url正则"
                                       value="${spiderInfo.urlReg}">
                            </div>
                            <div class="form-group">
                                <label for="charset">Charset</label>
                                <input type="text" class="form-control" id="charset" name="charset" placeholder="编码"
                                       value="${spiderInfo.charset}">
                            </div>
                            <div class="form-group">
                                <label for="publishTimeReg">Publish Time RegEx</label>
                                <input type="text" class="form-control" id="publishTimeReg" name="publishTimeReg"
                                       placeholder="发布时间正则" value="${spiderInfo.publishTimeReg}">
                            </div>
                            <div class="form-group">
                                <label for="publishTimeXPath">Publish Time XPath</label>
                                <input type="text" class="form-control" id="publishTimeXPath" name="publishTimeXPath"
                                       placeholder="发布时间xpath" value="${spiderInfo.publishTimeXPath}">
                            </div>
                            <div class="form-group">
                                <label for="publishTimeFormat">Publish Time Format</label>
                                <input type="text" class="form-control" id="publishTimeFormat" name="publishTimeFormat"
                                       placeholder="发布时间模板" value="${spiderInfo.publishTimeFormat}">
                            </div>
                            
                            <%--动态字段--%>
                            <div class="form-group" id="dynamicFields">
                                <button type="button" onclick="addDynamicField();" class="btn btn-info">添加动态字段</button>
                            	<c:forEach items="${spiderInfo.dynamicFields}" var="field" varStatus="index">
                            	    <div id="dynamicField${index.count}" class="dynamicField" name="${field.name}">
                                		<button class="btn btn-danger" type="button" onclick="$('#dynamicField${index.count}').remove();">删除动态字段${field.name}</button>
                                    	<div class="form-group">
                                    		<label for="${field.name}Regex"><span class="label label-warning">动态</span>${field.name} RegEx</label>
                                    		<input type="text" class="form-control" id="${field.name}Reg" name="${field.name}Reg" placeholder="${field.name}正则" value="${field.regex}">
                                		</div>
                                		<div class="form-group">
                                    		<label for="${field.name}XPath"><span class="label label-warning">动态</span>${field.name} XPath</label>
                                    		<input type="text" class="form-control" id="${field.name}XPath" name="${field.name}XPath" placeholder="${field.name}XPath" value="${field.xpath}">
                                		</div>
                                		<div class="checkbox">
                                    		<label>
                                        		<c:if test="${field.need}">
                                            		<input type="checkbox" name="need${field.name}" id="need${field.name}" checked="checked">
                                        		</c:if>
                                        		<c:if test="${!field.need}">
                                            		<input type="checkbox" name="need${field.name}" id="need${field.name}">
                                        		</c:if> 是否网页必须有${field.name}
                                    		</label>
                                		</div>
                            		</div>
                            	</c:forEach>
                            </div>
                            
                            <%--静态字段 --%>
                            <div class="form-group" id="staticFields">
                                <button type="button" onclick="addStaticField();" class="btn btn-info">添加静态字段</button>
                            	<c:forEach items="${spiderInfo.staticFields}" var="field" varStatus="index">
                            		<div id="staticField${index.count}" class="staticField" name="${field.name}">
                                		<button class="btn btn-danger" type="button" onclick="$('#staticField${index.count}').remove();">删除静态字段${field.name}</button>
                                		<div class="form-group">
                                    		<label for="static-${field.name}-value"><span class="label label-warning">静态</span>${field.name} Value</label>
                                    		<input type="text" class="form-control" id="static-${field.name}-value" name="${field.name}-value" placeholder="${field.name}value" value="${field.value}">
                               		 	</div>
                            		</div>
                        		</c:forEach>
                            </div>
                            <div class="form-group">
                                <label for="lang">Language</label>
                                <input type="text" class="form-control" id="lang" name="lang"
                                       placeholder="发布时间模板语言" value="${spiderInfo.lang}">
                            </div>
                            <div class="form-group">
                                <label for="country">Country</label>
                                <input type="text" class="form-control" id="country" name="country"
                                       placeholder="发布时间模板国家" value="${spiderInfo.country}">
                            </div>
                            <div class="form-group">
                                <label for="callbackURL">Callback URL</label>
                                <input type="text" class="form-control" id="callbackUrl" name="callbackUrl" placeholder="回调url(多个回调地址请使用JSON格式书写)"
                                       value="${spiderInfo.callbackUrl}">
                            </div>
                            <div class="form-group">
                                <label for="userAgent">User Agent</label>
                                <input type="text" class="form-control" id="userAgent" name="userAgent" placeholder="userAgent"
                                       value="${spiderInfo.userAgent}">
                            </div>
                            <div class="form-group">
                                <label for="proxyHost">Proxy Host</label>
                                <input type="text" class="form-control" id="proxyHost" name="proxyHost"
                                       placeholder="proxyHost"
                                       value="${spiderInfo.proxyHost}">
                            </div>
                            <div class="form-group">
                                <label for="proxyPort">Proxy Port</label>
                                <input type="number" class="form-control" id="proxyPort" name="proxyPort"
                                       placeholder="proxyPort"
                                       value="${spiderInfo.proxyPort}">
                            </div>
                            <div class="form-group">
                                <label for="proxyUsername">Proxy Username</label>
                                <input type="text" class="form-control" id="proxyUsername" name="proxyUsername"
                                       placeholder="proxyUsername"
                                       value="${spiderInfo.proxyUsername}">
                            </div>
                            <div class="form-group">
                                <label for="proxyPassword">Proxy Password</label>
                                <input type="text" class="form-control" id="proxyPassword" name="proxyPassword"
                                       placeholder="proxyPassword"
                                       value="${spiderInfo.proxyPassword}">
                            </div>
                            
                            <div class="form-group">
                                <div class="checkbox">
                                    <label>
                                        <c:if test="${spiderInfo.doNLP}">
                                    		<input type="checkbox" name="doNLP" id="doNLP" checked="checked">
                                		</c:if>
                                		<c:if test="${!spiderInfo.doNLP}">
                                    		<input type="checkbox" name="doNLP" id="doNLP">
                                		</c:if>是否进行nlp处理
                                    </label>
                                    <p><sub>包括摘要， 关键词， 命名实体的抽取</sub></p>
                                </div>
                            </div>
                            <div class="form-group">
                                <div class="checkbox">
                                    <label>
                                        <c:if test="${spiderInfo.gatherFirstPage}">
                                    		<input type="checkbox" name="gatherFirstPage" id="gatherFirstPage" checked="checked">
                                		</c:if>
                                		<c:if test="${!spiderInfo.gatherFirstPage}">
                                    		<input type="checkbox" name="gatherFirstPage" id="gatherFirstPage">
                                		</c:if>是否只抓取首页
                                    </label>
                                </div>
                            </div>
                            <div class="form-group">
                                <div class="checkbox">
                                    <label>
                                        <c:if test="${spiderInfo.needTitle}">
                                    		<input type="checkbox" name="needTitle" id="needTitle" checked="checked">
                                		</c:if>
                                		<c:if test="${!spiderInfo.needTitle}">
                                    		<input type="checkbox" name="needTitle" id="needTitle">
                                		</c:if> 网页是否必须有标题
                                    </label>
                                </div>
                            </div>
                            <div class="form-group">
                                <div class="checkbox">
                                    <label>
                                        <c:if test="${spiderInfo.needContent}">
                                    		<input type="checkbox" name="needContent" id="needContent" checked="checked">
                                		</c:if>
                                		<c:if test="${!spiderInfo.needContent}">
                                    		<input type="checkbox" name="needContent" id="needContent">
                                		</c:if> 网页是否必须有正文
                                    </label>
                                </div>
                            </div>
                            <div class="form-group">
                                <div class="checkbox">
                                    <label>
                                        <c:if test="${spiderInfo.needPublishTime}">
                                    		<input type="checkbox" name="needPublishTime" id="needPublishTime" checked="checked">
                                		</c:if>
                                		<c:if test="${!spiderInfo.needPublishTime}">
                                    		<input type="checkbox" name="needPublishTime" id="needPublishTime">
                                		</c:if> 网页是否必须有发布时间
                                    </label>
                                </div>
                            </div>
                            <div class="form-group">
                                <div class="checkbox">
                                    <label>
                                        <c:if test="${spiderInfo.saveCapture}">
                                    		<input type="checkbox" name="saveCapture" id="saveCapture" checked="checked">
                                		</c:if>
                                		<c:if test="${!spiderInfo.saveCapture}">
                                    		<input type="checkbox" name="saveCapture" id="saveCapture">
                                		</c:if> 是否保存网页快照
                                    </label>
                                </div>
                            </div>
                            <div class="form-group">
                                <div class="checkbox">
                                    <label>
                                        <c:if test="${spiderInfo.ajaxSite}">
                                    		<input type="checkbox" name="ajaxSite" id="ajaxSite" checked="checked">
                                		</c:if>
                                		<c:if test="${!spiderInfo.ajaxSite}">
                                    		<input type="checkbox" name="ajaxSite" id="ajaxSite">
                                		</c:if> 网页是否经过Ajax渲染
                                    </label>
                                </div>
                            </div>
                            <div class="form-group">
                                <div class="checkbox">
                                    <label>
                                        <c:if test="${spiderInfo.autoDetectPublishDate}">
                                			<input type="checkbox" name="autoDetectPublishDate" id="autoDetectPublishDate" checked="checked">
                            			</c:if>
                            			<c:if test="${!spiderInfo.autoDetectPublishDate}">
                                			<input type="checkbox" name="autoDetectPublishDate" id="autoDetectPublishDate">
                            			</c:if> 是否自动探测发布日期
                                    </label>
                                    <p><sub>尽量手工配置,自动探测费时,默认关闭</sub></p>
                                </div>
                            </div>
                        </div>
                        <div class="modal-footer">
                            <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
                        </div>
                    </div><!-- /.modal-content -->
                </div><!-- /.modal-dialog -->
            </div><!-- /.modal -->
            <button type="submit" class="btn btn-info">抓取样例数据</button>
            <button type="button" class="btn btn-primary" onclick="exportJson()">导出模板</button>
            <button type="button" class="btn btn-danger" onclick="submit_task()">提交抓取任务</button>
            <button type="button" class="btn btn-warning" onclick="save()">存储此模板</button>
            <button type="button" class="btn btn-primary" data-toggle="modal" data-target="#advance_settings">
                显示高级配置
            </button>
        </form>
    </div>
    <div class="jumbotron col-xs-3 col-sm-6" style="margin-left:6.5%;background-color:rgba(220,220,220,0.74)">
        <div class="container">
            <form id="import" action="/nest-spider/spiderinfo/importSpiderInfo.do" method="post">
                <fieldset class="panel panel-primary form-group">
                    <div class="panel-heading">
                        <label for="spiderInfoJson">Json爬虫模板</label>
                    </div>
                    <div class="panel-body">
                        <textarea class="form-control" id="spiderInfoJson" rows="10" name="spiderInfoJson">${spiderInfoJson}</textarea>
                    </div>
                </fieldset>
                <button type="submit" class="btn btn-warning">自动填充</button>
            </form>
        </div>
    </div>

    <div class="container" style="margin-top: 50%">
        <div class="alert alert-success" style="display: none;" role="alert" id="task_div">
            抓取完成， 抓取任务ID:<span id="task_id"></span>.
            <a class="btn btn-info" id="open-task-detail" target="_blank">查看抓取详情</a>
            <button type="button" class="close" data-dismiss="alert" aria-label="Close">
                <span aria-hidden="true">&times;</span>
                <span class="sr-only">Close</span>
            </button>
        </div>
        <div class="row">
            <table class="table table-hover" style="display:none;background-color:rgba(220,220,220,0.74)" id="webpageTable">
                <thead class="thead-inverse">
                <tr>
                    <th>##</th>
                    <th>Title</th>
                    <th>Summary</th>
                    <th>Content</th>
                    <th>PublishTime</th>
                    <th>Keywords</th>
                    <th>Category</th>
                    <th>Go</th>
                    <th>Full Data</th>
                </tr>
                </thead>
                <tbody id="webpageTableBody">

                </tbody>
            </table>
        </div>
    </div>
</div>

<div class="container">
    <footer style="color:white;text-align: center">
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
<script src="../js/my.js"></script>
<script type="text/javascript">
    $('.row featurette').hover(
        $('.row featurette').css({
            'background-color':'rgba(150,200,200,0.9)',
            'cursor':'pointer'
        })
    );
    
    function showSettings() {
    	$('#advance_settings').modal('show');
    }

    var dynamicFieldList = {};
    $(function () {
        var validate = $('#spiderInfoForm').validate({
            submitHandler : function (form) {
                var result = formJson('spiderInfoForm');
                $('#confirmModalTitle').text('正在抓取, 请稍候');
                $('#confirmModalBody').html(
                    '<div class="progress">\n' +
                    '    <div class="progress-bar progress-bar-striped active" id="progressbar" role="progressbar" aria-valuenow="20" aria-valuemin="0" aria-valuemax="100" style="width: 20%"></div>\n' +
                    '</div>');
                $('#confirmModal').modal({
                    keyboard : false,
                    backdrop : 'static'
                });
                $.getJSON('/nest-spider/spiderinfo/testSpiderInfo.do', {spiderInfoJson: JSON.stringify(result)}, function (data) {
                    $("#webpageTableBody").html("");
                    var modalBody = $("#confirmModalBody");
                    var modalTitle = $("#confirmModalTitle");
                    if (data.count <= 0) {
                        modalTitle.text("Error!");
                        if(data.errorMessage == null)
                        	modalBody.html("无法采集到数据");
                        else
                        	modalBody.html(data.errorMessage);
                        return;
                    }
                    modalTitle.text("Success!");
                    modalBody.html("已抓取数据,正在渲染,请稍后");
                    var webpageTable = $("#webpageTable");
                    webpageTable.show("slow");
                    $('#task_id').html(data.res[0].spiderUUID);
                    $('#open-task-detail').attr('href', '/nest-spider/findTask.do?taskId=' + data.res[0].spiderUUID + "&extra=" + true);
                    $('#task_div').show();
                    var engContentCount = 0, threshold = 0.8;
                    var tempContent = "";
                    $.each(data.res, function (i, item) {
                        if (isEnWebpage(item.content)) {
                            engContentCount++;
                        }
                        if(item.content.length > 50){
                            tempContent = item.content.substring(0, 51) + "····[省略" + (item.content.length - 50) + "字]";
                        }
                        dynamicFieldList[i] = item.dynamicFields;
                        if(item.dynamicFields == null)
                        	$('<tr style="word-break:break-all; word-wrap:break-word;">' +
                                    '<th scope="row">' + (i+1) + '</th>' +
                                    '<td>' + item.title + '</td>' +
                                    '<td>' + item.summary + '</td>' +
                                    '<td>' + tempContent + '</td>' +
                                    '<td>' + item.publishTime + '</td>' +
                                    '<td>' + item.keywords + '</td>' +
                                    '<td>' + item.category + '</td>' +
                                    '<td><a class="btn btn-info" href="' + item.url + '">前往</a></td>' +
                                    '<td></td>' + 
                                    '</tr>')
                                    .appendTo("#webpageTableBody");
                        else
                        	$('<tr style="word-break:break-all; word-wrap:break-word;">' +
                                    '<th scope="row">' + (i+1) + '</th>' +
                                    '<td>' + item.title + '</td>' +
                                    '<td>' + item.summary + '</td>' +
                                    '<td>' + tempContent + '</td>' +
                                    '<td>' + item.publishTime + '</td>' +
                                    '<td>' + item.keywords + '</td>' +
                                    '<td>' + item.category + '</td>' +
                                    '<td><a class="btn btn-info" href="' + item.url + '">前往</a></td>' +
                                    '<td><button class="btn btn-info" onclick="showDynamicFields(' + i + ')">动态字段</button></td>' +
                                    '</tr>')
                                    .appendTo("#webpageTableBody");
                        
                    });
                    if (engContentCount / data.count >= threshold) {
                        $('<span class="label label-pill label-warning">可能是西文网页,请勿使用自动抽取正文</span>').appendTo('#taskId');
                    }
                    setTimeout("$('#confirmModal').modal('hide')", 2000);
                });
            }, rules : {
                thread: {
                    min : 1
                }, retry : {
                    max : 5
                }, maxPageGather : {
                    maxGather : 0
                }, timeout : {
                    min : 1000
                }
            }, highlight: function (element) {
                $(element).closest('.form-group').addClass('has-error');
            }, success: function (label) {
                label.closest('.form-group').removeClass('has-error');
                label.remove();
            }, errorPlacement: function (error, element) {
                element.parent('div').append(error);
            }
        });
    });
    
    window.onload = setInterval("progress()", 500);

    function progress() {
        $.get('/nest-spider/spider/progress.do', function (data) {
            $('#progressbar').css({
                width:(data*100)/$('#maxPageGather').val() + '%'
            })
        });
    }

    //return false则提示
    $.validator.addMethod('maxGather', function(v, e) {
        return v > 0 || $('#gatherFirstPage').prop('checked');
    }, '最大抓取页数与只抓取首页必选其一');

    $.validator.addMethod('domain', function (v, e) {
        return v.indexOf('/') == -1;
    }, '域名不可包含/');

    $.validator.addMethod('json', function (v, e) {
        var urlList, validate = true;
        try {
            urlList = eval(v);
            if(urlList.length < 0)
                return false;
            $.each(urlList, function (i, item) {
                if(item.substring(0, 4)!= 'http')
                    validate = false;
            })
        } catch (exception) {
            return false;
        }
        return validate;
    }, 'url列表必须有至少一个url,必须为json格式,且每个链接必须使用http或者https开头');
    
    function onDomainBlur() {
        var domain = $('#domain').val();
        $('#startUrl').val('[\'http://' + domain + '/\']');
        $.getJSON('/nest-spider/spiderinfo/findByDomain.do', {domain:domain}, function (data) {
            if(data.count > 0) {
               showModal('请确认','服务器中已包含此网站的模板， 是否继续配置',function () {//cancal
                   var result = formJson('spiderInfoForm');
                   localStorage['spiderInfo'] = jsonStringify(result, 0);
                   location.href = '/nest-spider/spiderinfo/listSpiderinfo.do?domain=' + domain;
               }, function () {//confirm
                   $('#confirmModal').modal('hide')
               })
            }
        })
    }
    
    function submit_task() {
        rpcAndShowData('/nest-spider/spider/start.do', {spiderInfoJson:JSON.stringify(formJson('spiderInfoForm'))})
    }

    function save() {
        rpcAndShowData('/nest-spider/spiderinfo/save.do', {spiderInfoJson:JSON.stringify(formJson('spiderInfoForm'))})
    }

    /*
     * form:该表单的ID
     */
    function formJson(form) {
        var result = {};
        var arr = $("#" + form).serializeArray();
        for (var i = 0; i < arr.length; i++) {
            var field = arr[i];
            if (field.name in result)
                result[field.name] += ',' + field.value;
            else
                result[field.name] = field.value;
        }
        result['gatherFirstPage'] = $("#gatherFirstPage").prop('checked');
        result['doNLP'] = $("#doNLP").prop('checked');
        result['needTitle'] = $("#needTitle").prop('checked');
        result['needContent'] = $("#needContent").prop('checked');
        result['needPublishTime'] = $("#needPublishTime").prop('checked');
        result['startUrl'] = eval(result['startUrl']);
        result['callbackUrl'] = eval(result['callbackUrl']);
        result['saveCapture'] = $("#saveCapture").prop('checked');
        result['autoDetectPublishDate'] = $("#autoDetectPublishDate").prop('checked');
        result['ajaxSite'] = $("#ajaxSite").prop('checked');

        var dynamicFields = [];
        var fieldConfigList = $('.dynamicField');
        for(var i = 0;i<fieldConfigList.length;i++) {
            var fieldDom = $('.dynamicField:eq(' + i + ')');
            var fieldId = fieldDom.attr('id');
            var fieldName = fieldDom.attr('name');
            var fieldConfig = {'regex': '', 'xpath': '', 'name': '', 'need': false};
            fieldConfig['name'] = fieldName;
            fieldConfig['regex'] = $('#' + fieldName + 'Reg').val();
            fieldConfig['xpath'] = $('#' + fieldName + 'XPath').val();
            fieldConfig['need'] = $('#need' + fieldName).prop('checked');
            dynamicFields.push(fieldConfig);
        }
        result['dynamicFields'] = dynamicFields;

        var staticFields = [];
        var staticFiledDomList = $('.staticField');
        for (i = 0; i < staticFiledDomList.length; i++) {
            var staticFieldDom = $('.staticField:eq(' + i + ')');
            var staticFieldName = staticFieldDom.attr('name');
            var staticFieldConfig = {'name': '', 'value': ''};
            staticFieldConfig['name'] = staticFieldName;
            staticFieldConfig['value'] = $('#static-' + staticFieldName + '-value').val();
            staticFields.push(staticFieldConfig);
        }
        result['staticFields'] = staticFields;
        console.log(result)
        return result;
    }

    /*
     * 导出爬虫模板
     */
    function exportJson() {
        var json = formJson('spiderInfoForm');
        console.log("step")
        var jsonRes = jsonStringify(json, 4);
        console.log(jsonRes == null);
        downloadFile(json['domain'] + '.json', jsonRes);
        $('#spiderInfoJson').html(jsonRes);
    }
    
    function addDynamicField() {
    	$('#advance_settings').modal('hide');
        var field, count, name, need, reg, path;
        inputModal('字段名称（必须为英文）',function (data) {
            count = $('.dynamicField').length + 1;
            field = data;
            $('#dynamicFields').append('<div id="dynamicField' + count + '" class="dynamicField" name=' + field +'>\n' +
                '                                    <div class="form-group">\n' +
                '                                        <label for="' + field + 'Reg">' + field + 'Regex</label>\n' +
                '                                        <input type="text" class="form-control reg" id="' + field + 'Reg" name="' + field + 'Reg" placeholder="' + field + '正则">\n' +
                '                                    </div>\n' +
                '                                    <div class="form-group">\n' +
                '                                        <label for="' + field + 'XPath">' + field + 'XPath</label>\n' +
                '                                        <input type="text" class="form-control xpath" id="' + field + 'XPath" name="' + field + 'XPath" placeholder="' + field + 'XPath">\n' +
                '                                    </div>\n' +
                '                                    <div class="checkbox">\n' +
                '                                        <label>\n' +
                '                                            <input type="checkbox" name="need' + field + '" id="need' + field + '">\n' +
                '                                            是否网页必须有' + field + '\n' +
                '                                        </label>\n' +
                '                                    </div>\n' +
                '                                    <button class="btn btn-danger" type="button" onclick="$(\'#dynamicField' + count + '\').remove();">删除动态字段' + field + '</button>\n' +
                '                                </div>');
            $('#confirmModal').modal('hide');
        });
    }

    function addStaticField() {
    	$('#advance_settings').modal('hide');
        var field, count, name, need, reg, path;
        inputModal('字段名称（必须为英文）',function (data) {
            count = $('.staticField').length + 1;
            field = data;
            $('#staticFields').append('<div id="staticField' + count + '" class="staticField" name=' + field +'>\n' +
                '                                    <div class="form-group">\n' +
                '                                        <label for="' + field + 'Reg">' + field + 'Regex</label>\n' +
                '                                        <input type="text" class="form-control reg" id="' + field + 'Reg" name="' + field + 'Reg" placeholder="' + field + '正则">\n' +
                '                                    </div>\n' +
                '                                    <div class="form-group">\n' +
                '                                        <label for="' + field + 'XPath">' + field + 'XPath</label>\n' +
                '                                        <input type="text" class="form-control xpath" id="' + field + 'XPath" name="' + field + 'XPath" placeholder="' + field + 'XPath">\n' +
                '                                    </div>\n' +
                '                                    <div class="checkbox">\n' +
                '                                        <label>\n' +
                '                                            <input type="checkbox" name="need' + field + '" id="need' + field + '">\n' +
                '                                            是否网页必须有' + field + '\n' +
                '                                        </label>\n' +
                '                                    </div>\n' +
                '                                    <button class="btn btn-danger" type="button" onclick="$(\'#staticField' + count + '\').remove();">删除静态字段' + field + '</button>\n' +
                '                                </div>');
            $('#confirmModal').modal('hide');
        });
    }
    
    function showDynamicFields(id) {
        var dynamicFields = dynamicFieldList[id];
        console.log('step here');
        tableModal(dynamicFields, id + '动态字段');
    }
</script>
</body>
</html>