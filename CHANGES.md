### Changelog

##### 2.4.0
+ add scala 2.13, drop 2.11
+ update play 2.8, anorm 2.6.5
+ commons-codec as a `1pay`'s dependency
+ update sbt 1.3.5, scalatest 3.1.0
+ deprecate `Phone2Telco`

##### 2.3.0
+ update play 2.6.3, sbt 0.13.16
+ cross compile for scala 2.12.3, 2.11.11
+ use sbt-coursier & update sbt plugins
+ use sbt-scalafmt-coursier instead of sbt-scalariform
+ move source code to github.com/ohze/1pay

##### 2.2.0
+ update play 2.5.3
+ travis test with oraclejdk8 & openjdk8
+ fix all deprecated warnings
+ break changes:
    + remove `implicit app: Application` param from some methods of SMS class
    + rename trait DataBase -> BaseData for better meaning

##### 2.1.0
+ update play 2.5.2, anorm 2.5.1

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
