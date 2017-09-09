import org.apache.commons.codec.digest.HmacUtils.hmacSha256Hex
import play.api.Application

object SignData {
  def sign(d: (String, String)*)(implicit app: Application): String = {
    val text = d.map {
      case (k, v) => s"$k=$v"
    }.mkString("&")

    val secret = app.configuration.get[String]("sd.pay.sms1pay.secret")
    hmacSha256Hex(secret, text)
  }

  @inline def apply(d: (String, String)*)(implicit app: Application): Seq[(String, String)] =
    d :+ ("signature" -> sign(d: _*))
}
