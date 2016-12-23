package jvmmonitor.agent.module;

import jvmmonitor.agent.Util;
import jvmmonitor.agent.monitor.MonitorItem;

/**
 * Created by peiliping on 16-12-21.
 */
public class ModuleZGC extends AbstractModule {

    private String garbageCollector0Name;
    private String garbageCollector1Name;

    public ModuleZGC(String moduleName, MonitorItem item) {
        super(moduleName, item);
        this.garbageCollector0Name = Util.getValueFromMonitoredVm(item.getMonitoredVm(), "sun.gc.collector.0.name").toLowerCase();
        this.garbageCollector1Name = Util.getValueFromMonitoredVm(item.getMonitoredVm(), "sun.gc.collector.1.name").toLowerCase();
        super.noChangeMetricNames = new String[] {garbageCollector0Name + "/count", garbageCollector1Name + "/count"};
        METRICNAME.put(garbageCollector0Name + "/count", "sun.gc.collector.0.invocations");
        METRICNAME.put(garbageCollector0Name + "/time", "sun.gc.collector.0.time");
        METRICNAME.put(garbageCollector1Name + "/count", "sun.gc.collector.1.invocations");
        METRICNAME.put(garbageCollector1Name + "/time", "sun.gc.collector.1.time");
        METRICNAME.put(garbageCollector1Name + "/time", "sun.gc.collector.1.time");
    }

    public void output() {
        super._output(garbageCollector0Name + "/count", getDeltaVal(garbageCollector0Name + "/count"));
        super._output(garbageCollector0Name + "/time", handleTimePrecision(getDeltaVal(garbageCollector0Name + "/time")));
        super._output(garbageCollector1Name + "/count", getDeltaVal(garbageCollector1Name + "/count"));
        super._output(garbageCollector1Name + "/time", handleTimePrecision(getDeltaVal(garbageCollector1Name + "/time")));
    }
}
