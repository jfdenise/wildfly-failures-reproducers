#!/bin/sh

# Uncomment if the CLI dependency doesn't resolve logging (7.0)
#LOGGING_MODULES=/Users/jfdenise/workspaces/wildfly-core-eap-7.0.x/dist/target/wildfly-core-2.1.13.Final-redhat-SNAPSHOT/modules/system/layers/base/org/jboss/logging/main/jboss-logging-3.3.0.Final-redhat-1.jar:/Users/jfdenise/workspaces/wildfly-core-eap-7.0.x/dist/target/wildfly-core-2.1.13.Final-redhat-SNAPSHOT/modules/system/layers/base/org/jboss/as/logging/main/wildfly-logging-2.1.13.Final-redhat-SNAPSHOT.jar:/Users/jfdenise/workspaces/wildfly-core-eap-7.0.x/dist/target/wildfly-core-2.1.13.Final-redhat-SNAPSHOT/modules/system/layers/base/org/jboss/logmanager/main/jboss-logmanager-2.0.3.Final-redhat-1.jar
mvn exec:exec -Dexec.executable=java -Dexec.args="-Xmx512m -Dorg.xnio.ssl.TRACE_SSL=true -XX:MetaspaceSize=128m -Djava.util.logging.manager=org.jboss.logmanager.LogManager -Dlogging.configuration=file:./logging$1.properties -classpath %classpath:$LOGGING_MODULES com.mycompany.testconnect.Main"
