package com.couchbase.playground.scala

import com.couchbase.playground.scala.CodeExample.{BUCKET, CONNECTION_STRING, PASSWORD, USERNAME}
import org.scalatest.flatspec.AnyFlatSpec

class CodeExampleTest extends AnyFlatSpec {

  "code example without variables" should "stay the same" in {
    val codeExample = CodeExample(snippet = "", "3 + 4")

    val newSample = codeExample.replacePlaceholder("{var1}", "5")

    assertResult(codeExample)(newSample)
  }

  "code example with variable" should "change the variable" in {
    val codeExample = CodeExample(snippet = "", "3 + {var1}")

    val newSample = codeExample.replacePlaceholder("{var1}", "5")

    assertResult(CodeExample("", "3 + 5"))(newSample)
  }

  "code example with variable" should "not change different variable" in {
    val codeExample = CodeExample(snippet = "", "3 + {var1}")

    val newExample = codeExample.replacePlaceholder("{var2}", "5")

    assertResult(CodeExample("", "3 + {var1}"))(newExample)
  }

  "code example with multiple variables" should "change all the variable" in {
    val codeExample = CodeExample(snippet = "", "3 + {var1} + {var1}")

    val newExample = codeExample.replacePlaceholder("{var1}", "5")

    assertResult(CodeExample("", "3 + 5 + 5"))(newExample)
  }

  "code example with credential variables" should "change all credential variables" in {
    val codeExample = CodeExample(snippet = "", s"$CONNECTION_STRING-$USERNAME-$PASSWORD-$BUCKET")
    val credentials = CBCredentials(connectionString = "127.0.0.1", username = "user", password = "password", bucketName = "travel-sample")

    val newExample = codeExample.replaceCredentials(credentials)

    assertResult(CodeExample(snippet = "", "127.0.0.1-user-password-travel-sample"))(newExample)
  }
}
