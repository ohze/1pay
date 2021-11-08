package sd.util

import scala.collection.mutable

object Romanize {
  private val RomanizedChars: Map[Char, Char] = Map(
    "ÀÁẢÃẠĂẰẮẲẴẶÂẦẤẨẪẬ" -> 'A',
    "àáảãạăằắẳẵặâầấẩẫậ" -> 'a',
    "Đ" -> 'D',
    "đ" -> 'd',
    "ÈÉẺẼẸÊỀẾỂỄỆ" -> 'E',
    "èéẻẽẹêềếểễệ" -> 'e',
    "ÌÍỈĨỊ" -> 'I',
    "ìíỉĩị" -> 'i',
    "ÒÓỎÕỌÔỒỐỔỖỘƠỜỚỞỠỢ" -> 'O',
    "òóỏõọôồốổỗộơờớởỡợ" -> 'o',
    "ÙÚỦŨỤƯỪỨỬỮỰ" -> 'U',
    "ùúủũụưừứửữự" -> 'u',
    "ỲÝỶỸỴ" -> 'Y',
    "ỳýỷỹỵ" -> 'y'
  ).foldLeft(mutable.Map.empty[Char, Char]) { case (ret, (s, c)) =>
    s.foreach(k => ret(k) = c); ret
  }.toMap

  def apply(s: String) = s.map(c => RomanizedChars.getOrElse(c, c))
}
