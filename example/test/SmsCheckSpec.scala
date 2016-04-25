import play.api.test.{FakeRequest, WithApplication, PlaySpecification}
import sd.Uid2Name
import sd.pay.sms1pay.{CheckData, Forms1pay}

class SmsCheckSpec extends PlaySpecification {
  "/1pay/check" should {
    "signature error when the Form not contain enough data" in new WithApplication {
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

    "signature error when the signature is not match" in new WithApplication {
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
      (js \ "sms").asOpt[String] must beSome.which(_.startsWith("Tin nhan sai cu phap."))
      (js \ "status").asOpt[Int] must beSome(0)
    }

    "signature error when can't get uid from mo_message" in new WithApplication {
      val req = FakeRequest(GET, "/1pay/check")
        .withFormUrlEncodedBody(SignData(
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
      (js \ "sms").asOpt[String] must beSome.which(_.startsWith("Tin nhan sai cu phap."))
      (js \ "status").asOpt[Int] must beSome(0)
    }

    "ErrId when user id not exist" in new WithApplication {
      val uid = 99999999
      val req = FakeRequest(GET, "/1pay/check")
        .withFormUrlEncodedBody(SignData(
          "access_key" -> "access_key",
          "amount" -> "10000",
          "command_code" -> "GAME1",
          "mo_message" -> s"MI NAP10 $uid",
          "msisdn" -> "84988888888",
          "telco" -> "vnp"
        ): _*)
      val Some(result) = route(req)

      status(result) must equalTo(OK)
      val js = contentAsJson(result)
      (js \ "type").asOpt[String] must beSome("text")
      (js \ "sms").asOpt[String] must beSome.which(_.startsWith(s"So ID: $uid khong hop le."))
      (js \ "status").asOpt[Int] must beSome(0)
    }

    "ok" in new WithApplication {
      EnsureUser1.run()
      val uid2Name = app.injector.instanceOf[Uid2Name]
      uid2Name(1) must beSome("Trần Văn Nguyễn")

      val req = FakeRequest(GET, "/1pay/check")
        .withFormUrlEncodedBody(SignData(
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
      (js \ "sms").asOpt[String] must beSome.which(_.startsWith("Tin nhan dung cu phap."))
      (js \ "status").asOpt[Int] must beSome(1)
    }

    "ok with real data" in new WithApplication {
      // val signature = "7e53f375e176fe70e45b249094b9b235eabd8213e188cbb9b94cd5f070f88566"
      val signature = "3c5aa7b2f8254db41fe1cc7d4980003f93316e8adc4271a113bb132c556491ed"
      SignData.sign(
        "access_key" -> "4l732pw3tjhzexml8wry",
        "amount" -> "5000",
        "command_code" -> "SD",
        "mo_message" -> "SD NAP 14",
        "msisdn" -> "84982479988",
        "telco" -> "vtm"
      ) must be_==(signature)

      EnsureUser1.run(14, "Kiều Phong")
      val uid2Name = app.injector.instanceOf[Uid2Name]
      uid2Name(14) must beSome("Kiều Phong")

      implicit val req = FakeRequest(GET, s"/1pay/check?access_key=4l732pw3tjhzexml8wry&amount=5000&command_code=SD&mo_message=SD+NAP+14&msisdn=84982479988&signature=$signature&telco=vtm")
      val forms1pay = app.injector.instanceOf[Forms1pay]
      val form = forms1pay.formCheck.bindFromRequest
      form.value must beSome[CheckData]
      form.errors must beEmpty

      val Some(result) = route(req)

      status(result) must equalTo(OK)
      val js = contentAsJson(result)
      (js \ "type").asOpt[String] must beSome("text")
      (js \ "sms").asOpt[String] must beSome.which(_.startsWith("Tin nhan dung cu phap."))
      (js \ "status").asOpt[Int] must beSome(1)
    }
  }
}
