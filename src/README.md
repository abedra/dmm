# Deterministic Memory Management on the JVM




## Additional Requirements

If this problem is solved, there is still documentation and
recommended practice around how key material gets pushed to a
program. Often it is read from a file or from JKS. The use of strings
should be explicitly noted as unsafe unless a mechanism can be
implemented to release the string in a deterministic fashion.
