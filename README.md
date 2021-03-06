# Cloud Computing Project
-------

#### Linux 常用Command：
https://www.jianshu.com/p/05f893b0671c
#### Stanford NLP API:
https://stanfordnlp.github.io/CoreNLP/download.html
#### 大数据学习笔记 Gitbooks:
https://chu888chu888.gitbooks.io/hadoopstudy/content/Content/8/chapter0807.html
#### 大数据资源共享 Gitbooks:
https://opensourceteam.gitbooks.io/bigdata/content/hadoop/example/02-wordcount.html
#### Vincent's Github:
https://github.com/vincentkk6356/TwitterXSparkStreamingXBitcoin


## Framework Version:
1. Spark Version: 2.2.1
2. Hadoop Version: 2.7.5
3. Flume Version: 1.6.0
4. Kafka Version: 2.12-1.0.1
5. Zookeeper Version: 3.4.11
6. Maven Version: 3.5.3
7. Hive Version: 2.3.2

--------------------Spark Streaming------------------------
## Spark-submit:
./spark-submit --master local[2] \
--class org.apache.spark.examples.streaming.NetworkWordCount \
--name NetworkWordCount \
$SPARK_HOME/examples/jars/spark-examples_2.xxx.xx.jar hduser 9999

//ps: 9999是netcat的端口

## 启动netcat:
$ nc -lk 9999

## Spark-shell 提交（测试）
./spark-submit --master local[2] \

//然后在shell里面写代码
//Spark Conf 可以写成sc,在进入spark shell的时候就已经设定好了

----------------------------Flume--------------------------
https://flume.apache.org/FlumeUserGuide.html

## 启动flume:
flume-ng agent -n TwitterAgent -c conf -f /opt/flume/conf/twitter1.conf

## 启动flume2 - Console有command:
flume-ng agent -n TwitterAgent --conf /opt/flume/conf -f /opt/flume/conf/twitter1.conf -Dflume.root.logger=DEBUG,console

## 启动flume2 - Console有command:
flume-ng agent --name TwitterAgent --conf $FLUME_HOME/conf  --conf-file $FLUME_HOME/conf/beta_test.conf -Dflume.root.logger=DEBUG,console


--------------------------ZooKeeper------------------------
Start Server:
$Zookeeper_HOME/bin/  ->  ./zkServer.sh
//"QuorumPeerMain" will start in 'jps'

Client: ./zkCli.sh

---------------------------Kafka ----------------------------
官方Doc：
https://kafka.apache.org/10/documentation.html
Kafka Stream例子：（自己测试成功）
https://kafka.apache.org/10/documentation/streams/quickstart

## - Start Kafka (Start Zookeeper first)
`bin/zookeeper-server-start.sh config/zookeeper.properties`

```
./kafka-server-start.sh $KAFKA_HOME/config/server.properties
----will see "kafka" in 'jps'
```

## - Create Topic:
cd $KAFKA_HOME/bin/
kafka-topics.sh --create --zookeeper l

## - Verify Topic:
cd $KAFKA_HOME/bin/


## - Start consumer:
cd $KAFKA_HOME/bin/
./kafka-console-consumer.sh --zookeeper localhost:2181 hello_topic
-- (consume it form the beginning):
./kafka-console-consumer.sh --zookeeper localhost:2181 hello_topic --from-beginning

## - Descript it :
kafka-topics.sh --describe --zookeeper localhost:2181
- (后面可以指定topic:)
kafka-topics.sh --describe --zookeeper localhost:2181 hello_topic

--------------Chap.9 Flume + SparkStreaming---------------------
# Combime them:
## Advanced Source: (Flume + Kafka)
* Check the version match (important)

## Flume-syle Push-based Approach
* Config Flume
	* Avro Agent (?)
	* sink is avro sink
	* initialize port num
* Config Spark Streaming Application (Video 9.4)
	1. Flume.conf, 改AVRO
	2. IJ 里面加入dependency
	3. New a "FlumePushWordCount.scala"
	4. maven打包：`mvn clean package -DskipTests` 获得一个jar包，然后就copy到机器上
	5. 传输FlumeUtils (看官网): 在Spark Submit的时候加入`--Package xxx`
	6. 改sink host name(flume.conf) -> 本地

