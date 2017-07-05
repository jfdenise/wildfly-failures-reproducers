# wildfly-failures-reproducers

Directories
-----------
- ConnectionReproducer: a reproducer and informations on how to chase CLI (and generic remoting) connection issues.
- settings: some maven settings to build internal EAP releases.
- xnio: patchs to apply to make xnio easier to debug (debug threads started, traces added).

Notes
-----
- When chasing connection hangs. They can be caused by the client or the server socket not interested in read or write operations. 
You can check by setting a breakpoint in the Xnio DEBUG threads, this thread wakes up every 5 seconds. 
From the thread you can navigate to the selector and look at the SelectableKey and their state, 1 means read, 3 means write.
- By looking at the traces determinate if there are no race condition between conduit resume/suspend read/write

Build some internal EAP versions
--------------------------------
EAP 7.0
- git clone git://git.app.eng.bos.redhat.com/wildfly/wildfly-core.git
- CP6 branch: 2.1.15.Final-redhat-1

