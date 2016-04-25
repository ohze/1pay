### Changelogs

##### 2.0.1
+ update sbt 0.13.11, scala 2.11.8, play 2.4.6

##### 2.0.0
ONLY change config key:
`sd.pay.sfs1pay.secret` to `sd.pay.sms1pay.secret`

##### 1.0.4
fix critical error: when check signature, use `hmacSha256Hex(secret, s)` instead of `hmacSha256Hex(s, secret)`

##### 1.0.3
+ add example project
+ add many test cases
+ NOTE: we must use parse.anyContent or the body of parsed request will be empty.
see [example/app/controllers/Sms.scala](example/app/controllers/Sms.scala) 

##### 1.0.2
+ add sd.util.Romanize with test spec (using scalatest)
+ Romanize username on result sms `SmsResult.OkCharged`

##### 1.0.1
Change SmsResult.OkValid's json field `status` from 0 to 1.
As documented at http://developers.1pay.vn/http-apis/smsplus-charging:
Khi trả về status là 1 thì khi đó 1pay mới gọi đến bước 2 của merchant.

##### 1.0.0
First version.
