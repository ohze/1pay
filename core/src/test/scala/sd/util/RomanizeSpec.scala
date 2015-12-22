package sd.util

import org.scalatest.{ Matchers, FlatSpec }

class RomanizeSpec extends FlatSpec with Matchers {
  val R2 = Map(
    'À' -> 'A', 'Á' -> 'A', 'Ạ' -> 'A', 'Ả' -> 'A',
    'Ă' -> 'A', 'Â' -> 'A',
    'Ằ' -> 'A', 'Ắ' -> 'A', 'Ặ' -> 'A', 'Ẳ' -> 'A',
    'Ầ' -> 'A', 'Ấ' -> 'A', 'Ậ' -> 'A', 'Ẩ' -> 'A',
    'à' -> 'a', 'á' -> 'a', 'ạ' -> 'a', 'ả' -> 'a',
    'ă' -> 'a', 'â' -> 'a',
    'ằ' -> 'a', 'ắ' -> 'a', 'ặ' -> 'a', 'ẳ' -> 'a',
    'ầ' -> 'a', 'ấ' -> 'a', 'ậ' -> 'a', 'ẩ' -> 'a',
    'Đ' -> 'D', 'đ' -> 'd',
    'È' -> 'E', 'É' -> 'E', 'Ẹ' -> 'E', 'Ẻ' -> 'E',
    'Ê' -> 'E', 'ê' -> 'e',
    'Ề' -> 'E', 'Ế' -> 'E', 'Ệ' -> 'E', 'Ể' -> 'E',
    'è' -> 'e', 'é' -> 'e', 'ẹ' -> 'e', 'ẻ' -> 'e',
    'ề' -> 'e', 'ế' -> 'e', 'ệ' -> 'e', 'ể' -> 'e',
    'Ì' -> 'I', 'Í' -> 'I', 'Ị' -> 'I', 'Ỉ' -> 'I',
    'ì' -> 'i', 'í' -> 'i', 'ị' -> 'i', 'ỉ' -> 'i',
    'Ò' -> 'O', 'Ó' -> 'O', 'Ọ' -> 'O', 'Ỏ' -> 'O',
    'Ờ' -> 'O', 'Ớ' -> 'O', 'Ợ' -> 'O', 'Ở' -> 'O',
    'Ồ' -> 'O', 'Ố' -> 'O', 'Ộ' -> 'O', 'Ổ' -> 'O',
    'ò' -> 'o', 'ó' -> 'o', 'ọ' -> 'o', 'ỏ' -> 'o',
    'ờ' -> 'o', 'ớ' -> 'o', 'ợ' -> 'o', 'ở' -> 'o',
    'ồ' -> 'o', 'ố' -> 'o', 'ộ' -> 'o', 'ổ' -> 'o',
    'Ơ' -> 'O', 'Ô' -> 'O', 'ơ' -> 'o', 'ô' -> 'o',
    'Ù' -> 'U', 'Ú' -> 'U', 'Ụ' -> 'U', 'Ủ' -> 'U',
    'Ừ' -> 'U', 'Ứ' -> 'U', 'Ự' -> 'U', 'Ử' -> 'U',
    'ù' -> 'u', 'ú' -> 'u', 'ụ' -> 'u', 'ủ' -> 'u',
    'ừ' -> 'u', 'ứ' -> 'u', 'ự' -> 'u', 'ử' -> 'u',
    'Ý' -> 'Y', 'Ỳ' -> 'Y', 'Ỵ' -> 'Y', 'Ỷ' -> 'Y'
  )

  "Romanize" should "have RomanizedChars" in {
    import org.scalatest.PrivateMethodTester._
    val RomanizedChars = Romanize.invokePrivate(PrivateMethod[Map[Char, Char]]('RomanizedChars)())
    //TODO pls asserting. not just printing
    RomanizedChars.size should be > R2.size
    R2.size shouldBe 104
    RomanizedChars.filterNot(R2.toSeq.contains).keys should contain theSameElementsAs "ỡẪỮễỖýĩũỳỗỷŨĨẴõÕỸẽỹƯỄữỠẼưẫÃỵãẵ"
  }
}
