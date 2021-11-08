[scala](http://scala-lang.org/)/ [play](https://playframework.com/) library for working with [1pay](http://developers.1pay.vn/http-apis)
=========================
[![CI](https://github.com/ohze/1pay/actions/workflows/sbt-devops.yml/badge.svg)](https://github.com/ohze/1pay/actions/workflows/sbt-devops.yml)

### Install
1pay is [published](http://search.maven.org/#search|ga|1|g%3A%22com.sandinh%22%201pay) to maven center.

add to build.sbt:
`libraryDependencies += "com.sandinh" %% "1pay" % 1payVersion`

### Usage
1. implement `sd.Uid2Name`, `sd.pay.sms1pay.SmsAddCoin`

2. set `sd.pay.sms1pay.secret` in your play's `application.conf`

3. define `check` & `charge` actions in your play's Controller
```
class Sms @Inject() (sms1Pay: SMS) {
    def check =  Action.async { implicit req =>
        sms1Pay.check.map(Ok(_))
    }
    def charge =  Action.async { implicit req =>
        sms1Pay.charge.map(Ok(_))
    }
}
```

### Changelog
see [CHANGES.md](CHANGES.md)

### Licence
This software is licensed under the Apache 2 license:
http://www.apache.org/licenses/LICENSE-2.0

Copyright 2015-2109 Sân Đình (https://sandinh.com)
