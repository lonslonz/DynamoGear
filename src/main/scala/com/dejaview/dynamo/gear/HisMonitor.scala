package com.dejaview.dynamo.gear

import java.sql._

import com.dejaview.dynamo.DynamoGearDriver
import com.dejaview.dynamo.model.GearSchedule
import com.dejaview.dynamo.mysql.MySQLConnPool

object HisMonitor {

  def queryChanged() : String = {
      s"""
         |insert into ${DynamoGearDriver.propConfig.mysqlTableGearHis}
         |(dynamo_gear_id, table_name, start_time, end_time, curr_write_tps, expected_write_tps, changed_write_tps)
         |values
         |(?, ?, ?, now(), ?, ?, ?)
        """.stripMargin
  }

  def saveChanged(gearSchedule : GearSchedule, startTime : String, currTps : Long, changedTps : Long) : Unit = {

    var conn : Option[Connection] = None
    var stmt : Option[PreparedStatement] = None


    var pstmt : Option[PreparedStatement] = None

    try {

      conn = Option(MySQLConnPool.instance.getConnection())
      var queryStr = queryChanged()

      pstmt = Option(conn.get.prepareStatement(queryStr))

      pstmt.get.setLong(1, gearSchedule.dynamoGearId)
      pstmt.get.setString(2, gearSchedule.tableName)
      pstmt.get.setString(3, startTime)
      pstmt.get.setLong(4, currTps)
      pstmt.get.setLong(5, gearSchedule.tps)
      pstmt.get.setLong(6, changedTps)

      pstmt.get.executeUpdate()

    } catch {
      case e : Exception => {
        e.printStackTrace()
        throw e
      }
    } finally {
      if(pstmt.isDefined) pstmt.get.close()
      if(conn != null) conn.get.close()
    }
  }

}
