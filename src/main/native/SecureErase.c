#include <string.h>
#include "SecureErase.h"

// From http://www.daemonology.net/blog/2014-09-04-how-to-zero-a-buffer.html
static void * (* const volatile memset_ptr)(void *, int, size_t) = memset;

static void
secure_memzero(void * p, size_t len)
{
  (memset_ptr)(p, 0, len);
}

JNIEXPORT jint JNICALL
Java_SecureErase_zero (JNIEnv *env, jobject obj, jarray arr)
{
  jint len = (*env)->GetArrayLength(env, arr);
  jbyte* buffer = (*env)->GetPrimitiveArrayCritical(env, arr, 0);
  secure_memzero(buffer, len);
  (*env)->ReleasePrimitiveArrayCritical(env, arr, buffer, 0);
  return 0;
}