## Flume + Spark Streaming - Push方式 整个流程：
1. Spark Submit:
```shell
spark-submit \
--class com.imooc.spark.FlumePushWordCount \
--master local[2] \
--package org.apache.spark:spark-streaming-flume_2.11:2.2.0 \
/$HADOOP_HOME/lib/sparktrain-1.0.jar\
hadoop000 41414
```
2. 启动Flume：
```shell
flume-g agent \
--name xxx-agent \
--conf $FLUME_HOME/conf \
--conf-file $FLUME_HOME/conf/flume_push_xxx.conf \
-Dflume.root.logger = INFO,console
```
3. 启动Telnet，观察数据是否有反应：`telnet localhost 4444`

## Spark 开发应用程序 (Video 9-9)

Pull 的方式： **要先启动Flume，后启动SparkStreaming**

--------------Chap.9 Kafka + SparkStreaming-----------------
https://spark.apache.org/docs/2.2.1/streaming-kafka-0-8-integration.html

## Receiver-based Approach
1. Start zk 
`cd $ZK_HOME/bin/` `./zkServer.sh start`
2. Start kafka 
`cd $KAFKA_HOME/bin/` `./kafka-sever-start.sh -daemon $KAFKA_HOME/config/server.properties` (JPS will have kafka)
3. 创建topics:
`./kafka-topics.sh --create --zookeeper localhost:2181 --replication-factor 1 --partitions 1 --topic kafka_streaming_topic`
*可以查看是否存在*： `./kafka-topics.sh --list --zookeeper localhost:2181`
4. 开Producer: `./kafka-console-producer.sh --broker-list localhost:9092 --topic kafka_streaming_topic`
5. (开一个控制台) `cd $KAFKA_HOME/bin` `./kafka-console-consumer.sh --zookeeper localhost:2181 --topic `


常用命令：
https://www.jianshu.com/p/f439bcca64c8

--------------------------References------------------------
Read-write into HDFS using JAVA:
https://creativedata.atlassian.net/wiki/spaces/SAP/pages/52199514/Java+-+Read+Write+files+with+HDFS

Hive Spark Tutorial:
http://spark.apache.org/docs/2.2.1/sql-programming-guide.html#hive-tables

Streaming-Spark:
http://spark.apache.org/docs/2.1.0/streaming-programming-guide.html#dataframe-and-sql-operations

Stanford NLP Framework:
https://stanfordnlp.github.io/CoreNLP/download.html






--------------------Copy to all slave nodes------------------
tar cvf ~/zk.tgz  zookeeper-3.4.11
tar cvf ~/hive.tgz apache-hive-2.3.2-bin

ssh hduser@studentxx-x1

sudo scp hduser@student59:kafka-7305.tgz /opt
sudo scp hduser@student59:zk.tgz /opt
sudo scp hduser@student59:hive.tgz /opt

cd /opt

sudo tar xvf kafka-7305.tgz
sudo tar xvf zk.tgz
sudo tar xvf hive.tgz

sudo chown -R hduser:hadoop /opt/kafka_2.12-1.0.1
sudo chown -R hduser:hadoop /opt/zookeeper-3.4.11
sudo chown -R hduser:hadoop /opt/apache-hive-2.3.2-bin

-------------------Maven Project Build------------------------
Reference Link:
http://wiki.jikexueyuan.com/project/maven/building-and-test-project.html
http://maven.apache.org/guides/getting-started/maven-in-five-minutes.html

