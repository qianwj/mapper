# nest-spider
A funny spider


It already finished, after a series of exams...

This time, I made this spider just for fun.:-)

Actucally, this spider has existed, I just update and fix it.

Any questions, you can send to my e-mail:qwjlu@sina.com, I'll fix it soon.

### Provide the support for Webmagic 0.7.3, HttpClient 4.5.2, Spring 5.0 
### Fix some problem in the Controller model
### Fix lots of NullPointerException
### Optimize UI

## Settings

1. JDK 1.8.0
2. Tomcat 9.0
3. ElasticSearch 5.x(and need ansj, or you can edit the spiderinfo.json and webpage.json to adjust other Tokenizer, like ik.)
Casperjs(you need to install phontomjs before)</br>

Optional:redis 3.2.x or 4.0

## Quick Start
After you downloaded this .war, and finished configure jdk,tomcat,..., you should put that into TOMCAT_HOME/webapp.

Then start tomcat

Linux:
Terminal:
```
cd <your tomcat dir>
./bin/startup.sh
```

Windows:
Find your tomcat directory, into the bin, you'll find the tomcat9w.exe, then start it, you must know how to startup it.

Open your browser

input the url:http://localhost:8080/nest-spider/ into location bar(if you change your tomcat port, remember change the port in the url)

Yup, It already start!

## Use Casperjs
Setting up your casperjs's port:9302
If you want another port, you need to decompress this war, and find the staticvalue.json, confgure the casperjs.
```
"ajaxDownloader": "http://localhost:9302/" 
```
And change to your port.

## Use ElasticSearch
1. Decompressing this war, then find the staticvalue.json,configure the es(cause the default option is not need elasticsearch)
```
"needES":true
```
2. Change to your cluster
```
"esClusterName": "nest"
```

If you want to change the host and port
```
"esHost": "localhost",
"esPort": 9300,
```

You need start elasticsearch before you catch the web pages.
