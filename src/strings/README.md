# Strings

## Implementing Secure Strings

Even if we successfully solve the Byte Array issue, we still have to
deal with strings. When using other Java software, especially servlet
containers, the use of strings are inevitable. Microsoft responded to
this with the [SecureString](http://msdn.microsoft.com/en-us/library/system.security.securestring.aspx) class that is designed to solve this particular issue.

Currently there is no way (via the Java language or via JNI) to do this.
