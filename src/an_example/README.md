# An Example

In order to introduce the problem, an example program has been created. It uses a real world
scenario to demonstrate the current problems. During implementation a few assumptions were made:

* Implemented using Java 8 with no addtional libraries (except log4j)
* Uses common practice encryption mechanisms
* Coded with common Java idioms and verified using checkstyle

The example implementation is an HTTP server that encrypts and decrypts data. You could label it
and encryption service or a software based HSM. Quite a few liberties were taken with this
implementation and it is only intended as a piece of example code and is not suitable for
production use. The basic functionality is as follows:

```
$ mvn exec:java -Dexec.mainClass=Main

$ curl -d "Secret Data" "http://localhost:8080/encrypt"
liWKzWRcBfMF0wSMHXkFpSqBZQaa5f1BUUHVWoorBoc=

$curl -d "liWKzWRcBfMF0wSMHXkFpSqBZQaa5f1BUUHVWoorBoc=" "http://localhost:8080/decrypt"
Secret Data
```

It provides no other real functionality. The implementation offers a few additional responses
which were only used as implementation references.

## Limitations of the Example

The example code does not follow the HTTP protocol specification nor does it implement what
constitues a proper web server. It was built up from just the pieces needed to provide a
complete demonstration of the problem documented.

## Why didn't you use X?

There are plenty of libraries and non-Java JVM languages that could have been used to create
this example. The point of doing this in Java and not using any additonal libararies is to try to
cut down on the depth of the stack and to be able to modify the implementation to test out the
different theories provided throughout the book.
