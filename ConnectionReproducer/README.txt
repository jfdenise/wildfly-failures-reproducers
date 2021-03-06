To debug CLI
------------
- Select the right wildfly-cli version and update pom.xml. This will bring xnio and remoting as dependency. Check that these are the ones you expect.
- If you want to add traces and allow to set breakpoint after hang occured in xnio WorkerThread, goto xnio directory, you can select the patch for your release (crossing finger it will apply).
- You can make light adjustment to Main or MainCLI classes (server port, protocols, truststore...).
- mvn clean install this project. If you are running an internal eap version, use the right ../settings/xxx.xml file mvn -s xxx.xml -Dmaven.repo.local=$HOME/.m2/repository
- Rebuild the wildfly (core is generally enough) server.
- Start the server
- call run-with-traces.sh (for remoting only kind of client, Main class) or run-with-traces-cli.sh for CLI (MainCLI class).
- The client is in a loop, making connection then closing.
- logging.properties file can be updated with the loggers and levels. The log file is a rotating one.

Running multiple clients in //
------------------------------
Each client must have its own jboss-cli.log file. 2 logging properties are present in the project.
When calling run-with-traces(-cli).sh the logging file logging.properties is used.
If you want to run a second client do: run-with-traces(-cli).sh 2
To add another client, copy logging.properties to logging3.properties, edit logging3.properties and update the name of the log file (eg: jboss-cli3.log)

