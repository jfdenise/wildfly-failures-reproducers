#!/bin/bash

trap ctrl_c INT

function ctrl_c() {
 exit 1;
}
JBOSS_HOME=/Users/jfdenise/workspaces/wildfly-core-latest/dist/target/wildfly-core-3.0.0.Beta29-SNAPSHOT
JAVA_OPTS="-Djavax.net.ssl.trustStore=/Users/jfdenise/.jboss-cli.truststore -Djavax.net.ssl.trustStorePassword=cli_truststore"
if [ $# -ne 0 ]
  then
    JAVA_OPTS="$JAVA_OPTS -Dlogging.configuration=file:./logging$1.properties"
fi
export JAVA_OPTS;
for number in {1..1000}
do
echo "calling CLI $number"
sh $JBOSS_HOME/bin/jboss-cli.sh --timeout=9000000 -c ":read-attribute(name=server-state)"
if [ $? -ne 0 ]; then
    echo "CLI failed"
    exit 1;
fi
echo "DONE CLI $number"
done
