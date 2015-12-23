import play.api.Application
import play.api.db.DB
import anorm._

object EnsureUser1 {
  def run(uid: Int = 1, username: String = "Trần Văn Nguyễn")(implicit app: Application): Unit = {
    DB.withConnection { implicit conn =>
      SQL"""INSERT INTO users(id, coin, username)
            VALUES ($uid, 5000000000, $username)
            ON DUPLICATE KEY UPDATE coin = 5000000000
        """.executeUpdate()
    }
  }
}
