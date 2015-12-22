import play.api.Application
import play.api.db.DB
import anorm._

object EnsureUser1 {
  def run(implicit app: Application): Unit = {
    DB.withConnection { implicit conn =>
      SQL"""INSERT INTO users(id, coin, username)
            VALUES (1, 5000000000, 'Trần Văn Nguyễn')
            ON DUPLICATE KEY UPDATE coin = 5000000000
        """.executeUpdate()
    }
  }
}
