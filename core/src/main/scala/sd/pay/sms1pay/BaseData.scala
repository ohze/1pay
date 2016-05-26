package sd.pay.sms1pay

import scala.util.Try

trait BaseData {
  def mo_message: String

  def uidOpt: Option[Int] = {
    val s = mo_message.trim
    s.lastIndexOf(" ") match {
      case -1 => None
      case i  => Try { s.substring(i + 1).toInt }.toOption
    }
  }
}
