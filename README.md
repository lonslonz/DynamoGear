Dynamo-Gear
---
Controll DynamoDB's capacity (tps). It's very useful for saving money.

# Features

* Change DynamoDB's write tps from schedule.
* Save and read schedule from MySQL
* Implemented by Scala

# Install    

### Clone source

	```
	git clone https://github.com/lonslonz/DynamoGear.git
	```
		
### Comple by maven

	```
	mvn package
	```
# Usage

### Create MySQL table for saving schedule.

There is create.sql in source.

```
 Execute creating sql onto MySQL. : ./scripts/mysql/create.sql
```
	
Contents of create.sql are

``` 

CREATE TABLE `DYNAMO_GEAR` (
  `dynamo_gear_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `table_name` varchar(32) COLLATE utf8_bin NOT NULL,
  `start_time` time DEFAULT NULL,
  `end_time` time DEFAULT NULL,
  `tps` bigint(20) DEFAULT NULL,
  `valid` tinyint(4) DEFAULT '1',
  `updated_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`dynamo_gear_id`),
  UNIQUE KEY `table_name` (`table_name`,`start_time`),
  KEY `start_time` (`start_time`,`end_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

CREATE TABLE `DYNAMO_GEAR_HIS` (
  `his_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `dynamo_gear_id` bigint(20) NOT NULL,
  `table_name` varchar(32) COLLATE utf8_bin NOT NULL,
  `start_time` datetime DEFAULT NULL,
  `end_time` datetime DEFAULT NULL,
  `curr_write_tps` bigint(20) DEFAULT NULL,
  `expected_write_tps` bigint(20) DEFAULT NULL,
  `changed_write_tps` bigint(20) DEFAULT NULL,
  `updated_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`his_id`),
  KEY `dynamo_gear_id` (`dynamo_gear_id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
```
	
### Make your schedule

Insert schedules. 
	
```
Sample : ./scripts/mysql/sample.sql
```
	
Contents of sample.sql are

```
Sample : 
INSERT INTO `DYNAMO_GEAR` (`table_name`, `start_time`, `end_time`, `tps`, `valid`)
VALUES
	('test', '00:00:00', '12:00:00', 10, 1),
	('test', '12:00:00', '14:00:00', 11, 1),
	('test', '14:00:00', '18:00:00', 14, 1),
	('test', '18:00:00', '23:59:59', 12, 1);

```

### Configure your server environment. 

The configuration file is "src/main/resources/production.properties"
Change them as your config. You can install [local DynamoDB](http://docs.aws.amazon.com/amazondynamodb/latest/developerguide/DynamoDBLocal.html) for test
	
```
aws.accessKey = YOUR_ACCESS_KEY
aws.secretAccessKey = YOUR_SECRET_ACCESS_KEY
aws.dynamo.local=http://localhost:8000
aws.dynamo.region=ap-northeast-2

mysql.url=jdbc:mysql://localhost:20000/test
mysql.user=YOUR_USER_ID
mysql.password=YOUR_PASSWORD
mysql.driver=com.mysql.jdbc.Driver

mysql.table.gear=DYNAMO_GEAR
mysql.table.gearHis=DYNAMO_GEAR_HIS
```

### Execute jar

```
scala -cp target/dynamo-gear-0.1.0.jar com.dejaview.dynamo.DynamoGearDriver --table test --local true
```

Parameters : 
* table : DynamoDB's table name that you want to change tps.
* local : true if you install local DynamoDB and use it.

