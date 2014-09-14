# The Problem

Key material will inevitably enter a crypto system when cryptographic
operations are being performed. To ensure the highest level of
protection of the key material, management of the memory holding the
information should be carefully examined. In a managed runtime, this
is a difficult if not intractible problem. Many crypto systems have
been built using C or C++ in order to achieve the deterministic
management of memory as well as for performance. With the evoloution
of managed runtimes the performance gap has closed in ways that
support building the next generation of systems using this
technology. There is no need to spell out the advantages of a managed
runtime when focusing on low level bugs such as those you might find
with programs developed in C.

In order to build next generation crypto systems, there needs to be
guarantees that key material can be safely managed. With this in mind
we find the Java language lacking. Space allocated for key material
using Java's string class presents the problem of immutability. Since
the reserved string cannot be modified there is no way to determine
exactly when the string will be garbage collected and the data written
over in memory. Using a byte array to hold the data solves the
mutability problem, but doesn't solve the determinism issue. Code can
be written to zero a byte array after its contents have been used, but
there is no guarantee that the JVM won't optimize this code away as
unnecessary. By zeroing out a byte array and never using it the
dangling reference is a prime candidate for optimization, potentially
causing what seems like a solution to no longer function as
expected.
