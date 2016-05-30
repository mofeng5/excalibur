package icesword.agent.service;

import icesword.agent.Startup;
import icesword.agent.data.process.Config;
import icesword.agent.data.process.Event;
import icesword.agent.data.result.ResultData;
import icesword.agent.util.NetTools;
import icesword.agent.util.NetTools.HttpResult;

import java.util.List;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import com.alibaba.fastjson.JSON;

@Setter
@Getter
@Builder
public class ConfigService {

    private static final String CLIENT_IP    = NetTools.getLocalIP();

    private static final String PROTOCAL     = "http://";

    private static final String CONNECT_PATH = "/connect";

    private static final String DATA_PATH    = "/metric/jvm";

    private String              address;

    private Config              config;

    public void updateConfigAndSendEvents() {

        EventService.oOOo();
        List<Event> events = EventService.getLastOne();

        String params = null;
        params = NetTools.buildParams(params, "agent_ip", (!Startup.DEBUG || Startup.DEBUG_IP == null) ? CLIENT_IP : Startup.DEBUG_IP);
        params = NetTools.buildParams(params, "agent_version", Startup.AGENT_VERSION);
        params = NetTools.buildParams(params, "health_info", JSON.toJSONString(events));

        HttpResult hr = NetTools.httpPost(PROTOCAL + address + CONNECT_PATH, params);

        if (Startup.DEBUG) {
            System.out.println(params);
            System.out.println(hr.content);
        }

        if (hr.success) {
            config = JSON.parseObject(hr.content, Config.class);
            config.period = config.period * 1000;
        } else {
            EventService.addEvent(new Event(0, "Update Config Error ."));
        }
        EventService.cleanLastOne();
    }

    public void sendData() {
        try {
            DataService.oOOo();
            List<ResultData> result = DataService.getLastOne();
            for (ResultData rd : result) {
                rd.updateMeta(config);
                if (rd.getData().size() > 0) {
                    if (Startup.DEBUG)
                        System.out.println(JSON.toJSONString(rd.getMeta()));
                    NetTools.httpPost(PROTOCAL + address + DATA_PATH, NetTools.compress(rd));
                }
            }
            DataService.cleanLastOne();
        } catch (Exception ec) {
            ec.printStackTrace();
        }
    }
}
