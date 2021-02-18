package com.liuyao.demo.utilutil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class CacheUtils {

    public static final String DICT_KEY = "dictKey";//<code, name>
    public static final String CRAFT_KEY = "tb_base_supervisionprocess";//<id, name>
    public static final String COMPANY_KEY = "tb_base_company";//<srcId, companyCode>

    private static final ConcurrentMap<String, Object> CACHE = new ConcurrentHashMap<>();

    private static synchronized ConcurrentMap dictInit(String type, boolean ifReload) {
        if (ifReload || null == CACHE.get(DICT_KEY) || null == ((ConcurrentMap) CACHE.get(DICT_KEY)).get(type)){
//            RiskZlMapper mapper = SpringUtils.getBean(RiskZlMapper.class);
//            if (null == mapper) { return new ConcurrentHashMap(); }
//            List<Map> datas = mapper.commonList("tb_out_datadict", null);
            List<Map> datas = new ArrayList<>();
            if (!CACHE.containsKey(DICT_KEY)){
                CACHE.put(DICT_KEY, new ConcurrentHashMap<>());
            }
            ConcurrentMap dictCache = (ConcurrentMap) CACHE.get(DICT_KEY);
            for (int i = 0; i < datas.size(); i++) {
                Map data = datas.get(i);
                Object dicType = data.get("resourceid");
                if (!dictCache.containsKey(dicType)){
                    dictCache.put(dicType, new ConcurrentHashMap<>());
                }
                ((ConcurrentHashMap) dictCache.get(dicType)).put(data.get("dictcode"), data.get("dictname"));
            }
        }
        return (ConcurrentMap) ((ConcurrentMap) CACHE.get(DICT_KEY)).get(type);
    }

    private static synchronized ConcurrentMap tableToCache(String table, String keyName, String valueName, boolean ifReload){
        if (ifReload || null == CACHE.get(table)) {
//            RiskZlMapper mapper = SpringUtils.getBean(RiskZlMapper.class);
//            if (null == mapper) { return new ConcurrentHashMap(); }
//            List<Map> datas = mapper.commonList(table, null);
            List<Map> datas = new ArrayList<>();
            if (!CACHE.containsKey(table)){
                CACHE.put(table, new ConcurrentHashMap<>());
            }
            for (int i = 0; i < datas.size(); i++) {
                Object srcId = datas.get(i).get(keyName);
                if (null == srcId){
                    continue;
                }
                ((ConcurrentHashMap) CACHE.get(table)).put(srcId, datas.get(i).get(valueName));
            }
        }
        return (ConcurrentMap) CACHE.get(table);
    }

    public static ConcurrentMap findDict(String type, boolean ifReload){
        switch (type){
            case CRAFT_KEY:
                return tableToCache(type,"id", "processname", ifReload);
            case COMPANY_KEY:
                return tableToCache(type,"src_id", "companycode", ifReload);
            default: return dictInit(type, ifReload);
        }
    }

    //此方法仅在字典是map时可用
    public static String findName(String type, String code, String def){
        ConcurrentMap dicts = findDict(type, false);
        if (null == dicts.get(code)) {
            return def;
        }
        String res = String.valueOf(dicts.get(code));
        return "".equals(res) ? def : res;
    }
    //此方法仅在字典是map时可用
    public static String findCode(String type, String name, String def){
        ConcurrentMap dicts = findDict(type, false);
        for (Object key : dicts.keySet()) {
            if (name.equals(dicts.get(key))){
                String res = String.valueOf(key);
                return "".equals(res) ? def : res;
            }
        }
        return def;
    }

}
