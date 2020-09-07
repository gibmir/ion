# ion
Project represents json-rpc 2.0 protocol implementation.

## Features:
  * Functional remote procedure call API
  * Configurable requests
  * API for framework configuration
  * YAML framework configuration
  * Netty tcp client and server
  * TLS support for the netty client and server
  
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
```keytool -export -keystore mysslstore.jks -alias cert -file maanadev.org.cert
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
jsonRpcServer.registerProcedureProcessor(TestStringProcedure.class,/*TestStringProcedure implementation*/ String::toUpperCase);
```

## SSL

For testing, you can use script: 
```shell script
#Generate new key and create a self signed certificate:
openssl req \
-x509 \
-nodes \
-days 365 \
-newkey rsa:4096 \
-keyout selfsigned.key.pem \
-out selfsigned-x509.crt \

#Convert PEM key to PKCS8 format:
openssl pkcs8 \
-v1 PBE-MD5-DES \
-topk8 \
-inform PEM \
-outform PEM \
-in selfsigned.key.pem \
-out selfsigned-pkcs8.pem
```
It will provide self signed certificate and private key. 
Setup ion server configuration as follows:
```yaml
ion:
  netty:
    server:
      ssl:
        provider: 'JDK'
        certificate:
          path: 'Path to your *.crt'
        key:
          path: 'Path to your *.pem'
          password: 'Your *.pem password'
```
Setup ion client configuration as follows:
```yaml
ion:
  netty:
    client:
      ssl:
        provider: 'JDK'
        trust:
          path: 'Path to your *.crt'
        key:
          path: 'Path to your *.pem'
          password: 'Your *.pem password'
        auth: 'netty auth mode'
```
**You can find examples in submodules of module `test`**