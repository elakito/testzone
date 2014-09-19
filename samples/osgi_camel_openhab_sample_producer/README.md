This bundle uses component direct-vm to act as a direct-vm producer
to exchange data with another direct-vm consumer.

Concretely, this scenario is intended to be used with OpenHAB
to exchange data between Camel and OpenHAB.
This producer configuration defines the following route.

```xml
    <route>
      <from uri="file:///tmp/camel-file/openhab/in"/>
      <process ref="greetingProcessor"/>
      <to uri="direct-vm:openhab-sample-producer"/>
    </route>
```
In order to receive data at OpenHAB from this producer endpoint,
binding type camelprovider can be used in the corresponding
OpenHAB items configuration.

```
String CamelProviderIn "CamelProviderIn[%s]" {camelprovider="<[//openhab-sample-producer:REGEX((.*))]"}
```
