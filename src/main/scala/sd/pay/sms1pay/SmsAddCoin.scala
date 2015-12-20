package sd.pay.sms1pay

import scala.concurrent.Future

trait SmsAddCoin {
  def apply(uid: Int, vnd: Int, msg: String): Future[Long]
}
