package com.mycompany.testconnect;

import java.io.ByteArrayOutputStream;
import java.lang.management.ManagementFactory;
import java.lang.management.ThreadInfo;
import org.jboss.as.cli.CommandContext;
import org.jboss.as.cli.CommandContextFactory;
import org.jboss.as.cli.impl.CommandContextConfiguration;

/**
 *
 * @author jdenise@redhat.com
 */
public class MainCLI {

    /**
     * @param args the command line arguments
     */
    private static volatile boolean running;

    public static void main(String[] args) throws Exception {
        int cpt = 0;
        System.out.println("STARTING");
        System.setProperty("javax.net.ssl.trustStore", "/Users/jfdenise/.jboss-cli.truststore");
        System.setProperty("javax.net.ssl.trustStorePassword", "cli_truststore");
        for (int i = 0; i < 10000; i++) {
            long t = System.currentTimeMillis();
            final int ii = i;
            try {
                Runnable watcher = new Runnable() {
                    @Override
                    public void run() {
                        //  System.out.println("Entering watcher");
                        while (running) {
                            try {
                                Thread.sleep(100);
                            } catch (InterruptedException ex) {
                                System.err.println("Interrupted");
                                System.exit(1);
                            }
                            long time = System.currentTimeMillis() - t;
                            if (time > 30000) {
                                System.out.println("TIMEOUT FOR " + ii + " time is " + time);
                                ThreadInfo info[] = ManagementFactory.getThreadMXBean().dumpAllThreads(true, false);
                                for (ThreadInfo i : info) {
                                    System.out.println(i.toString());
                                }
                                //System.exit(1);
                                running = false;
                            }
                        }
                        //      System.out.println("Leaving watcher");
                    }
                };
                Thread thr = new Thread(watcher);
                ByteArrayOutputStream bo = new ByteArrayOutputStream();
                CommandContextConfiguration c = new CommandContextConfiguration.Builder().setConsoleOutput(bo).setConnectionTimeout(9000000).setController("http-remoting://localhost:9990").build();
                CommandContext ctx = CommandContextFactory.getInstance().newCommandContext(c);
                running = true;
                thr.start();
                System.out.println("CONNECTING " + ii);
                ctx.connectController();
                System.out.println("CONNECTED to" + ctx.getControllerPort());
                running = false;
                thr.join();
                ctx.terminateSession();
                cpt += 1;
            } catch (Exception ex) {
                throw new Exception("Got Wxception " + cpt, ex);
            }
        }
        System.out.println("DONE");
    }

}
