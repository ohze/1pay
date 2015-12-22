### Changelogs

##### 1.0.2
+ add sd.util.Romanize with test spec (using scalatest)
+ Romanize username on result sms `SmsResult.OkCharged`

##### 1.0.1
Change SmsResult.OkValid's json field `status` from 0 to 1.
As documented at http://developers.1pay.vn/http-apis/smsplus-charging:
Khi trả về status là 1 thì khi đó 1pay mới gọi đến bước 2 của merchant.

##### 1.0.0
First version.
