package sd.pay.sms1pay

import javax.inject.{Inject, Singleton}
import play.api.Configuration
import play.api.data._
import play.api.data.Forms._

@Singleton
class Forms1pay @Inject() (config: Configuration) {
  val Secret: String = config.get[String]("sd.pay.sms1pay.secret")

  private val text0 = default(text, "0")

  /** Mệnh giá 100.000VNĐ chỉ áp dụng đối với mạng Viettel và Mobifone
    *
    * @see http://developers.1pay.vn/http-apis/smsplus-charging
    */
  private val amount = number(1000, 100000)

  val formCharge = Form(mapping(
    "access_key" -> text0,
    "amount" -> amount,
    "command_code" -> text0,
    "error_code" -> text0,
    "error_message" -> text0,
    "mo_message" -> text0,
    "msisdn" -> text,
    "request_id" -> text,
    "request_time" -> text,
    "signature" -> text0
  )(ChargeData.apply)(ChargeData.unapply) verifying ("error.signature", _.checkSign(Secret)))

  val formCheck = Form(mapping(
    "access_key" -> text0,
    "amount" -> amount,
    "command_code" -> text0,
    "mo_message" -> text0,
    "msisdn" -> text,
    "telco" -> text0,
    "signature" -> text
  )(CheckData.apply)(CheckData.unapply) verifying ("error.signature", _.checkSign(Secret)))
}
