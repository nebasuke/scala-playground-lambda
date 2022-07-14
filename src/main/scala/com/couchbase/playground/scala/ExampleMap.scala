package com.couchbase.playground.scala

object ExampleMap {
  val exampleMap = Map(
    "KV_GET" -> CodeExample(
      snippet = "",
      fullCode = """import com.couchbase.client.core.error.{CouchbaseException, DocumentNotFoundException}
                   |import com.couchbase.client.scala.Cluster
                   |import scala.util.{Failure, Success, Try}
                   |import com.couchbase.client.scala.json.{JsonObject, JsonObjectSafe}
                   |
                   |object Program extends App {
                   |  val cluster = Cluster.connect("{credentials-connectionString}", "{credentials-username}", "{credentials-password}").get
                   |  var bucket = cluster.bucket("{credentials-bucket}");
                   |  val collection = bucket.defaultCollection
                   |
                   |  collection.get("airline_10") match {
                   |    case Success(result) =>
                   |      result.contentAs[JsonObjectSafe] match {
                   |          case Success(json) => println(json)
                   |          case Failure(err) => println("Error decoding result: " + err)
                   |      }
                   |    case Failure(err: DocumentNotFoundException) => println("Document not found")
                   |    case Failure(err: CouchbaseException) => println("Couchbase error: " + err)
                   |    case Failure(err) => println("Error getting document: " + err)
                   |  }
                   |}""".stripMargin
    ),


  )

  def getExample(key: String): Either[String, CodeExample] = {
    ExampleMap.exampleMap.get(key).toRight(s"Example does not exists. Key: $key")
  }
}
