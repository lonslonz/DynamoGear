package com.dejaview.dynamo.config

import java.util.Properties

case class PropConfig(awsAccessKey : String,
                      awsSecretAccessKey : String,
                      awsDynamoLocal : String,
                      awsDynamoRegion : String,
                      mysqlUrl : String,
                      mysqlUser : String,
                      mysqlPass : String,
                      mysqlDriver : String,
                      mysqlTableGear : String,
                      mysqlTableGearHis : String) {

  def this(props : Properties) = {
    this(
      props.getProperty("aws.accessKey"),
      props.getProperty("aws.secretAccessKey"),
      props.getProperty("aws.dynamo.local"),
      props.getProperty("aws.dynamo.region"),
      props.getProperty("mysql.url"),
      props.getProperty("mysql.user"),
      props.getProperty("mysql.password"),
      props.getProperty("mysql.driver"),
      props.getProperty("mysql.table.gear"),
      props.getProperty("mysql.table.gearHis")
    )
  }
}
