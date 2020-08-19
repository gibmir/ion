# ion
Project represents json-rpc 2.0 protocol implementation.

## Features:
  * Functional remote procedure call API
  * Configurable requests
  * Netty tcp client
  * Netty tcp server
  * API for framework configuration
  * YAML framework configuration
  
## Examples:
### Service API

To use ion you need to describe your service as bunch of functions. 
For example describe part of your functionality as function (get string and return string):
```java
public interface ServiceProcedure extends JsonRemoteProcedure1<String, String> {
 
}
```
To specify named parameters you can use special annotation (but framework gave you defaults):
```java
public interface TestStringProcedure extends JsonRemoteProcedure1<String, String> {
  @Override
  String call(@Named(name = "namedArgument") String arg);
}
```
(`schema API. service functions code generation`)

---
### Client
So far, so good. You can get request factory provider through SPI mechanism(or create concrete implementation manually):
```
RequestFactory requestFactory = RequestFactoryProvider.load().provide();
```
After that we can create request:
```
Request1<String, String> request = requestFactory.singleArg(TestStringProcedure.class);
```
Make a positional call through request:
```
request.positionalCall("id-1", "argument")
```
Make a notification call through request:
```
request.notificationCall("notification message")
```
Make a named call through request:
```
request.namedCall("id-1", "argument")
```
---
### Server
You can get a json-rpc server factory through SPI(or create concrete implementation manually):
```
JsonRpcServerFactory jsonRpcServerFactory = JsonRpcServerFactoryProvider.load().provide();
```
After that you need to create a server:
```
JsonRpcServer jsonRpcServer = jsonRpcServerFactory.create();
```
Finally, you can register your procedure processor:
```
jsonRpcServer.registerProcedureProcessor(TestStringProcedure.class, String::toUpperCase);
```
**You can find examples in submodules of module `test`**