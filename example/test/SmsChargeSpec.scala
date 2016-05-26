import play.api.db.Database
import play.api.test.{FakeRequest, PlaySpecification, WithApplication}
import sd.Uid2Name

class SmsChargeSpec extends PlaySpecification {
  "/1pay/charge" should {
    "signature error when the Form not contain enough data" in new WithApplication {
      val req = FakeRequest(GET, "/1pay/charge")
        .withFormUrlEncodedBody(
          "access_key" -> ""
        )
      val Some(result) = route(app, req)

      status(result) must equalTo(OK)
      val js = contentAsJson(result)
      (js \ "type").asOpt[String] must beSome("text")
      (js \ "status").asOpt[Int] must beSome(0)
      (js \ "sms").asOpt[String] must beSome.which(_.startsWith("Tin nhan sai cu phap."))
    }

    "signature error when the signature is not match" in new WithApplication {
      val req = FakeRequest(GET, "/1pay/charge")
        .withFormUrlEncodedBody(
          "access_key" -> "access_key",
          "amount" -> "10000",
          "command_code" -> "GAME1",
          "error_code" -> "error_code",
          "error_message" -> "error_message",
          "mo_message" -> "MI NAP10 dunglp",
          "msisdn" -> "84988888888",
          "request_id" -> "request_id",
          "request_time" -> "2013-07-06T22:54:50Z",
          "signature" -> "invalid signature"
        )
      val Some(result) = route(app, req)

      status(result) must equalTo(OK)
      val js = contentAsJson(result)
      (js \ "type").asOpt[String] must beSome("text")
      (js \ "sms").asOpt[String] must beSome.which(_.startsWith("Tin nhan sai cu phap."))
      (js \ "status").asOpt[Int] must beSome(0)
    }

    "signature error when can't get uid from mo_message" in new WithApplication {
      val req = FakeRequest(GET, "/1pay/charge")
        .withFormUrlEncodedBody(SignData(
          "access_key" -> "access_key",
          "amount" -> "10000",
          "command_code" -> "GAME1",
          "error_code" -> "error_code",
          "error_message" -> "error_message",
          "mo_message" -> "MI NAP10 dunglp",
          "msisdn" -> "84988888888",
          "request_id" -> "request_id",
          "request_time" -> "2013-07-06T22:54:50Z"
        ): _*)
      val Some(result) = route(app, req)

      status(result) must equalTo(OK)
      val js = contentAsJson(result)
      (js \ "type").asOpt[String] must beSome("text")
      (js \ "sms").asOpt[String] must beSome.which(_.startsWith("Tin nhan sai cu phap."))
      (js \ "status").asOpt[Int] must beSome(0)
    }

    "ErrId when user id not exist" in new WithApplication {
      val uid = 99999999
      val req = FakeRequest(GET, "/1pay/charge")
        .withFormUrlEncodedBody(SignData(
          "access_key" -> "access_key",
          "amount" -> "10000",
          "command_code" -> "GAME1",
          "error_code" -> "error_code",
          "error_message" -> "error_message",
          "mo_message" -> s"MI NAP10 $uid",
          "msisdn" -> "84988888888",
          "request_id" -> "request_id",
          "request_time" -> "2013-07-06T22:54:50Z"
        ): _*)
      val Some(result) = route(app, req)

      status(result) must equalTo(OK)
      val js = contentAsJson(result)
      (js \ "type").asOpt[String] must beSome("text")
      (js \ "sms").asOpt[String] must beSome.which(_.startsWith(s"So ID: $uid khong hop le."))
      (js \ "status").asOpt[Int] must beSome(0)
    }

    "Ok then ErrProcessed" in new WithApplication {
      import anorm._, SqlParser._
      val db = app.injector.instanceOf[Database]
      val reqId = "request_id_1"
      db.withConnection { implicit conn =>
        SQL"DELETE FROM 1pay_log WHERE request_id = $reqId".execute()
      }

      EnsureUser1.run(db)
      val uid2Name = app.injector.instanceOf[Uid2Name]
      uid2Name(1) must beSome("Trần Văn Nguyễn")

      val uid = 1

      val req = FakeRequest(GET, "/1pay/charge")
        .withFormUrlEncodedBody(SignData(
          "access_key" -> "access_key",
          "amount" -> "10000",
          "command_code" -> "GAME1",
          "error_code" -> "error_code",
          "error_message" -> "error_message",
          "mo_message" -> s"MI NAP10 $uid",
          "msisdn" -> "84988888888",
          "request_id" -> reqId,
          "request_time" -> "2013-07-06T22:54:50Z"
        ): _*)
      val Some(result) = route(app, req)

      status(result) must equalTo(OK)
      val js = contentAsJson(result)
      (js \ "type").asOpt[String] must beSome("text")
      (js \ "sms").asOpt[String] must beSome.which(_.startsWith("Ban da nap thanh cong 1.000.000 Bao vao tai khoan: Tran Van Nguyen."))
      (js \ "status").asOpt[Int] must beSome(1)

      val Some(result2) = route(app, req)

      status(result2) must equalTo(OK)
      val js2 = contentAsJson(result2)
      (js2 \ "type").asOpt[String] must beSome("text")
      (js2 \ "sms").asOpt[String] must beSome.which(_.startsWith("Tin nhan da duoc xu ly."))
      (js2 \ "status").asOpt[Int] must beSome(0)

      val coin = db.withConnection { implicit conn =>
        SQL"SELECT coin FROM users WHERE id = $uid".as(scalar[Long].singleOpt)
      }
      coin must beSome(5000000000L + 1000000)
    }
  }
}
