import org.apache.commons.codec.digest.HmacUtils
import org.apache.commons.codec.digest.HmacAlgorithms.HMAC_SHA_256
import play.api.Application

object SignData {
  def sign(d: (String, String)*)(implicit app: Application): String = {
    val text = d.map {
      case (k, v) => s"$k=$v"
    }.mkString("&")

    val secret = app.configuration.get[String]("sd.pay.sms1pay.secret")
    new HmacUtils(HMAC_SHA_256, secret).hmacHex(text)
  }

  @inline def apply(d: (String, String)*)(implicit app: Application): Seq[(String, String)] =
    d :+ ("signature" -> sign(d: _*))
}
