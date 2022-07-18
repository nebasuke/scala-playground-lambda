package com.couchbase.playground.scala

import org.scalatest.flatspec.AnyFlatSpec

class CodeResultTest extends AnyFlatSpec {
  "json output for CodeResult with no errors" should "only show output" in {
    assertResult("""{"isSuccessful":true,"output":"hello"}""")(CodeResult(isSuccessful = true, output = "hello").toJsonString)
  }

  "json output for CodeResult with no output" should "only show exception" in {
    assertResult("""{"isSuccessful":false,"exception":"oh noez"}""")(CodeResult(isSuccessful = false, exception = "oh noez").toJsonString)
  }
}
