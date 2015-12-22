import play.api.test.{ FakeRequest, WithApplication, PlaySpecification }

class SmsSpec extends PlaySpecification {
  "sms1pay" should {
    "check" in new WithApplication {
      val req = FakeRequest(GET, "/1pay/check")
        .withFormUrlEncodedBody(
          "access_key" -> ""
        )
      val Some(result) = route(req)

      status(result) must equalTo(OK)
      val js = contentAsJson(result)
      (js \ "type").asOpt[String] must beSome("text")
      (js \ "status").asOpt[Int] must beSome(0)
      (js \ "sms").asOpt[String] must beSome("Tin nhan sai cu phap")
    }
  }
}
