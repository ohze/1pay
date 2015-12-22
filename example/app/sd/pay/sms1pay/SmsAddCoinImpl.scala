package sd.pay.sms1pay

import javax.inject.Singleton
import play.api.Logger
import play.api.db.DB
import scala.concurrent.Future
import anorm._
import play.api.Play.current

@Singleton
class SmsAddCoinImpl extends SmsAddCoin {
  private val logger = Logger(this.getClass)

  /** @return changed coin */
  def apply(uid: Int, vnd: Int, msg: String): Future[Long] = {
    val changed = vnd * 100
    DB.withConnection { implicit c =>
      SQL"UPDATE users SET coin = coin + $changed WHERE id = $uid".executeUpdate()
    }
    logger.info(s"add $changed for $uid - $msg")
    Future successful changed
  }
}
