## Run instructions
> sbt clean assembly

See `target/scala-2.13/<jarname>` for the result. Note that tests are deliberately skipped. Clean is used to make sure
we don't include dependencies that are not used anymore.

## Test instructions
> sbt test