package icesword.agent;

import icesword.agent.data.process.JvmItem;
import icesword.agent.service.ConfigService;
import icesword.agent.service.JpsMonitorService;
import icesword.agent.service.JstatMonitorService;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

public class JstatPlus {

    public static final String         AGENT_VERSION    = "1.0";

    public static AtomicLong           POLL_INTERVEL    = new AtomicLong(60 * 1000);
    public static AtomicBoolean        RUNNING          = new AtomicBoolean(true);
    public static AtomicBoolean        ONLINE           = new AtomicBoolean(false);

    private static long                MONITOR_INTERVAL = 1000;

    private static JstatMonitorService jstatPool        = new JstatMonitorService();

    public static void main(String[] args) throws InterruptedException {
        if (args.length == 0 || (args.length == 2 && args[0].trim().equals("-k"))) { // 单机模式
            while (RUNNING.get()) {
                List<JvmItem> jvmList = JpsMonitorService.findWorkerJVM((args.length == 0 ? null : args[1].trim()), jstatPool);
                jstatPool.addJVMs(jvmList, MONITOR_INTERVAL);
                jstatPool.cleanDoneFuture();
                Thread.sleep(POLL_INTERVEL.get());
            }
        } else if (args.length == 2 && args[0].trim().equals("-r")) { // Diamond模式
            ConfigService cs = ConfigService.builder().configServerAddress(args[1].trim()).build();
            while (RUNNING.get()) {
                cs.updateConfigAndSendEvent(AGENT_VERSION);
                if (cs.getConfig() != null) {
                    if (cs.getConfig().getStatus() == 1) {
                        ONLINE.set(true);
                        POLL_INTERVEL.set(cs.getConfig().getPeriod());
                        List<JvmItem> jvmList = JpsMonitorService.findWorkerJVM(null, jstatPool);
                        jstatPool.addJVMs(jvmList, MONITOR_INTERVAL * 2);
                        jstatPool.cleanDoneFuture();
                    } else if (cs.getConfig().getStatus() == 0) {
                        jstatPool.killAllAttach();
                        jstatPool.cleanDoneFuture();
                    } else if (cs.getConfig().getStatus() == -1) {
                        jstatPool.killAllAttach();
                        jstatPool.cleanDoneFuture();
                        break;
                    }
                }
                Thread.sleep(POLL_INTERVEL.get());
                cs.sendData();
            }
        }
        System.exit(0);
    }
}
