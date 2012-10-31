
Hub for collecting messages from different channels and sending them to Digipost in batches.

Channels implemented so far:
- Mail (Two-way using SMTP/POP)


Usage
============================================
Start
-----
* Rename hub.properties.template to hub.properties and enter your Digipost sender's settings.
* Run hub
  cd hub/
  mvn clean install
  mvn exec:java
  
To send a letter in Digipost via the SMTP-channel
-------------------------------------------------
* Define a new SMTP-account in your mail-client. 
  Email-adress: to@localhost.com
  Username: user
  Password: pass
  Incoming POP-server: localhost:25001
  Outgoing SMTP-server: localhost:25000
* Send a mail from the account.
  Subject: [Subject for the Digipost letter]
  To:[digipostadresses]  (Ex. test.testing#1234)
  Attach the pdf-file to send to all recipients defined in the To-field. 
