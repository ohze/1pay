package sd.pay.sms1pay

/** @deprecated ứ đúng nữa - giờ VN cho chuyển mạng đổi số :D */
object Phone2Telco {
  private val Prefixes = Seq(
    "viettel" -> Seq(
      "96",
      "97",
      "98",
      "162",
      "163",
      "164",
      "165",
      "166",
      "167",
      "168",
      "169"
    ),
    "vina" -> Seq("91", "94", "123", "124", "125", "127", "129"),
    "vms" -> Seq("90", "93", "120", "121", "122", "126", "128")
  ).map { case (tpe, prefixes) =>
    tpe -> prefixes.map("84" + _)
  }

  def apply(phone: String): String = Prefixes
    .find { case (_, prefixes) =>
      prefixes.exists(phone.startsWith)
    }
    .fold("na")(_._1)
}
