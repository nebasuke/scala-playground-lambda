package com.couchbase.playground.scala

import upickle.default

case class GatewayOutput(error: String = "", output: String = "") {
  def toJsonString: String = {
    upickle.default.write(this)
  }
}

object GatewayOutput {
  implicit val GatewayOutputRW: default.ReadWriter[GatewayOutput] = upickle.default.macroRW
}
