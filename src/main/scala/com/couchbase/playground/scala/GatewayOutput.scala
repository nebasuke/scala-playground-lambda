package com.couchbase.playground.scala

import io.circe.{Decoder, Encoder}
import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}

case class GatewayOutput(error: Option[String] = None, output: Option[String] = None) {
  def toJsonString: String = {
    import io.circe.syntax._
    this.asJson.noSpaces
  }
}

object GatewayOutput {
  implicit val gatewayOutputDecoder: Decoder[GatewayOutput] = deriveDecoder
  implicit val gatewayOutputEncoder: Encoder[GatewayOutput] = deriveEncoder
}
