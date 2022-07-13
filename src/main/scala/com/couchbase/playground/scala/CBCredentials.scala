package com.couchbase.playground.scala

import com.amazonaws.services.lambda.runtime.events.APIGatewayV2HTTPEvent
import com.couchbase.client.scala.implicits.Codec
import upickle.default

import scala.util.Try

case class CBCredentials(connectionString: String, username: String, password: String, bucketName: String)

object CBCredentials {
  implicit val codec: Codec[CBCredentials] = Codec.codec[CBCredentials]

  def readFromString(s: String): Either[String, CBCredentials] = {
    implicit val cbCredentialsRW: default.ReadWriter[CBCredentials] = upickle.default.macroRW
    Try(upickle.default.read(s)).toEither.left.map(_.getMessage)
  }

  def fromApiGatewayEvent(apiGatewayEvent: APIGatewayV2HTTPEvent): Either[String, CBCredentials] = {
    readFromString(apiGatewayEvent.getBody)
  }
}