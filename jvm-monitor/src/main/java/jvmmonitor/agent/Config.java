package jvmmonitor.agent;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import jvmmonitor.agent.module.*;
import lombok.Getter;

import java.util.Map;
import java.util.Set;

/**
 * Created by peiliping on 16-12-19.
 */


public class Config {

    @Getter private String remoteIp = "";

    // metric monitor interval
    @Getter private int interval;
    @Getter private int multiple4SendData;

    // filter info
    private Set<Integer> targetPids;
    private Set<String>  excludeKeyWords;
    private static final Set<String> EXCLUDEKEYWORDS_CONS = Sets.newHashSet();

    static {
        EXCLUDEKEYWORDS_CONS.add("sun.tools.*");
    }

    @Getter private Set<String> modules;
    public static final Map<String, Class<? extends AbstractModule>> MODULES_CONS = Maps.newHashMap();

    @Getter private boolean debug;

    static {
        MODULES_CONS.put("agetable", ModuleZAgetable.class);
        MODULES_CONS.put("class", ModuleZClass.class);
        MODULES_CONS.put("compiler", ModuleZCompiler.class);
        MODULES_CONS.put("gc", ModuleZGC.class);
        MODULES_CONS.put("gcextend", ModuleZGCExtend.class);
        MODULES_CONS.put("lock", ModuleZLock.class);
        MODULES_CONS.put("memory", ModuleZMemory.class);
        MODULES_CONS.put("thread", ModuleZThread.class);
        MODULES_CONS.put("threshold", ModuleZThreshold.class);
        MODULES_CONS.put("tlab", ModuleZTlab.class);
        MODULES_CONS.put("safepoint", ModuleZSafepoint.class);
    }

    public Config(int interval, Set<Integer> targetPids, Set<String> excludeKeyWords, Set<String> modules, int multiple, String remoteIp, boolean debug) {
        this.remoteIp = remoteIp;
        this.interval = interval;
        this.targetPids = targetPids;
        this.excludeKeyWords = excludeKeyWords;
        this.excludeKeyWords.addAll(EXCLUDEKEYWORDS_CONS);
        this.modules = modules;
        this.multiple4SendData = multiple;
        if (modules.size() == 0) {
            modules.addAll(MODULES_CONS.keySet());
        }
        this.debug = debug;
    }

    public boolean filterPid(Integer id) {
        if (targetPids.isEmpty()) {
            return true;
        } else {
            return targetPids.contains(id);
        }
    }

    public boolean filterKeyWords(String mainClass) {
        for (String kw : excludeKeyWords) {
            if (mainClass.matches(kw)) {
                return true;
            }
        }
        return false;
    }

    public String getMetricsUrl() {
        return "http://" + this.remoteIp + "/jvm/metrics";
    }

    public String getFlagsUrl() {
        return "http://" + this.remoteIp + "/jvm/flags";
    }

}
