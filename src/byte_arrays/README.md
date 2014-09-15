# Dealing With Byte Arrays

This example attempts to demonstrate a primary scenario for which this
problem is present. The sample was built using Java 8.

#### Preventing the VM from moving the data in memory

The following is a native implementation of the zero method called
from the Java code. It accepts a byte array, determines the length,
and sets every value in the array to 0. `GetPrimitiveArrayCritical`
and `ReleasePrimitiveArrayCritical` are used to prevent the VM from
acting on the data.

***SecureErase.c***
```C
  #include <string.h>
  #include "SecureErase.h"

  JNIEXPORT jint JNICALL Java_SecureErase_zero (JNIEnv *env, jobject obj, jarray arr) {
    jint len = (*env)->GetArrayLength(env, arr);
    jbyte* buffer = (*env)->GetPrimitiveArrayCritical(env, arr, 0);
    memset(buffer, 0, len);
    (*env)->ReleasePrimitiveArrayCritical(env, arr, buffer, 0);
    return 0;
  }
```

#### A Functionality Test in Java

***SecureErase.java***
```java
  import java.util.Arrays;

  public class SecureErase {
      public static native int zero(byte[] arr);

      public static void main(String[] args) {
          System.loadLibrary("SecureErase");
          byte[] test = "ThisIsASecretKey".getBytes();
          System.out.println("Before: " + Arrays.toString(test));
          SecureErase.zero(test);
          System.out.println("After: " + Arrays.toString(test));
      }
  }
```

#### Compiling and Running the Example

This code was built on Mac OS X 10.9. The following =Makefile=
provides the mechanics for building and running the example.

***Makefile***
```make
all:
	javac SecureErase.java
	javah SecureErase
	gcc -c -I`/usr/libexec/java_home`/include -I`/usr/libexec/java_home`/include/darwin SecureErase.c -o libSecureErase.o
	libtool -dynamic -lSystem -macosx_version_min 10.8 libSecureErase.o -o libSecureErase.dylib

clean:
	rm *.o *.dylib *.class *.h

.PHONY: clean
```

```
$ make
javac SecureErase.java
javah SecureErase
gcc -c -I`/usr/libexec/java_home`/include -I`/usr/libexec/java_home`/include/darwin SecureErase.c -o libSecureErase.o
libtool -dynamic -lSystem -macosx_version_min 10.8 libSecureErase.o -o libSecureErase.dylib

$ java SecureErase
Before: [84, 104, 105, 115, 73, 115, 65, 83, 101, 99, 114, 101, 116, 75, 101, 121]
After: [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0]
```