1. check version
2. Edit the local repository:仓库 (conf/setting.xml) 路径是(`$MAVEN_HOME/project`)
3. Create Project
```shell
mvn archetype:generate \
-DgroupId=twitter.bijection.Program \
-DartifactId=bitcoinProgram \
-DarchetypeArtifactId=maven-archetype-quickstart \
-DinteractiveMode=false
```
*It might take a while, but the result will like this:*
```
[INFO] ----------------------------------------------------------------------------
[INFO] Using following parameters for creating project from Old (1.x) Archetype: maven-archetype-quickstart:1.0
[INFO] ----------------------------------------------------------------------------
[INFO] Parameter: basedir, Value: /opt/maven
[INFO] Parameter: package, Value: twitter.bijection.Program
[INFO] Parameter: groupId, Value: twitter.bijection.Program
[INFO] Parameter: artifactId, Value: bitcoinProgram
[INFO] Parameter: packageName, Value: twitter.bijection.Program
[INFO] Parameter: version, Value: 1.0-SNAPSHOT
[INFO] project created from Old (1.x) Archetype in dir: /opt/maven/bitcoinProgram
[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
[INFO] Total time: 59.451 s
[INFO] Finished at: 2018-04-09T10:49:10+08:00
[INFO] ------------------------------------------------------------------------
```
4. Add some dependency:
```xml
        <dependency>
            <groupId>org.scala-lang</groupId>
            <artifactId>scala-library</artifactId>
            <version>${scala.version}</version>
        </dependency>

        <!-- avro & twitter -->
        <dependency>
            <groupId>org.apache.avro</groupId>
            <artifactId>avro</artifactId>
            <version>1.8.0</version>
        </dependency>

        <dependency>
            <groupId>com.twitter</groupId>
            <artifactId>bijection-avro_2.11</artifactId>
            <version>0.9.6</version>
        </dependency>

        <!-- spark dependencies-->
        <dependency>
            <groupId>org.apache.spark</groupId>
            <artifactId>spark-core_2.11</artifactId>
            <version>2.3.0</version>
        </dependency>

        <dependency>
            <groupId>org.apache.spark</groupId>
            <artifactId>spark-streaming-kafka-0-10_2.11</artifactId>
            <version>2.3.0</version>
        </dependency>


        <dependency>
            <groupId>org.apache.spark</groupId>
            <artifactId>spark-streaming_2.11</artifactId>
            <version>2.3.0</version>
        </dependency>
```
5. Go and edit the JAVA file:
Where the HelloWorld Program is:
`cd src/main/java/twitter/bijection/Program/`
Where the test program is:
`cd src/test/java/twitter/bijection/Program/`

6. Build the project:（记得是在你的Project的目录下，也就是有pom.xml的那个文件夹）
`hduser@student59:/opt/maven/bitcoinProgram$ mvn clean package`
Results:
```
[INFO] Building jar: /opt/maven/bitcoinProgram/target/bitcoinProgram-1.0-SNAPSHOT.jar
[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
[INFO] Total time: 03:09 min
[INFO] Finished at: 2018-04-09T11:11:31+08:00
[INFO] ------------------------------------------------------------------------
```

7. You can find class: (只供你查看，不需要做操作)
`cd /opt/maven/bitcoinProgram/target/classes/twitter/bijection/Program`
you can find `App.class`

8. 运行:
`hduser@student59:/opt/maven/bitcoinProgram$ java -cp target/bitcoinProgram-1.0-SNAPSHOT.jar twitter.bijection.Program.App`

或者：
```shell
cd /opt/maven/bitcoinProgram/target/classes/
java twitter.bijection.Program.App
```
也可以出现HelloWorld!的结果

### Where you can find the jar file?
> `/opt/maven/bitcoinProgram/target/` 当你build成功之后，你就能够看到在target文件夹下面有个：`bitcoinProgram-1.0-SNAPSHOT.jar` 获取之后可以用来以后做Spark-submit

### 开发编译运行流程：
```shell
cd /opt/maven/bitcoinProgram/src/main/java/twitter/bijection/Program
vim App.java
vim /opt/maven/bitcoinProgram/src/main/java/twitter/bijection/Program/App.java

cd /opt/maven/bitcoinProgram
mvn clean package

cd /opt/maven/bitcoinProgram
java -cp target/bitcoinProgram-1.0-SNAPSHOT.jar twitter.bijection.Program.App
```

```java
//App.java

package twitter.bijection.Program;

/**
 * Hello world!
 *
 */
public class App
{
    public static void main( String[] args )
    {
        System.out.println( "Test Maven!!!!!" );
    }
}
```
### Maven user setting (setting.xml) - 添加大陆镜像mirror
https://zhuanlan.zhihu.com/p/28133184

### Maven 简书介绍（关于本地repo等信息）
https://www.jianshu.com/p/78c16fc600a9



----------------Hive Programming-------------------
### Reference Link
* 将 Beeline 客户端与 Apache Hive 配合使用
https://docs.azure.cn/zh-cn/hdinsight/hadoop/apache-hadoop-use-hive-beeline

# Hive

1. Verify the Hive config(hive-site.xml in conf):
```xml
<property>
  <name>javax.jdo.option.ConnectionDriverName</name>
  <value>com.mysql.cj.jdbc.Driver</value>
</property>

<property>
  <name>javax.jdo.option.ConnectionUserName</name>
  <value>group11</value>
</property>

<property>c
  <name>javax.jdo.option.ConnectionPassword</name>
  <value>student</value>
</property>
```

