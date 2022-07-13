package com.couchbase.playground.scala

import org.scalatest.flatspec.AnyFlatSpec

class RunTest extends AnyFlatSpec {
  val m = new Main()

  "Evaluating simple code" should "return expected result" in {
    assertResult(Right(CodeResult("", 42)))(m.evalString("13 + 29"))
  }

  "Evaluating non-compiling code" should "return error message" in {
    assert(m.evalString("13 + 29 + ").isLeft)
  }

  "Evaluate code requiring Couchbase jars" should "return expected result" in {
    val code = """    import com.couchbase.client.core.error.CouchbaseException
                 |    val ce = new CouchbaseException("Hello")
                 |    print(ce.getMessage)
                 |    ce.getMessage
                 |""".stripMargin

    assertResult(Right(CodeResult("Hello", "Hello")))(m.evalString(code))
  }
}
