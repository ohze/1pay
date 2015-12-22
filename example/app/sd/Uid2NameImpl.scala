package sd

import javax.inject.Singleton
import anorm.SqlParser._
import anorm._
import play.api.cache.Cache
import play.api.db.DB
import scala.concurrent.duration._
import play.api.Play.current

@Singleton
class Uid2NameImpl extends Uid2Name {
  private val Expiry = 2.hours.toSeconds.toInt

  def apply(uid: Int): Option[String] = Cache.getOrElse("n" + uid, Expiry)(
    DB.withConnection { implicit c =>
      SQL"SELECT username FROM users WHERE id = $uid"
        .as(scalar[String].singleOpt)
    }
  )
}
