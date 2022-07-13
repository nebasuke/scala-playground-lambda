package com.couchbase.playground.scala

// We currently don't use the value, but it's probably good to store it in case we do want the result of running a program.
case class CodeResult[A](output: String, value: A)
