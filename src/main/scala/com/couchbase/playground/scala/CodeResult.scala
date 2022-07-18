package com.couchbase.playground.scala

import upickle.default

case class CodeResult(isSuccessful: Boolean, output: String = "", exception: String = "", compilationError: String = "") {
  def toJsonString: String = {
    upickle.default.write(this)
  }
}

object CodeResult {
  implicit val codeResultRW: default.ReadWriter[CodeResult] = upickle.default.macroRW

  def fromErrorMessage(s: String): CodeResult = {
    CodeResult(isSuccessful = false, exception = s)
  }

  def fromCompilationError(t: Throwable): CodeResult = {
    CodeResult(isSuccessful = false, compilationError = s"Error during parse/compile phase: ${t.getMessage}")
  }

  def fromEvalError(t: Throwable, output: String): CodeResult = {
    CodeResult(isSuccessful = false, output = output, exception = s"Exception during evaluation: ${t.getMessage}")
  }

  def fromSuccess[A](value: A, output: String): CodeResult = {
    CodeResult(isSuccessful = true, output = s"$output$value")
  }
}
