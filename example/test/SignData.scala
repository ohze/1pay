import org.apache.commons.codec.digest.HmacUtils.hmacSha256Hex
import play.api.Application

object SignData {
  def apply(d: (String, String)*)(implicit app: Application): Seq[(String, String)] = {
    val signature = {
      val text = d.map {
        case (k, v) => s"$k=$v"
      }.mkString("&")

      val secret = app.configuration.getString("sd.pay.sfs1pay.secret").get
      hmacSha256Hex(text, secret)
    }
    d :+ ("signature" -> signature)
  }
}