2. Check the HDFS warehouse path:
`hduser@student59:~$ hdfs dfs -ls /user/hive/warehouse`

3. Check Hive Terminal:
```
hive
show databases;
show tables;
```
Restusts:
```
hive> show databases;
OK
default
Time taken: 2.95 seconds, Fetched: 1 row(s)
hive> show tables;
OK
test
```
表明这里有个数据库叫做default，然后有一个table叫做test;

4. Create Table:
Data Format:
t_date,t_time,b_price,s_output
2018-04-12,13:23:32,7713.12,1
2018-04-12,13:23:32,7713.12,1
2018-04-12,13:23:32,7713.12,1
2018-04-12,13:23:32,7713.12,2
2018-04-12,13:23:33,7713.12,1
2018-04-12,13:23:33,7713.12,2
2018-04-12,13:23:34,7713.12,1
2018-04-12,13:23:34,7713.12,1

```sql
CREATE TABLE b_results (b_price float, s_output int, t_time String) ROW FORMAT DELIMITED FIELDS TERMINATED BY ',';
CREATE TABLE b_results (t_date String, t_time String, s_output int, b_price float) ROW FORMAT DELIMITED FIELDS TERMINATED BY ',';
CREATE TABLE evlb (s_output int, r_weight float) ROW FORMAT DELIMITED FIELDS TERMINATED BY ',';


CREATE TABLE bevl1 (t_date date,t_time String,s_output float);

select t_date, t_time, avg(s_output) from b_results group by t_date, t_time;
select t_date, t_time, max(b_price) from b_results group by t_date, t_time;

INSERT OVERWRITE TABLE bevl1
SELECT b_results.t_date,b_results.t_time, avg (b_results.s_output)
FROM b_results
GROUP BY b_results.t_date, b_results.t_time;


select t_date,t_time,b_price,sum(number* coefficient) from evl group by t_date,t_time,b_price;



CREATE TABLE bpr (t_date date,t_time String, b_price float);
CREATE TABLE tamp2 (t_d String,t_t String, b_p float) ROW FORMAT DELIMITED FIELDS TERMINATED BY ',';

CREATE TABLE evl_result(t_date String,t_time String, b_price float, rise_prob float) ROW FORMAT DELIMITED FIELDS TERMINATED BY ',';

select t_date,t_time,b_price,sum(number* coefficient) from evl group by t_date,t_time,b_price;

insert overwrite table evl_result
select evl.t_date,evl.t_time,evl.b_price,sum(evl.number* evl.coefficient) from evl group by evl.t_date,evl.t_time,evl.b_price;


INSERT OVERWRITE TABLE evla
select t_date, t_time, price, s_output, count(s_output) from b_results group by all t_date, t_time, s_output;

----has problem----------
INSERT OVERWRITE TABLE evla
select b_results.t_date, b_results.t_time, b_results.b_price, b_results.s_output, count(b_results.s_output) from b_results group by b_results.t_date, b_results.t_time, b_results.s_output;



```



5. Load data form local (called it `test.csv`):
path:`/opt/apache-hive-2.3.2-bin/test.csv`

```shell
LOAD DATA LOCAL INPATH '/opt/apache-hive-2.3.2-bin/test.csv' OVERWRITE INTO TABLE b_results;
Loading data to table default.b_results

LOAD DATA LOCAL INPATH '/opt/apache-hive-2.3.2-bin/test.csv' OVERWRITE INTO TABLE evla;

//Load form HDFS
LOAD DATA INPATH '/twitter_sentiment_bitcoin/student59.txt' OVERWRITE INTO TABLE b_results;

//Load form HDFS
LOAD DATA INPATH '/twitter_sentiment_bitcoin/student59.txt' INTO TABLE b_results;

LOAD DATA INPATH '/merged/*' INTO TABLE b_results;
```

6. Delete table: `DROP TABLE b_results;`

7. Compile the Jar file with specifying the jar file:

`/opt/apache-hive-2.3.2-bin/lib/hive-jdbc-2.3.2.jar `

```shell
sudo java -cp target/bitcoinProgram-1.0-SNAPSHOT.jar:hive-jdbc-2.3.2.jar:httpclient-4.4.jar twitter.bijection.Program.App
java -cp target/bitcoinProgram-1.0-SNAPSHOT.jar twitter.bijection.Program.App

```

/opt/apache-hive-2.3.2-bin/lib/httpclient-4.4.jar

