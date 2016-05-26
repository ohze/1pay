package sd

import play.api.{Configuration, Environment}
import play.api.inject.Module
import sd.pay.sms1pay.{SmsAddCoin, SmsAddCoinImpl}

class PlayModule extends Module {
  def bindings(environment: Environment, configuration: Configuration) = Seq(
    bind[Uid2Name].to[Uid2NameImpl],
    bind[SmsAddCoin].to[SmsAddCoinImpl]
  )
}
