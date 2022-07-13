package com.couchbase.playground.scala

import com.amazonaws.services.lambda.runtime.events.APIGatewayV2HTTPEvent
import io.circe.{Decoder, Encoder}
import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}
import io.circe.parser._

case class CBCredentials(connectionString: String, username: String, password: String, bucketName: String)

object CBCredentials {
  implicit val credentialDecoder: Decoder[CBCredentials] = deriveDecoder
  implicit val credentialEncoder: Encoder[CBCredentials] = deriveEncoder

  def readFromString(s: String): Either[String, CBCredentials] = {
    parse(s) match {
      case Left(pf) => Left(pf.message)
      case Right(json) => json.as[CBCredentials].left.map(_.message)
    }
  }

  def fromApiGatewayEvent(apiGatewayEvent: APIGatewayV2HTTPEvent): Either[String, CBCredentials] = {
    readFromString(apiGatewayEvent.getBody)
  }
}