8. Occur some problem....不能够编译了。。


9. Connect to database 
beeline -u jdbc:hive2://localhost:10000/default -n root -p
beeline -u jdbc:hive2://localhost:10000/ -n root -p
```shell
beeline
!connect jdbc:hive2://localhost:10000/default
 
!connect jdbc:hive2://202.45.128.135:16859/default
 (如果是要远程连接，记得使用CSVPN)
```
kill -9 xxxx

**Start the Hive metastore service**

hive --service metastore &
service hive-metastore stop

service hive-metastore status
service hive-server2 status

hiveserver2 start &
--service hiveserver2
netstat -nl|grep 10000

#### Start Hive Process:
```
//后台运行
hiveserver2 start &
//pid 22176
hive --service metastore &
//pid 22286


//监控运行：(两个都必须打开，缺一不可)
hiveserver2 start
hive --service metastore
```


#### Hive sql 
https://stackoverflow.com/questions/8484722/access-denied-for-user-rootlocalhost-while-attempting-to-grant-privileges
```
 mysql -u root -p
 // input password 'root'
 select user();
```
Then you can see this:
    mysql> select user();
    +-------------------+
    | user()            |
    +-------------------+
    | root@localhost    |
    +-------------------+
    1 row in set (0.00 sec)

```sql    
GRANT ALL PRIVILEGES ON `%`.* TO 'root'@'localhost' IDENTIFIED BY 'root' WITH GRANT OPTION;
GRANT ALL PRIVILEGES ON `%`.* TO 'root'@'%' IDENTIFIED BY 'root' WITH GRANT OPTION;
//Query OK, 0 rows affected, 1 warning (0.00 sec)



 select * from b_results;

 show tables;

 desc b_results;
```
##### mysql conf Gen:
http://imysql.com/my-cnf-wizard.html at `/etc/mysql`
#### mysql 配置文件详解：
https://github.com/jaywcjlove/mysql-tutorial/blob/master/chapter2/2.5.md
#### 远程连接mysql数据库：
https://www.jianshu.com/p/8fc90e518e2c

#### restart mysql:
`service mysql restart`

#### Hive操作日期：
https://www.jianshu.com/p/e30395941f9c

#### Hive Programming Guide:
https://cwiki.apache.org/confluence/display/Hive/LanguageManual
https://cwiki.apache.org/confluence/display/Hive/LanguageManual+GroupBy
https://cwiki.apache.org/confluence/display/Hive/LanguageManual+Cli



-----------------HDFS 操作-------------------
#### HDFS 文件常用操作命令：
https://segmentfault.com/a/1190000002672666
hadoop fs -ls  /
/twitter_sentiment_bitcoin
hadoop fs -ls /twitter_sentiment_bitcoin
hadoop fs -ls -R /twitter_sentiment_bitcoin/
hadoop fs -cat /twitter_sentiment_bitcoin/student59.txt

hdfs://student59:9000

find file in MacOS:
mdfind xxx
/Library/hortonworks/hive/lib/

export ODBCINI=/Library/hortonworks/hive/Setup/odbc.ini
export ODBCINSTINI=/Library/hortonworks/hive/Setup/odbcinst.ini
export HORTONWORKSHIVEODBCINI=/Library/hortonworks/hive/lib/universal/hortonworks.hiveodbc.ini

中文版介绍：Tableau - Hive connection官方文档
http://onlinehelp.tableau.com/current/pro/desktop/zh-cn/help.html#examples_hortonworkshadoop.html


-------------------------------------------------------


# Hive Commands: 

## Basic command:
1. select * from b_results;
2. show tables;
3. desc b_results;


## Advance commands:
```sql
INSERT OVERWRITE TABLE pv_gender_sum
SELECT pv_users.gender, count (DISTINCT pv_users.userid)
FROM pv_users
GROUP BY pv_users.gender;

INSERT INTO TABLE test VALUES (1, "Test test");
```



hadoop fs -cat /merged/April13_student59_1523769336533_1523769336513_April13.csv


