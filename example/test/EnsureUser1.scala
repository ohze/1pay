import play.api.db.Database
import anorm._

object EnsureUser1 {
  def run(db: Database, uid: Int = 1, username: String = "Trần Văn Nguyễn"): Unit = {
    db.withConnection { implicit conn =>
      SQL"""INSERT INTO users(id, coin, username)
            VALUES ($uid, 5000000000, $username)
            ON DUPLICATE KEY UPDATE coin = 5000000000
        """.executeUpdate()
    }
  }
}
