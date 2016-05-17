package icesword.agent.service;

import icesword.agent.data.process.JvmItem;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.sun.tools.attach.VirtualMachine;

import sun.jvmstat.monitor.MonitorException;
import sun.jvmstat.monitor.MonitoredHost;
import sun.jvmstat.monitor.MonitoredVm;
import sun.jvmstat.monitor.MonitoredVmUtil;
import sun.jvmstat.monitor.VmIdentifier;
import sun.tools.jps.Arguments;

public class JpsMonitorService {

    public static Arguments JPS_ARGUMENTS = new Arguments(new String[] {"-lmv"});

    public static List<JvmItem> findWorkerJVM(String filterWord, JstatMonitorService jsm) {
        List<JvmItem> result = new ArrayList<JvmItem>();
        try {
            MonitoredHost monitoredHost = MonitoredHost.getMonitoredHost(JPS_ARGUMENTS.hostId());
            Set<?> jvms = monitoredHost.activeVms();
            for (Iterator<?> jvm = jvms.iterator(); jvm.hasNext();) {
                JvmItem jvmItem = new JvmItem(((Integer) jvm.next()).intValue());
                buildJvmItem(monitoredHost, jvmItem);
                if (filterJvm(jvmItem, filterWord) && !jsm.isExistPid(jvmItem.pid)) {
                    getJVMVersion(jvmItem);
                    if (jvmItem.status) {
                        result.add(jvmItem);
                    }
                }
            }
        } catch (MonitorException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static boolean filterJvm(JvmItem jvmItem, String filterWord) {
        if (jvmItem.mainClass.startsWith("sun.tools"))
            return false;
        if (jvmItem.mainClass.contains("icesword"))
            return false;
        if (jvmItem.mainClass.contains("jstat_plus.jar"))
            return false;
        if (filterWord != null) {
            return jvmItem.mainClass.contains(filterWord);
        }
        return true;
    }

    public static void buildJvmItem(MonitoredHost monitoredHost, JvmItem jvmItem) {
        MonitoredVm vm = null;
        String vmidString = "//" + jvmItem.pid + "?mode=r";
        String errorString = null;
        try {
            errorString = " -- process information unavailable";
            VmIdentifier id = new VmIdentifier(vmidString);
            vm = monitoredHost.getMonitoredVm(id, 0);
            errorString = " -- main class information unavailable";
            jvmItem.mainClass = "" + MonitoredVmUtil.mainClass(vm, JpsMonitorService.JPS_ARGUMENTS.showLongPaths());
            if (JpsMonitorService.JPS_ARGUMENTS.showMainArgs()) {
                errorString = " -- main args information unavailable";
                String mainArgs = MonitoredVmUtil.mainArgs(vm);
                if (mainArgs != null && mainArgs.length() > 0) {
                    jvmItem.mainArgs = mainArgs;
                }
            }
            if (JpsMonitorService.JPS_ARGUMENTS.showVmArgs()) {
                errorString = " -- jvm args information unavailable";
                String jvmArgs = MonitoredVmUtil.jvmArgs(vm);
                if (jvmArgs != null && jvmArgs.length() > 0) {
                    jvmItem.vmArgs = jvmArgs;
                }
            }
            if (JpsMonitorService.JPS_ARGUMENTS.showVmFlags()) {
                errorString = " -- jvm flags information unavailable";
                String jvmFlags = MonitoredVmUtil.jvmFlags(vm);
                if (jvmFlags != null && jvmFlags.length() > 0) {
                    jvmItem.vmFlags = jvmFlags;
                }
            }
            errorString = " -- detach failed";
            monitoredHost.detach(vm);
            errorString = null;
            jvmItem.simpleDesc();
        } catch (Exception e) {
            e.printStackTrace();
            jvmItem.errorString = errorString;
            jvmItem.status = false;
        }
    }

    public static void getJVMVersion(JvmItem jvmItem) {
        try {
            VirtualMachine vm = VirtualMachine.attach("" + jvmItem.pid);
            String r = (vm.getSystemProperties().getProperty("java.vm.specification.version"));
            vm.detach();
            jvmItem.vmVersion = r.trim();
        } catch (Exception e) {
            e.printStackTrace();
            jvmItem.status = false;
            jvmItem.errorString = "vm version information unavailable";
        }
    }
}
