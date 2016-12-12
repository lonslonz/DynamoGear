package com.dejaview.dynamo.util

import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat

object CommonUtil {

  val timeFmt = DateTimeFormat.forPattern("HH:mm:ss")
  val timeFullFmt = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")

  def currTime() : String = {
    return timeFmt.print(DateTime.now)
  }
  def currFullTime() : String = {
    return timeFullFmt.print(DateTime.now)
  }
}
