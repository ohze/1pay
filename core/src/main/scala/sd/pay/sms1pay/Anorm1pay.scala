package sd.pay.sms1pay

import java.sql.Connection
import anorm._
import anorm.SqlParser._
import java.lang.System.currentTimeMillis

object Anorm1pay {
  def exists(request_id: String)(implicit conn: Connection): Boolean =
    SQL"SELECT COUNT(*) FROM 1pay_log WHERE request_id = $request_id".as(scalar[Long].single) > 0

  private val SqlInsertLog = SQL("""
    INSERT INTO 1pay_log(request_id, phone, uid, status, output, amount, updated, type)
    VALUES ({rid}, {phone}, {uid}, {status}, {output}, {amount}, {time}, {type})""")

  def insertLog(d: ChargeData, uid: Int)(implicit conn: Connection): Unit =
    SqlInsertLog.on(
      "rid" -> d.request_id,
      "phone" -> d.msisdn,
      "uid" -> uid,
      "status" -> d.error_code,
      "output" -> d.toString,
      "amount" -> d.amount,
      "time" -> (currentTimeMillis / 1000).toInt,
      "type" -> d.telco
    ).executeInsert()
}
