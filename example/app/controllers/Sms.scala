package controllers

import javax.inject.{Inject, Singleton}

import play.api.mvc.InjectedController
import sd.pay.sms1pay.SMS
import scala.concurrent.ExecutionContext

@Singleton
class Sms @Inject() (implicit ec: ExecutionContext, sms1Pay: SMS) extends InjectedController {

  def check() = Action.async(parse.anyContent) { implicit req =>
    sms1Pay.check.map(Ok(_))
  }

  def charge() = Action.async(parse.anyContent) { implicit req =>
    sms1Pay.charge.map(Ok(_))
  }
}
