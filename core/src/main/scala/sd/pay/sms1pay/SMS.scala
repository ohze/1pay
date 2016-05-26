package sd.pay.sms1pay

import javax.inject.{Inject, Singleton}
import play.api.data.Form
import play.api.db.Database
import play.api.Logger
import play.api.libs.json.JsObject
import play.api.mvc.Request
import sd.Uid2Name
import scala.concurrent.Future
import scala.util.Failure
import play.api.libs.concurrent.Execution.Implicits._

@Singleton
class SMS @Inject() (
    forms1pay: Forms1pay,
    uid2Name:  Uid2Name,
    addCoin:   SmsAddCoin,
    smsRes:    SmsResult,
    db:        Database
) {
  private[this] val logger = Logger("sms1pay")
  import smsRes._

  private def check[T <: BaseData](form: Form[T])(logic: (Int, String, T) => Future[JsObject]) //uid, username, data
  (implicit req: Request[_]): Future[JsObject] = {
    form.bindFromRequest.fold(
      f => {
        logger.warn(
          s"""${f.errors.map(e => e.key + ":" + e.messages.mkString(",")).mkString("FormError: [", ",", "]")}
             |${req.headers.headers.map { case (k, v) => k + ":" + v }.mkString("Headers: [", ",", "]")}
             |${req.uri}""".stripMargin
        )
        ErrSignature
      },
      d => d.uidOpt.fold(ErrSignature) { uid =>
        uid2Name(uid).fold(ErrId(uid)) { username =>
          logic(uid, username, d)
        }
      }
    )
  }

  def check(implicit req: Request[_]): Future[JsObject] =
    check(forms1pay.formCheck) { (_, _, _) =>
      OkValid
    }

  private def chargeLogic(uid: Int, username: String, d: ChargeData): Future[JsObject] =
    db.withConnection { implicit conn =>
      if (Anorm1pay.exists(d.request_id)) ErrProcessed
      else {
        Anorm1pay.insertLog(d, uid)
        addCoin(uid, d.amount, s"Nạp Bảo qua SMS từ số ${d.msisdn} số tiền ${d.amount}")
          .map(OkCharged(username, _))
          .andThen {
            case Failure(e) => logger.error(d.toString, e)
          }
      }
    }

  def charge(implicit req: Request[_]): Future[JsObject] =
    check(forms1pay.formCharge) { (uid, username, d) =>
      try chargeLogic(uid, username, d)
      catch {
        case e: Exception =>
          logger.error(req.queryString.toString, e)
          ErrInternal
      }
    }
}
