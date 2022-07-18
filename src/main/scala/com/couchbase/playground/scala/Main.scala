package com.couchbase.playground.scala

import com.amazonaws.services.lambda.runtime.Context
import com.amazonaws.services.lambda.runtime.events.{APIGatewayV2HTTPEvent, APIGatewayV2HTTPResponse}

import java.io.ByteArrayOutputStream
import java.nio.charset.StandardCharsets
import scala.reflect.runtime.currentMirror
import scala.tools.reflect.ToolBox
import scala.util.Try

class Main {
  def handler(apiGatewayEvent: APIGatewayV2HTTPEvent, context: Context): APIGatewayV2HTTPResponse = {
    if (!apiGatewayEvent.getQueryStringParameters.containsKey("id")) {
      APIGatewayV2HTTPResponse.builder()
        .withStatusCode(400)
        .withBody("Missing query parameter id")
        .build()
    } else {
      val key = apiGatewayEvent.getQueryStringParameters.get("id")
      val credentials: Either[CodeResult, CBCredentials] = CBCredentials.fromApiGatewayEvent(apiGatewayEvent).left.map(CodeResult.fromErrorMessage)
      val resultOrFail = credentials.flatMap(creds => evalFromMap(key, creds))

      resultOrFail match {
        case Left(err) => APIGatewayV2HTTPResponse.builder()
          .withStatusCode(200)
          .withBody(err.toJsonString)
          .build()
        case Right(codeResult) => APIGatewayV2HTTPResponse.builder()
          .withStatusCode(200)
          .withBody(codeResult.toJsonString)
          .build()
      }
    }
  }

  def getHandler(apiGatewayEvent: APIGatewayV2HTTPEvent, context: Context): APIGatewayV2HTTPResponse = {
    if (!apiGatewayEvent.getQueryStringParameters.containsKey("id")) {
      APIGatewayV2HTTPResponse.builder()
        .withStatusCode(400)
        .withBody("Missing query parameter id")
        .build()
    } else {
      val key = apiGatewayEvent.getQueryStringParameters.get("id")
      val codeExample: Either[String, CodeExample] = ExampleMap.getExample(key)
      codeExample match {
        case Left(err) => APIGatewayV2HTTPResponse.builder()
          .withStatusCode(200)
          .withBody(CodeResult.fromErrorMessage(err).toJsonString)
          .build()
        case Right(codeExample) => APIGatewayV2HTTPResponse.builder()
          .withStatusCode(200)
          .withBody(codeExample.toJsonString)
          .build()
      }
    }
  }

  def evalFromMap[A](key: String, credentials: CBCredentials): Either[CodeResult, CodeResult] = {
    val codeExample: Either[CodeResult, CodeExample] = ExampleMap.getExample(key).left.map(CodeResult.fromErrorMessage)
    val exampleWithCredentials = codeExample.map(_.replaceCredentials(credentials))

    // To actually run code of the code example, we need to call the main method of the Program object.
    exampleWithCredentials.flatMap(code => evalString(code.fullCode + ";Program.main(new Array[String](0))"))
  }

  def evalString[A](code: String): Either[CodeResult, CodeResult] = {
    val out = new ByteArrayOutputStream()
    Console.withOut(out) {
      Console.withErr(out) {
        val toolbox = currentMirror.mkToolBox()
        val tree = Try(toolbox.parse(code)).toEither.left.map(CodeResult.fromCompilationError)
        val compiledProgram: Either[CodeResult, () => Any] = tree.flatMap(t => Try(toolbox.compile(t)).toEither.left.map(CodeResult.fromCompilationError))
        compiledProgram.flatMap(cp => Try(CodeResult.fromSuccess(cp(), out.toString(StandardCharsets.UTF_8))).toEither.left.map(t => CodeResult.fromEvalError(t, out.toString(StandardCharsets.UTF_8))))
      }
    }
  }
}
