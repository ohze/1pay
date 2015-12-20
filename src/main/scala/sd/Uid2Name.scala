package sd

trait Uid2Name {
  /** must not throws Exception */
  def apply(uid: Int): Option[String]
}
