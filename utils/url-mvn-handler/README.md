Mvn URL protocol handler
=================================================

This bundle implements the protocol handler for 
protocol mvn. The mvn URL of form

  mvn:groupid/artifactId/versionId

will resolve to the artifact jar file located
in your local maven repository or in the central
remote repository.


Building
--------
From the base directory of this application, the pom.xml file
is used to build and run the standalone unit test.

  mvn clean install
  







