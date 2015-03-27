package meepo;

import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicLong;

import lombok.Getter;
import lombok.Setter;

import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.tuple.Pair;

public class Config {

    @Setter
    @Getter
    private String               sourceTableName;
    @Setter
    @Getter
    private String               targetTableName;
    @Setter
    @Getter
    private String               primaryKeyName;
    @Setter
    @Getter
    private int                  readerStepSize;
    @Setter
    @Getter
    private int                  writerStepSize;
    @Setter
    @Getter
    private String               sourceColumsNames;
    @Setter
    @Getter
    private String               targetColumsNames;
    @Setter
    @Getter
    private Map<String, Integer> sourceColumsType;
    @Setter
    @Getter
    private List<String>         sourceColumsArray;
    @Setter
    @Getter
    private Map<String, Integer> targetColumsType;
    @Setter
    @Getter
    private List<String>         targetColumsArray;
    @Setter
    @Getter
    private AtomicLong           start;            // start为实际最小值-1
    @Setter
    @Getter
    private AtomicLong           end;              // end为实际最大值 如果val = -1则是sync模式
    @Setter
    @Getter
    private boolean              syncMode;
    @Setter
    @Getter
    private long                 syncDelay;
    @Setter
    @Getter
    private boolean              syncSuicide;
    @Setter
    @Getter
    private int                  bufferSize;
    @Setter
    @Getter
    private int                  readersNum;
    @Setter
    @Getter
    private int                  writersNum;
    @Setter
    @Getter
    private boolean              insertOrLoadData;
    @Setter
    @Getter
    private String               pluginName;

    public Config(Properties ps) {
        // ==================Required Config Item===================
        this.sourceTableName = ps.getProperty("sourcetablename");
        this.targetTableName = ps.getProperty("targettablename");
        this.sourceColumsNames = ps.getProperty("sourcecolumsnames");
        this.targetColumsNames = ps.getProperty("targetcolumsnames");
        Validate.notNull(this.sourceTableName);
        Validate.notNull(this.targetTableName);
        Validate.notNull(this.sourceColumsNames);
        Validate.notNull(this.targetColumsNames);
        // =========================================================
        this.primaryKeyName = ps.getProperty("primarykeyname", "id");
        this.bufferSize = Integer.valueOf(ps.getProperty("buffersize", "1024"));
        this.readerStepSize = Integer.valueOf(ps.getProperty("readerstepsize", "100"));
        this.writerStepSize = Integer.valueOf(ps.getProperty("writerstepsize", "100"));
        this.start = ps.getProperty("start") == null ? null : new AtomicLong(Long.valueOf(ps.getProperty("start")));
        this.end = ps.getProperty("end") == null ? null : new AtomicLong(Long.valueOf(ps.getProperty("end")));
        this.readersNum = Integer.valueOf(ps.getProperty("readersnum", "1"));
        this.writersNum = Integer.valueOf(ps.getProperty("writersnum", "1"));
        this.syncMode = Boolean.valueOf(ps.getProperty("syncmode", "false"));
        if (this.syncMode) {
            this.readersNum = 1;
            this.end = new AtomicLong(-1);
        }
        this.syncDelay = Long.valueOf(ps.getProperty("syncdelay", "10"));
        this.syncSuicide = Boolean.valueOf(ps.getProperty("syncsuicide", "false"));
        this.insertOrLoadData = Boolean.valueOf(ps.getProperty("insertorloaddata", "true"));
        this.pluginName = String.valueOf(ps.getProperty("pluginName", ""));
    }

    public void initStartEnd(Pair<Long, Long> ps) {
        if (syncMode) {
            if (this.start == null)
                this.start = new AtomicLong(ps.getRight());
        } else {
            if (this.start == null)
                this.start = new AtomicLong(ps.getLeft());
            if (this.end == null)
                this.end = new AtomicLong(ps.getRight());
        }
    }

    public boolean needAutoInitStartEnd() {
        return start == null || end == null;
    }

}
