package phoenix.dao;

import java.util.List;
import java.util.Map;

public interface DualDao {

    public List<String> query(Map<String, Object> paramMap);

    public List<String> queryMobile(Map<String, Object> paramMap);

}
