package com.couchbase.playground.scala

import com.amazonaws.services.lambda.runtime.Context
import com.amazonaws.services.lambda.runtime.events.{APIGatewayV2HTTPEvent, APIGatewayV2HTTPResponse}

import scala.reflect.runtime.currentMirror
import scala.tools.reflect.ToolBox

class Main {
  def handler(apiGatewayEvent: APIGatewayV2HTTPEvent, context: Context): APIGatewayV2HTTPResponse = {
    println(s"body = ${apiGatewayEvent.getBody}")
    APIGatewayV2HTTPResponse.builder
      .withStatusCode(200)
      .withBody("okay")
      .build
  }

  def evalString[A](code: String): Either[String, A] = {
    try {
      val toolbox = currentMirror.mkToolBox()
      val tree = toolbox.parse(code)
      Right(toolbox.eval(tree).asInstanceOf[A])
    }
    catch {
      case ex: Throwable => Left(s"Unable to run code: ${ex.getMessage}")
    }
  }

}