To debug CLI
------------
- Select the right wildfly-cli version and update pom.xml. This will bring xnio and remoting as dependency. Check that these are the ones you expect.
- If you want to add traces and allow to set breakpoint after hang occured in xnio WorkerThread, goto xnio directory, you can select the patch for your release (crossing finger it will apply).
- You can make light adjustment to Main or MainCLI classes (server port, protocols, truststore...).
- mvn clean install this project. If you are running an internal eap version, use the right ../settings/xxx.xml file mvn -s xxx.xml -Dmaven.repo.local=$HOME/.m2/repository
- Rebuild the wildfly (core is generally enough) server.
- Start the server
- call run-with-traces.sh (for remoting only kind of client, Main class) or run-with-traces-cli.sh for CLI (MainCLI class.
- The client is in a loop, making connection then closing.
- logging.properties file can be updated with the loggers and levels. The log file is a rotating one.

NOTES
-----
- Hang can be caused by the client or the server socket not interested in read or write operations. You can check by setting a breakpoint in the Xnio DEBUG threads, these thread wake up every 5 seconds. From the thread you can navigate to the selector and look at the SelectableKey and their state, 1 means read, 3 means write.
- By looking at the traces determinate if there are no race condition between conduit resume/suspend read/write

SOME VERSIONS
-------------
EAP 7.0
git clone git://git.app.eng.bos.redhat.com/wildfly/wildfly-core.git
CP6 branch: 2.1.15.Final-redhat-1

