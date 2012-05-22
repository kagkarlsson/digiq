
Hub for collecting messages and sending them to Digipost in batches.

* Rename hub.properties.template to hub.properties and enter your Digipost sender's settings.
* Run hub
  cd hub/
  mvn clean install
  mvn exec:java
* Run example-client called ClientMain to generate a Digipost-message on the queue.
