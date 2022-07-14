package com.couchbase.playground.scala

import com.couchbase.playground.scala.CodeExample.{BUCKET, CONNECTION_STRING, PASSWORD, USERNAME}
import upickle.default

case class CodeExample(snippet: String, fullCode: String) {
  def replacePlaceholder(variable: String, value: String): CodeExample = {
    CodeExample(snippet, fullCode.replace(variable, value))
  }

  def replaceCredentials(credentials: CBCredentials): CodeExample = credentials match {
    case CBCredentials(connectionString, username, password, bucketName) =>
      this
        .replacePlaceholder(CONNECTION_STRING, connectionString)
        .replacePlaceholder(USERNAME, username)
        .replacePlaceholder(PASSWORD, password)
        .replacePlaceholder(BUCKET, bucketName)
  }

  def toJsonString: String = {
    upickle.default.write(this)
  }
}

object CodeExample {
  val CONNECTION_STRING = "{credentials-connectionString}"
  val USERNAME = "{credentials-username}"
  val PASSWORD = "{credentials-password}"
  val BUCKET = "{credentials-bucket}"

  implicit val codeExampleRW: default.ReadWriter[CodeExample] = upickle.default.macroRW
}
