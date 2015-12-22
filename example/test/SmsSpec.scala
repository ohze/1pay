import play.api.Application
import play.api.db.DB
import play.api.test.{ FakeRequest, WithApplication, PlaySpecification }
import org.apache.commons.codec.digest.HmacUtils.hmacSha256Hex

class SmsSpec extends PlaySpecification {
  "sms1pay" should {
    "check signature error when the Form not contain enough data" in new WithApplication {
      val req = FakeRequest(GET, "/1pay/check")
        .withFormUrlEncodedBody(
          "access_key" -> ""
        )
      val Some(result) = route(req)

      status(result) must equalTo(OK)
      val js = contentAsJson(result)
      (js \ "type").asOpt[String] must beSome("text")
      (js \ "status").asOpt[Int] must beSome(0)
      (js \ "sms").asOpt[String] must beSome.which(_.startsWith("Tin nhan sai cu phap."))
    }

    "check signature error when the signature is not match" in new WithApplication {
      val req = FakeRequest(GET, "/1pay/check")
        .withFormUrlEncodedBody(
          "access_key" -> "access_key",
          "amount" -> "10000",
          "command_code" -> "GAME1",
          "mo_message" -> "MI NAP10 dunglp",
          "msisdn" -> "84988888888",
          "telco" -> "vnp",
          "signature" -> "invalid signature"
        )
      val Some(result) = route(req)

      status(result) must equalTo(OK)
      val js = contentAsJson(result)
      (js \ "type").asOpt[String] must beSome("text")
      (js \ "status").asOpt[Int] must beSome(0)
      (js \ "sms").asOpt[String] must beSome.which(_.startsWith("Tin nhan sai cu phap."))
    }

    "check signature error when can't get uid from mo_message" in new WithApplication {
      val req = FakeRequest(GET, "/1pay/check")
        .withFormUrlEncodedBody(dataWithSign(
          "access_key" -> "access_key",
          "amount" -> "10000",
          "command_code" -> "GAME1",
          "mo_message" -> "MI NAP10 dunglp",
          "msisdn" -> "84988888888",
          "telco" -> "vnp"
        ): _*)
      val Some(result) = route(req)

      status(result) must equalTo(OK)
      val js = contentAsJson(result)
      (js \ "type").asOpt[String] must beSome("text")
      (js \ "status").asOpt[Int] must beSome(1)
      (js \ "sms").asOpt[String] must beSome.which(_.startsWith("Tin nhan dung cu phap."))
    }

    "check signature error when user id not exist" in new WithApplication {
      val req = FakeRequest(GET, "/1pay/check")
        .withFormUrlEncodedBody(dataWithSign(
          "access_key" -> "access_key",
          "amount" -> "10000",
          "command_code" -> "GAME1",
          "mo_message" -> "MI NAP10 99999999",
          "msisdn" -> "84988888888",
          "telco" -> "vnp"
        ): _*)
      val Some(result) = route(req)

      status(result) must equalTo(OK)
      val js = contentAsJson(result)
      (js \ "type").asOpt[String] must beSome("text")
      (js \ "status").asOpt[Int] must beSome(1)
      (js \ "sms").asOpt[String] must beSome.which(_.startsWith("Tin nhan dung cu phap."))
    }

    "check ok" in new WithApplication {
      ensureUser1
      val req = FakeRequest(GET, "/1pay/check")
        .withFormUrlEncodedBody(dataWithSign(
          "access_key" -> "access_key",
          "amount" -> "10000",
          "command_code" -> "GAME1",
          "mo_message" -> "MI NAP10 1",
          "msisdn" -> "84988888888",
          "telco" -> "vnp"
        ): _*)
      val Some(result) = route(req)

      status(result) must equalTo(OK)
      val js = contentAsJson(result)
      (js \ "type").asOpt[String] must beSome("text")
      (js \ "status").asOpt[Int] must beSome(1)
      (js \ "sms").asOpt[String] must beSome.which(_.startsWith("Tin nhan dung cu phap."))
    }
  }

  private def ensureUser1(implicit app: Application): Unit = {
    import anorm._
    import anorm.SqlParser._
    DB.withConnection { implicit conn =>
      SQL"""INSERT INTO users(id, coin, username)
            VALUES (1, 5000000000, 'Trần Văn Nguyễn')
            ON DUPLICATE KEY UPDATE coin = 5000000000
        """.executeUpdate()
    }
  }

  private def dataWithSign(d: (String, String)*)(implicit app: Application): Seq[(String, String)] = {
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
