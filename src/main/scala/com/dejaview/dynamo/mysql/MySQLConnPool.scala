package com.dejaview.dynamo.mysql

import com.dejaview.dynamo.DynamoGearDriver
import com.typesafe.scalalogging.slf4j.Logger
import org.apache.commons.dbcp2.BasicDataSource
import org.slf4j.LoggerFactory

class MySQLConnPool(url : String, driver : String, user : String, pass : String) {

  val pool = new BasicDataSource()

  pool.setUrl(url)
  pool.setDriverClassName(driver)
  pool.setUsername(user)
  pool.setPassword(pass)
  pool.setDefaultAutoCommit(true)
  pool.setInitialSize(3)

}

object MySQLConnPool {
  val logger = Logger(LoggerFactory.getLogger(this.getClass.getName))

  val connPool = new MySQLConnPool(DynamoGearDriver.propConfig.mysqlUrl,
                                   DynamoGearDriver.propConfig.mysqlDriver,
                                   DynamoGearDriver.propConfig.mysqlUser,
                                   DynamoGearDriver.propConfig.mysqlPass)

  def instance = connPool.pool

  def close() = {
    instance.close
    logger.info("MySQL Connection closed.")
  }
}