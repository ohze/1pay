package controllers

import javax.inject.{Inject, Singleton}
import play.api.mvc.{Action, Controller}
import sd.pay.sms1pay.SMS
import play.api.libs.concurrent.Execution.Implicits.defaultContext

@Singleton
class Sms @Inject() (sms1Pay: SMS) extends Controller {

  def check() = Action.async(parse.anyContent) { implicit req =>
    sms1Pay.check.map(Ok(_))
  }

  def charge() = Action.async(parse.anyContent) { implicit req =>
    sms1Pay.charge.map(Ok(_))
  }
}
