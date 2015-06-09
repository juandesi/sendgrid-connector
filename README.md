# SendGrid Anypoint Connector

This connector allows you to send email without having to maintain email servers. It manages all of the technical details using the sendgrid API, from scaling the infrastructure to ISP outreach and reputation monitoring to whitelist services and real time analytics.


## Mule supported versions

* Mule 3.5.x
* Mule 3.6.x
* Mule 3.7.x

## Installation 
* Clone this repository
* Compile with Maven: mvn clean install -DskipTests
* Go to the target generated folder
* Find the UpdateSite.zip file
* Go to Studio Help -> Install New Software -> Search for the UpdateSite.zip file -> Install & Restart Studio
* Now you can use the Connector in your muleapps

## RunningTests
You need to specify your email, username and password in the .properties file.
Once you done that, just MvnCleanInstall

## Reporting Issues
We use GitHub:Issues for tracking issues with this connector. You can report new issues in this same repository