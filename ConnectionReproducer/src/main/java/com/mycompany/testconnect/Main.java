/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.testconnect;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadInfo;
import org.jboss.as.cli.Util;
import org.jboss.as.cli.operation.impl.DefaultOperationRequestBuilder;
import org.jboss.as.controller.client.ModelControllerClientConfiguration;
import org.jboss.logging.Logger;

/**
 *
 * @author jdenise@redhat.com
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    private static volatile boolean running;
    public static void main(String[] args) throws Exception {
        Logger log = Logger.getLogger("org.jboss.as.foo");
        int cpt = 0;
        for (int i = 0; i < 10000; i++) {
            long t = System.currentTimeMillis();
            final int ii = i;
            try {
                Runnable watcher = new Runnable() {
                    @Override
                    public void run() {
                        //System.out.println("Entering watcher");
                        log.debug("Entering watcher");
                        while (running) {
                            try {
                                Thread.sleep(100);
                            } catch (InterruptedException ex) {
                                System.err.println("Interrupted");
                                System.exit(1);
                            }
                            long time = System.currentTimeMillis() - t;
                            if (time > 10000) {
                                System.out.println("TIMEOUT FOR " + ii + " time is " + time);
                                log.debug("TIMEOUT FOR " + ii + " time is " + time);
                                ThreadInfo info[] = ManagementFactory.getThreadMXBean().dumpAllThreads(true, false);
                                for (ThreadInfo i : info) {
                                    System.out.println(i.toString());
                                }
                                //System.exit(1);
                                running = false;
                            }
                        }
                        //System.out.println("Leaving watcher");
                        log.debug("Leaving watcher");
                    }
                };
                Thread thr = new Thread(watcher);
                //log.info("@@@@@Connect" + i);
                //Thread.sleep(100);
                System.setProperty("javax.net.ssl.trustStore", "/Users/jfdenise/.jboss-cli.truststore");
                System.setProperty("javax.net.ssl.trustStorePassword", "cli_truststore");
                ModelControllerClientConfiguration config = new ModelControllerClientConfiguration.Builder()
                        .setProtocol("https-remoting")
                        .setHostName("127.0.0.1")
                        .setPort(9993)
                        .setConnectionTimeout(90000)
                        .build();
                org.jboss.as.controller.client.ModelControllerClient c = org.jboss.as.controller.client.ModelControllerClient.Factory.create(config);
                DefaultOperationRequestBuilder builder = new DefaultOperationRequestBuilder();
                builder.setOperationName(Util.READ_ATTRIBUTE);
                builder.addProperty(Util.NAME, Util.NAME);
                running = true;
                thr.start();
                //System.out.println("CONNECTING " + ii);
                log.debug("CONNECTING " + ii);
                c.execute(builder.buildRequest());
                //System.out.println("CONNECTED" + ii);
                log.debug("CONNECTED" + ii);
                running = false;
                thr.join();
                //log.info("@@@@@Called");
                c.close();
                cpt += 1;
                //log.info("@@@@@DisConnected");
            } catch (Exception ex) {
                //log.log(Level.INFO, "@@@@Error in test!", ex);
                // j += 1;
                //if (j == 4) {
                throw new Exception("Got Wxception " + cpt, ex);
                // }
            }
        }
    }

}
