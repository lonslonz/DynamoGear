package com.dejaview.dynamo.model

import java.sql.{Date, ResultSet, Time}

case class GearSchedule(dynamoGearId : Long, tableName : String, startTime : Time, endTime : Time, tps : Long) {

  def this(rs : ResultSet) = {
    this(
      rs.getLong("dynamo_gear_id"),
      rs.getString("table_name"),
      rs.getTime("start_time"),
      rs.getTime("end_time"),
      rs.getLong("tps")
    )
  }
}
