package com.dejaview.dynamo.gear

import java.sql.{Connection, PreparedStatement, ResultSet}

import com.dejaview.dynamo.DynamoGearDriver
import com.dejaview.dynamo.model.GearSchedule
import com.dejaview.dynamo.mysql.MySQLConnPool
import com.dejaview.dynamo.util.CommonUtil
import com.typesafe.scalalogging.slf4j.Logger
import org.slf4j.LoggerFactory

object GearScheduleReader {
  val logger = Logger(LoggerFactory.getLogger(this.getClass.getName))

  def read(tableName : String) : Option[GearSchedule] = {

    var conn : Option[Connection] = None
    var pstmt : Option[PreparedStatement] = None
    var schedule : Option[GearSchedule] = None

    try {

      conn = Option(MySQLConnPool.instance.getConnection())

      val query =
        s"""
           |select dynamo_gear_id, table_name, start_time, end_time, tps
           |from ${DynamoGearDriver.propConfig.mysqlTableGear}
           |where table_name = ? and start_time <= ? and ? < end_time and valid = 1
         """.stripMargin

      logger.info(query)
      pstmt = Option(conn.get.prepareStatement(query))

      val curr = CommonUtil.currTime()

      pstmt.get.setString(1, tableName)
      pstmt.get.setString(2, curr)
      pstmt.get.setString(3, curr)

      val rs: ResultSet = pstmt.get.executeQuery( )

      while (rs.next()) {
        schedule = Some(new GearSchedule(rs))
      }

    } catch {
      case e : Exception => {
        e.printStackTrace()
        throw e
      }
    } finally {
      if(pstmt.isDefined) pstmt.get.close()
      if(conn.isDefined) conn.get.close()
    }
    return schedule
  }
}
