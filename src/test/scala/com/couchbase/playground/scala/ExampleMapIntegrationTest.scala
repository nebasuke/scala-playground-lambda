package com.couchbase.playground.scala

import org.scalatest.flatspec.AnyFlatSpec

// This class needs Couchbase Server to be installed locally, with the following assumptions:
// - running on 127.0.0.1
// - with a user Administrator and password = "password"
// - travel-sample has been imported
class ExampleMapIntegrationTest extends AnyFlatSpec {
  val m = new Main()

  "Evaluate code from ExampleMap" should "return expected result" in {
    val credentials = CBCredentials("127.0.0.1", "Administrator", "password", "travel-sample")

    val output = m.evalFromMap("KV_GET", credentials)

    assert(output.isRight)
    assert(output.toOption.get.output.contains("""{"country":"United States","iata":"Q5","name":"40-Mile Air","callsign":"MILE-AIR","icao":"MLA","id":10,"type":"airline"}"""))
  }
}
