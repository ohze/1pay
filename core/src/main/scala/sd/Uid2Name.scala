package sd

trait Uid2Name {

  /** @return username or None if not found
    * @note must not throws Exception
    */
  def apply(uid: Int): Option[String]
}
