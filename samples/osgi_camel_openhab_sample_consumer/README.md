This bundle uses component direct-vm to act as a direct-vm consumer
to exchange data with another direct-vm producer.

Concretely, this scenario is intended to be used with OpenHAB
to exchange data between Camel and OpenHAB.
This consumer configuration defines the following route.

```xml
    <route>
      <from uri="direct-vm:openhab-sample-consumer"/>
      <process ref="greetingProcessor"/>
      <to uri="file:///tmp/camel-file/openhab/out"/>
    </route>
```
In order to post data from OpenHAB to this consumer endpoint,
binding type cameldispatcher can be used in the corresponding
OpenHAB items configuration.

```
Switch CamelDispatcherOut "CamelDispatcherOut" {cameldispatcher=">[ON://openhab-sample-consumer:REGEX((.*))] >[OFF://openhab-sample-consumer:REGEX((.*))]"}
String CamelDispatcherIn "CamelDispatcherIn[%s]" {cameldispatcher="<[//openhab-sample-consumer:REGEX((.*))]"}
```