```sql
//关于如何在数据库处理数据

1. row data b_results;

2. CREATE TABLE evla (t_date String,t_time String, b_price float, s_output int, number int) ROW FORMAT DELIMITED FIELDS TERMINATED BY ',';

3. INSERT OVERWRITE TABLE evla
select t_date, t_time, max(b_price), s_output, count(s_output) from b_results group by t_date, t_time, s_output;

4. create cofficient table evlb;

5. //左链接
INSERT OVERWRITE TABLE evl
SELECT evla.t_date, evla.t_time, evla.b_price, evla.s_output, evlb.r_weight, evla.number
FROM evla
JOIN evlb
ON evla.s_output=evlb.s_output
ORDER BY evla.t_time;

//查询语句
select t_date, t_time, b_price, sum(coefficient * number) from evl group by t_date, t_time, b_price;

6. create result table;
insert overwrite TABLE evl_result
select evl.t_date,evl.t_time,max(evl.b_price),sum(evl.number* evl.coefficient) from evl group by evl.t_date,evl.t_time;

```

-----------pySpark--------------

# PySpark execution: Hive connection: (connHive.py)
GitHub Code Repository
https://github.com/apache/spark/blob/master/examples/src/main/python/sql/hive.py
https://github.com/apache/spark/blob/master/examples/src/main/python/sql/basic.py
Read data form Hive(local metastore)
https://creativedata.atlassian.net/wiki/spaces/SAP/pages/54067227/Spark+Scala+-+Read+Write+files+from+Hive

(Search HiveContext)
http://spark.apache.org/docs/2.1.0/api/python/pyspark.sql.html#pyspark.sql.HiveContext
PySpark 入门：
https://distributesystem.wordpress.com/2016/04/12/python-spark-%E4%B8%A4%E5%A4%A9%E5%85%A5%E9%97%A8


```shell
vim ~/pyConnHive/pyConnHive.py 

$SPARK_HOME/bin/spark-submit --master=yarn ~/pyConnHive/pyConnHive.py
```


Hive read Json Tutorial (Very useful!)
http://thornydev.blogspot.hk/2013/07/querying-json-records-via-hive.html

Hive Json format project: (Maven, need to build)
https://github.com/rcongiu/Hive-JSON-Serde

```sql
LOAD DATA LOCAL INPATH '/home/hduser/newData.txt' OVERWRITE INTO TABLE json_table;


select get_json_object(json_table.json, '$.bitcoin_price') as bitcoin_price, 
       get_json_object(json_table.json, '$.created_at') as created_at,
       get_json_object(json_table.json, '$.text') as text
from json_table;

CREATE TABLE clean_result (bitcoin_price String,created_at String,text String);


INSERT OVERWRITE TABLE clean_result
select get_json_object(json_table.json, '$.bitcoin_price') as bitcoin_price, 
       get_json_object(json_table.json, '$.created_at') as created_at,
       get_json_object(json_table.json, '$.text') as text
from json_table;

INSERT OVERWRITE TABLE clean_result
select * from clean_result where bitcoin_price LIKE '8%' AND where bictoin_price IS NOT NULL;

hive -e 'select * from clean_result' > /home/hduser/trainingData.txt

```

Comparism with Hive and Spark SQL:
https://data-flair.training/blogs/apache-hive-vs-spark-sql/

kafka introduction:
https://www.slideshare.net/JeanPaulAzar1/brief-introduction-to-kafka-streaming-platform

kafka中文介绍：
https://www.jianshu.com/p/1b657ac52f89

### Reload the data into Hive:

```sql
LOAD DATA LOCAL INPATH '/home/hduser/db/warehouse/b_results/2018-04-17_1523939055889.csv' OVERWRITE INTO TABLE b_results;

LOAD DATA LOCAL INPATH '/opt/apache-hive-2.3.2-bin/test.csv' OVERWRITE INTO TABLE evlb;

INSERT OVERWRITE TABLE evla select t_date, t_time, max(b_price), s_output, count(s_output) from b_results group by t_date, t_time, s_output;
INSERT OVERWRITE TABLE evl SELECT evla.t_date, evla.t_time, evla.b_price, evla.s_output, evlb.r_weight, evla.number FROM evla JOIN evlb ON evla.s_output=evlb.s_output ORDER BY evla.t_time;
INSERT INTO TABLE evl_result select evl.t_date,evl.t_time,max(evl.b_price),sum(evl.number* evl.coefficient) from evl group by evl.t_date,evl.t_time;
```

#### Change the Reduce Tasks:

In order to change the average load for a reducer (in bytes):
  set hive.exec.reducers.bytes.per.reducer=<number>
In order to limit the maximum number of reducers:
  set hive.exec.reducers.max=8
In order to set a constant number of reducers:
  set mapreduce.job.reduces=8

