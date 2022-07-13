package com.couchbase.playground.scala

import org.scalatest.flatspec.AnyFlatSpec

class GatewayOutputTest extends AnyFlatSpec {
  "json output for GatewayOutput with no error" should "only show output" in {
    assertResult("""{"output":"hello"}""")(GatewayOutput(output = "hello").toJsonString)
  }

  "json output for GatewayOutput with no output" should "only show error" in {
    assertResult("""{"error":"oh noez"}""")(GatewayOutput(error = "oh noez").toJsonString)
  }
}
