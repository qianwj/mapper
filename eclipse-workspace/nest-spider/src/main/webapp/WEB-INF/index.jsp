<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="zh-CN"><head>
    <meta http-equiv="content-type" content="text/html; charset=UTF-8">
    <meta charset="utf-8">
    <title>Nest Have Fun</title>
    <meta name="author" content="Qian Wenjin">

    <!-- Le styles -->
    <link href="css/bootstrap.css" rel="stylesheet">
    <link href="css/bootstrap-responsive.css" rel="stylesheet">
    <link href="css/docs.css" rel="stylesheet">
    <style>
        body{font-family:"ff-tisa-web-pro-1","ff-tisa-web-pro-2","Lucida Grande","Helvetica Neue",Helvetica,Arial,"Hiragino Sans GB","Hiragino Sans GB W3","Microsoft YaHei UI","Microsoft YaHei","WenQuanYi Micro Hei",sans-serif;}
    </style>

    <!-- Le HTML5 shim, for IE6-8 support of HTML5 elements -->
    <!--[if lt IE 9]>
    <script src="http://cdn.bootcss.com/html5shiv/3.7.0/html5shiv.min.js"></script>
    <![endif]-->

    <!-- Le fav and touch icons -->
    <link rel="apple-touch-icon-precomposed" sizes="144x144" href="http://v2.bootcss.com/assets/ico/apple-touch-icon-144-precomposed.png">
    <link rel="apple-touch-icon-precomposed" sizes="114x114" href="http://v2.bootcss.com/assets/ico/apple-touch-icon-114-precomposed.png">
    <link rel="apple-touch-icon-precomposed" sizes="72x72" href="http://v2.bootcss.com/assets/ico/apple-touch-icon-72-precomposed.png">
    <link rel="apple-touch-icon-precomposed" href="http://v2.bootcss.com/assets/ico/apple-touch-icon-57-precomposed.png">
    <link rel="shortcut icon" href="http://v2.bootcss.com/assets/ico/favicon.png">

    <style id="holderjs-style" type="text/css"></style></head>

<body data-spy="scroll" data-target=".bs-docs-sidebar">

<!-- Navbar
================================================== -->
<div class="navbar navbar-inverse navbar-fixed-top" role="navigation">
    <div class="container">
        <div class="navbar-header">
            <button type="button" class="navbar-toggle" data-toggle="collapse" data-target=".navbar-collapse">
                <span class="sr-only">Toggle navigation</span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
            </button>
            <a class="navbar-brand" href="index.html">Nest Spider</a>
        </div>
        <div class="navbar-collapse collapse">
            <ul class="nav navbar-nav">
                <li><a href="/nest-spider/webpage/search_list.html">搜索</a></li>
                <li><a href="/nest-spider/webpage/domain_list.html">网站列表</a></li>
                <li><a href="/nest-spider/task/task_list.html">查看进度</a></li>
                <li><a href="/nest-spider/spider/edit_spiderinfo.html">模板编辑</a></li>
                <li><a href="/nest-spider/spiderinfo/spiderinfo_list.html">模板列表</a></li>
                <li><a href="spider/update_data.html">更新数据</a></li>
                <li><a href="/nest-spider/spider/quartz_list.html">定时任务管理</a></li>
            </ul>

        </div><!--/.navbar-collapse -->
    </div>
</div>


<div class="jumbotron masthead">
    <div class="container">
        <h1>Nest爬虫系统</h1>
        <p>只是为了好玩</p>
        <ul class="masthead-links">
            <li>
                <a href="#">GitHub project</a>
            </li>
            <li>
                <a href="#">Documents</a>
            </li>
            <li>
                <a href="#">意见反馈</a>
            </li>
            <li>
                Version 1.0.0
            </li>
        </ul>
    </div>
</div>
<div class="container">

    <div class="marketing">

        <h1>Nest爬虫介绍</h1>
        <p class="marketing-byline">一个分布式的，快速的爬虫系统</p>

        <div class="row-fluid">
            <div class="span6">
                <img class="marketing-img" src="imgs/logos/webmagic_logo.jpeg">
                <h2>基于WebMagic开发</h2>
                <p><a href="http://webmagic.io/">WebMagic</a>是一个简单灵活的Java爬虫框架。基于WebMagic，你可以快速开发出一个高效、易维护的爬虫。</p>
            </div>
            <div class="span6">
                <img class="marketing-img" src="imgs/logos/icon-elasticsearch-bb.svg" alt="elastic-logo">
                <h2>使用ElasticSearch作为数据源</h2>
                <p><a href="https://www.elastic.co/products/elasticsearch">ElasticSearch</a>是一个基于Lucene的搜索服务器。它提供了一个分布式多用户能力的全文搜索引擎，基于RESTful web接口。Elasticsearch是用Java开发的，并作为Apache许可条款下的开放源码发布，是当前流行的企业级搜索引擎。设计用于云计算中，能够达到实时搜索，稳定，可靠，快速，安装使用方便。</p>
            </div>

        </div>
    </div>

</div>

<!-- Footer
================================================== -->
<footer class="footer">
    <div class="container">
        <p>© 2017 Company, Inc. Nest Technology Create by TYUT Software Engineer 1517 Qian.</p>
    </div>
</footer>



<!-- Le javascript
================================================== -->
<!-- Placed at the end of the document so the pages load faster -->

<script src="js/jquery.js"></script>
<script src="js/bootstrap.js"></script>
</body></html>