package sd.pay.sms1pay

import java.text.NumberFormat
import java.util.Locale
import javax.inject.{Inject, Singleton}
import play.api.Configuration
import play.api.libs.json.{JsObject, Json}
import sd.util.Romanize
import scala.concurrent.Future

@Singleton
final class SmsResult @Inject() (config: Configuration) {
  private val CSKH = config.getString("sd.cskh").get

  /** @param status 0 if fail, 1 if success */
  private def toJs(sms: String, status: Int) = Json.obj(
    "status" -> status,
    "sms" -> (
      if (status == 0) s"$sms. Quy khach vui long lien he $CSKH de duoc ho tro. Tran trong!"
      else s"$sms. CSKH: $CSKH"
    ),
    "type" -> "text"
  )

  @inline private def futJs(sms: String, status: Int = 0) = Future successful toJs(sms, status)

  val ErrSignature = futJs("Tin nhan sai cu phap")
  val OkValid = futJs("Tin nhan dung cu phap", 1)
  val ErrInternal = futJs("Giao dich that bai")
  val ErrProcessed = futJs("Tin nhan da duoc xu ly")
  @inline def ErrId(id: Int) = futJs(s"So ID: $id khong hop le")

  private val numberFormat = NumberFormat.getNumberInstance(Locale.GERMAN)
  def OkCharged(username: String, changedCoin: Long): JsObject = {
    val changed = numberFormat.format(changedCoin)
    val name = Romanize(username)
    val msg = s"Ban da nap thanh cong $changed Bao vao tai khoan: $name. Chi tiet xem tai: sandinh.com/bank"
    toJs(msg, 1)
  }
}
