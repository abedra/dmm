all:
	javac LoadKeyFromStore.java
	javac LoadKeyFromFile.java
	javac SecureErase.java
	javah SecureErase
	gcc -c -I`/usr/libexec/java_home`/include -I`/usr/libexec/java_home`/include/darwin SecureErase.c -o libSecureErase.o
	libtool -dynamic -lSystem -macosx_version_min 10.8 libSecureErase.o -o libSecureErase.dylib

clean:
	rm *.o *.dylib *.class *.h

.PHONY: clean
