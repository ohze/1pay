package sd

import javax.inject.{Inject, Singleton}

import anorm.SqlParser._
import anorm._
import play.api.cache.SyncCacheApi
import play.api.db.Database

import scala.concurrent.duration._

@Singleton
class Uid2NameImpl @Inject() (db: Database, cacheApi: SyncCacheApi)
    extends Uid2Name {
  private val Expiry = 2.hours

  def apply(uid: Int): Option[String] =
    cacheApi.getOrElseUpdate("n" + uid, Expiry)(
      db.withConnection { implicit c =>
        SQL"SELECT username FROM users WHERE id = $uid"
          .as(scalar[String].singleOpt)
      }
    )
}
