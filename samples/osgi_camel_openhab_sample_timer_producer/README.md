Apache Camel with OpenHAB sample: Camel Producer
=================================================

This bundle uses component direct-vm to act as a direct-vm producer
to exchange data with another direct-vm consumer.

Concretely, this scenario is intended to be used with OpenHAB
to exchange data between Camel and OpenHAB.
This producer configuration defines the following route.

```xml
    <route>
      <from uri="timer://openhabTestTrigger?fixedRate=true&amp;period=10000" />
      <process ref="greetingProcessor"/>
      <to uri="direct-vm:openhab-sample-timer-producer"/>
    </route>
```
In order to receive data at OpenHAB from this producer endpoint,
binding type camelprovider can be used in the corresponding
OpenHAB items configuration.

```
String CamelProviderTimerIn "CamelProviderTimerIn[%s]" {camelprovider="<[//openhab-sample-timer-producer:REGEX((.*))]"}
```
Building
--------
From the base directory of this sample, the pom.xml file
is used to build the project.

  mvn clean install
  
Running the demo in OpenHAB OSGi
------------------------
After building the sample, copy the jar file at the target
directory into the hot deployment directory of OpenHAB.

See
../instruction_osgi_camel_openhab_sample_route.txt

