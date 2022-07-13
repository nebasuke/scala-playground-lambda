package com.couchbase.playground.scala

import com.amazonaws.services.lambda.runtime.Context
import com.amazonaws.services.lambda.runtime.events.{APIGatewayV2HTTPEvent, APIGatewayV2HTTPResponse}

import java.io.ByteArrayOutputStream
import java.nio.charset.StandardCharsets
import scala.reflect.runtime.currentMirror
import scala.tools.reflect.ToolBox

class Main {
  def handler(apiGatewayEvent: APIGatewayV2HTTPEvent, context: Context): APIGatewayV2HTTPResponse = {
    if (!apiGatewayEvent.getQueryStringParameters.containsKey("id")) {
      APIGatewayV2HTTPResponse.builder()
        .withStatusCode(400)
        .withBody("Missing query parameter id")
        .build()
    } else {
      val key = apiGatewayEvent.getQueryStringParameters.get("id")
      val credentials = CBCredentials.fromApiGatewayEvent(apiGatewayEvent)
      val resultOrFail = credentials.flatMap(creds => evalFromMap(key, creds))

      resultOrFail match {
        case Left(err) => APIGatewayV2HTTPResponse.builder()
          .withStatusCode(200)
          .withBody(GatewayOutput(error = Some(err)).toJsonString)
          .build()
        case Right(codeResult) => APIGatewayV2HTTPResponse.builder()
          .withStatusCode(200)
          .withBody(GatewayOutput(output = Some(codeResult.output)).toJsonString)
          .build()
      }
    }
  }

  def evalFromMap[A](key: String, credentials: CBCredentials): Either[String, CodeResult[A]] = {
    val codeExample: Either[String, CodeExample] = ExampleMap.exampleMap.get(key).toRight(s"Example does not exists. Key: $key")
    val exampleWithCredentials = codeExample.map(_.replaceCredentials(credentials))

    // To actually run code of the code example, we need to call the main method of the Program object.
    exampleWithCredentials.flatMap(code => evalString(code.fullCode + ";Program.main(new Array[String](0))"))
  }

  def evalString[A](code: String): Either[String, CodeResult[A]] = {
    val out = new ByteArrayOutputStream()
    Console.withOut(out) {
      Console.withErr(out) {
        try {
          val toolbox = currentMirror.mkToolBox()
          val tree = toolbox.parse(code)

          val value = toolbox.eval(tree).asInstanceOf[A]
          val output = out.toString(StandardCharsets.UTF_8)
          Right(CodeResult(output, value))
        }
        catch {
          case ex: Throwable => Left(s"Unable to run code: ${ex.getMessage}")
        }
      }
    }
  }
}
