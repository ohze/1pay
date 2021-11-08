package sd.pay.sms1pay

import org.apache.commons.codec.digest.HmacUtils
import org.apache.commons.codec.digest.HmacAlgorithms.HMAC_SHA_256

/** Data of: Kiểm tra cú pháp MO
  *
  * @param access_key Đại diện cho sản phẩm của merchant khai báo trong hệ thống 1pay.vn
  * @param amount Số tiền trừ vào tài khoản khách hàng.
  * @param command_code Là mã dịch vụ của khách hàng. Đăng ký và khai báo trên hệ thống 1Pay. Ví dụ GAME1
  * @param mo_message Nội dung tin nhắn của khách hàng ở dạng String .Ví dụ: MI NAP10 dunglp
  * @param msisdn Số điện thoại nhắn tin của khách hàng, theo chuẩn international, bắt đầu bằng 84
  * @param telco Mã nhà mạng. Ví dụ: vnp(Vinaphone),vms(Mobifone),vtm(Viettel)
  * @param signature Chữ ký, merchant có thể sử dụng signature để kiểm soát an ninh.
  * @see [[http://developers.1pay.vn/http-apis/smsplus-charging Kiểm tra cú pháp MO]]
  */
case class CheckData(
    access_key: String,
    amount: Int,
    command_code: String,
    mo_message: String,
    msisdn: String,
    telco: String,
    signature: String
) extends BaseData {
  def checkSign(secret: String): Boolean = {
    val s =
      s"access_key=$access_key&amount=$amount&command_code=$command_code&mo_message=$mo_message&msisdn=$msisdn&telco=$telco"
    val hex = new HmacUtils(HMAC_SHA_256, secret).hmacHex(s)
    hex equalsIgnoreCase signature
  }
}
