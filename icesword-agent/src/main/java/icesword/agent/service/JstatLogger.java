package icesword.agent.service;

import icesword.agent.JstatPlus;
import icesword.agent.data.process.JstatItem;
import icesword.agent.data.process.JvmItem;
import sun.jvmstat.monitor.MonitorException;
import sun.tools.jstat.OutputFormatter;

public class JstatLogger {

    private volatile static boolean printedHeader = false;
    private volatile boolean        active        = true;
    private JvmItem                 item;

    public JstatLogger(JvmItem item) {
        this.item = item;
    }

    public void stopLogging() {
        active = false;
    }

    public void logSamples(OutputFormatter formatter, int headerRate, int sampleInterval) throws MonitorException {
        if (JstatPlus.ONLINE.get()) {
            while (active) {
                try {
                    String row = formatter.getRow();
                    DataService.addData(item, new JstatItem(row, item));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    Thread.sleep(sampleInterval);
                } catch (Exception e) {
                };
            }
        } else {
            if (!printedHeader) {
                System.out.println("PID" + "\t" + formatter.getHeader());
                printedHeader = true;;
            }
            while (active) {
                try {
                    String row = formatter.getRow();
                    System.out.println(item.pid + "\t" + row);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    Thread.sleep(sampleInterval);
                } catch (Exception e) {
                };
            }
        }

    }
}
