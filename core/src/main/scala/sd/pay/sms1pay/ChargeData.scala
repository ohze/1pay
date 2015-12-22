package sd.pay.sms1pay

import org.apache.commons.codec.digest.HmacUtils.hmacSha256Hex

/**
 * Data of: Nhận request
 *
 * @param access_key Đại diện cho sản phẩm của merchant khai báo trong hệ thống 1pay.vn
 * @param amount Số tiền trừ vào tài khoản khách hàng.
 * @param command_code Là mã dịch vụ của khách hàng. Đăng ký và khai báo trên hệ thống 1Pay. Ví dụ GAME1
 * @param error_code Là mã lỗi 1pay trả về cho khách hàng. Ví dụ:
 * {{{
 * WCG-0000: Giao dịch thành công
 * WCG-0001: Thuê bao không hợp lệ
 * WCG-0002: Dữ liệu CP gửi lên sai
 * WCG-0005 Tài khoản không đủ tiền
 * }}}
 * @param error_message Thông báo lỗi 1Pay gửi về cho merchant
 * @param mo_message Nội dung tin nhắn của khách hàng ở dạng String .Ví dụ: MI NAP10 dunglp
 * @param msisdn Số điện thoại nhắn tin của khách hàng, theo chuẩn international, bắt đầu bằng 84
 * @param request_id Id của tin nhắn ở dạng String
 * @param request_time Thời gian đầu số nhận được tin nhắn, ở dạng iso, ví dụ: 2013-07-06T22:54:50Z
 * @param signature Chữ ký, merchant có thể sử dụng signature để kiểm soát an ninh.
 * @see [[http://developers.1pay.vn/http-apis/smsplus-charging Kiểm tra cú pháp MO]]
 */
case class ChargeData(
    access_key: String,
    amount: Int,
    command_code: String,
    error_code: String,
    error_message: String,
    mo_message: String,
    msisdn: String,
    request_id: String,
    request_time: String,
    signature: String
) extends DataBase {
  def checkSign(secret: String): Boolean = {
    val s = s"access_key=$access_key&amount=$amount&command_code=$command_code&error_code=$error_code&error_message=$error_message&mo_message=$mo_message&msisdn=$msisdn&request_id=$request_id&request_time=$request_time"
    hmacSha256Hex(s, secret) equalsIgnoreCase signature
  }
  lazy val telco = Phone2Telco(msisdn)
}
