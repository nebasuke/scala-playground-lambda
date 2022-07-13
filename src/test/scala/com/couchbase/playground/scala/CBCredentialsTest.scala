package com.couchbase.playground.scala

import org.scalatest.flatspec.AnyFlatSpec

class CBCredentialsTest extends AnyFlatSpec {

  "valid credentials" should "parse successfully" in {
    val credentials =
      """{
        |  "connectionString": "127.0.0.1",
        |  "username": "my-user",
        |  "password": "super-secret",
        |  "bucketName": "travel-sample"
        |}""".stripMargin

    val parsedCredentials = CBCredentials.readFromString(credentials)

    assertResult(Right(CBCredentials("127.0.0.1", "my-user", "super-secret", "travel-sample")))(parsedCredentials)
  }

  "invalid json" should "give parsing error" in {
    val invalidJson = "{ ss: ss"

    assert(CBCredentials.readFromString(invalidJson).isLeft)
  }
}
