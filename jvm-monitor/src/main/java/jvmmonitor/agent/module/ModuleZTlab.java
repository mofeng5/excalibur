package jvmmonitor.agent.module;

import jvmmonitor.agent.monitor.MonitorItem;

/**
 * Created by peiliping on 16-12-21.
 */
public class ModuleZTlab extends AbstractModule {

    public ModuleZTlab(String moduleName, MonitorItem item) {
        super(moduleName, item);
        super.noChangeMetricNames = new String[] {"minorgc", "majorgc"};
        super.addMetric("minorgc", "sun.gc.collector.0.invocations");
        super.addMetric("majorgc", "sun.gc.collector.1.invocations");

        super.addMetric("alloc", "sun.gc.tlab.alloc");
        super.addMetric("allocthreads", "sun.gc.tlab.allocThreads");
        super.addMetric("fills", "sun.gc.tlab.fills");
        super.addMetric("gcwaste", "sun.gc.tlab.gcWaste");
    }

    public void transform(long timestamp) {
        super.store("alloc", timestamp, getOriginVal("alloc"));
        super.store("allocthreads", timestamp, getOriginVal("allocthreads"));
        super.store("fills", timestamp, getOriginVal("fills"));
        super.store("gcwaste", timestamp, getOriginVal("gcwaste"));
        super.commit();
    }
}
