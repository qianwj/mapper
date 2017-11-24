# nest-spider
A funny spider


It already finished, after a series of exams...

This time, I made this spider just for fun.:-)

Actucally, this spider has existed, I just update and fix it.

Settings:

JDK 1.8.0
Tomcat 9.0
ElasticSearch 5.x(and need ansj, or you can edit the spiderinfo.json and webpage.json to adjust other Tokenizer, like ik.)
Casperjs(you need to install phontomjs before)

##Quick Start##
After you downloaded this .war, and finished configure jdk,tomcat,..., you should put that into TOMCAT_HOME/webapp.

Then start tomcat

Linux:
Terminal:
```
cd <your tomcat dir>
./bin/startup.sh
```

Windows:
find your tomcat directory, into the bin, you'll find the tomcat9w.exe, then start it, you must know how to startup it.

Open your browser

input the url:http://localhost:<your tomcat port>/nest-spider/ into location bar

Yup, It already start!
