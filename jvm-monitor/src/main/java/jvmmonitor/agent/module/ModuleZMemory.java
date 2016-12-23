package jvmmonitor.agent.module;

import jvmmonitor.agent.monitor.MonitorItem;

/**
 * Created by peiliping on 16-12-21.
 */
public class ModuleZMemory extends AbstractModule {

    private String memoryGeneration0Space0Name = "eden";
    private String memoryGeneration0Space1Name = "s0";
    private String memoryGeneration0Space2Name = "s1";
    private String memoryGeneration1Space0Name = "old";

    private String memorySurvivorName = "survivor";
    private String memoryPermName     = "perm";

    private String memoryCompressedClassSpaceName = "ccs";
    private String memoryMetaSpaceName            = "meta";

    public ModuleZMemory(String moduleName, MonitorItem item) {
        super(moduleName, item);
        super.noChangeMetricNames = new String[] {"eden/used"};

        METRICNAME.put(memoryGeneration0Space0Name + "/used", "sun.gc.generation.0.space.0.used");
        METRICNAME.put(memoryGeneration0Space0Name + "/capacity", "sun.gc.generation.0.space.0.capacity");
        METRICNAME.put(memoryGeneration0Space1Name + "/used", "sun.gc.generation.0.space.1.used");
        METRICNAME.put(memoryGeneration0Space1Name + "/capacity", "sun.gc.generation.0.space.1.capacity");
        METRICNAME.put(memoryGeneration0Space2Name + "/used", "sun.gc.generation.0.space.2.used");
        METRICNAME.put(memoryGeneration0Space2Name + "/capacity", "sun.gc.generation.0.space.2.capacity");
        METRICNAME.put(memoryGeneration1Space0Name + "/used", "sun.gc.generation.1.space.0.used");
        METRICNAME.put(memoryGeneration1Space0Name + "/capacity", "sun.gc.generation.1.space.0.capacity");

        METRICNAME.put(memorySurvivorName + "/desiredsize", "sun.gc.policy.desiredSurvivorSize");

        if ("1.8".compareTo(super.item.getVmVersion()) >= 0) {
            METRICNAME.put(memoryCompressedClassSpaceName + "/used", "sun.gc.compressedclassspace.used");
            METRICNAME.put(memoryCompressedClassSpaceName + "/capacity", "sun.gc.compressedclassspace.capacity");
            METRICNAME.put(memoryMetaSpaceName + "/used", "sun.gc.metaspace.used");
            METRICNAME.put(memoryMetaSpaceName + "/capacity", "sun.gc.metaspace.capacity");
        } else {
            METRICNAME.put(memoryPermName + "/used", "sun.gc.generation.2.space.0.used");
            METRICNAME.put(memoryPermName + "/capacity", "sun.gc.generation.2.space.0.capacity");
        }

    }

    public void output() {
        super._output(memoryGeneration0Space0Name + "/used", getOriginVal(memoryGeneration0Space0Name + "/used"));
        super._output(memoryGeneration0Space0Name + "/capacity", getOriginVal(memoryGeneration0Space0Name + "/capacity"));
        super._output(memoryGeneration0Space1Name + "/used", getOriginVal(memoryGeneration0Space0Name + "/used"));
        super._output(memoryGeneration0Space1Name + "/capacity", getOriginVal(memoryGeneration0Space0Name + "/capacity"));
        super._output(memoryGeneration0Space2Name + "/used", getOriginVal(memoryGeneration0Space0Name + "/used"));
        super._output(memoryGeneration0Space2Name + "/capacity", getOriginVal(memoryGeneration0Space0Name + "/capacity"));
        super._output(memoryGeneration1Space0Name + "/used", getOriginVal(memoryGeneration0Space0Name + "/used"));
        super._output(memoryGeneration1Space0Name + "/capacity", getOriginVal(memoryGeneration0Space0Name + "/capacity"));

        super._output(memorySurvivorName + "/desiredsize", getOriginVal(memorySurvivorName + "/desiredsize"));

        if ("1.8".compareTo(super.item.getVmVersion()) >= 0) {
            super._output(memoryCompressedClassSpaceName + "/used", getOriginVal(memoryCompressedClassSpaceName + "/used"));
            super._output(memoryCompressedClassSpaceName + "/capacity", getOriginVal(memoryCompressedClassSpaceName + "/capacity"));
            super._output(memoryMetaSpaceName + "/used", getOriginVal(memoryMetaSpaceName + "/used"));
            super._output(memoryMetaSpaceName + "/capacity", getOriginVal(memoryMetaSpaceName + "/capacity"));
        } else {
            super._output(memoryPermName + "/used", getOriginVal(memoryPermName + "/used"));
            super._output(memoryPermName + "/capacity", getOriginVal(memoryPermName + "/capacity"));
        }
    }
}
