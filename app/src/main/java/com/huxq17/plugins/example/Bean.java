package com.huxq17.plugins.example;

/**
 * Created by 2144 on 2016/11/24.
 */

public class Bean {

    /**
     * code : 200
     * message : 成功
     * data : {"version":"12","sha1":"427e7dd2867d9cc81d43e0284c4a46c5cbad2fc7","sign":"vKWHmO0CCJYEtdtovV+9cQhwRlMPdnss29BDMEH/zGcmWQBKpKRU0XMRyuUfxuItoqLQebYacIOPUNCoXh1smc88eoFY4HjaVVMCM3XXceGgz52TTllTdrGQb+pR03OvAOaxVhvwjpwIVTQQKuke1uP5iCgMznokal2e5DwsWwzdV4Wcs5kNHGwrgEHrd0gp57uNMmOWplJi12sP8K3FsVLa/93fo4JI1amKhZ+T2CJuZvkewcazReADNcyKDvsLPQ/oeW/Yl500QVxXW0AtV44mp3CCoMgU6m2g90mAvQyWuqBYtp1aHjUM+9NqtBSHWA5w41S9AlvgwmScT5XgFw==","url":"http://static.2144.cn/sj/sjlp/v1.1/patch/v12.zip","condition":"between,uid,10000,20000"}
     */

    private int code;
    private String message;
    /**
     * version : 12
     * sha1 : 427e7dd2867d9cc81d43e0284c4a46c5cbad2fc7
     * sign : vKWHmO0CCJYEtdtovV+9cQhwRlMPdnss29BDMEH/zGcmWQBKpKRU0XMRyuUfxuItoqLQebYacIOPUNCoXh1smc88eoFY4HjaVVMCM3XXceGgz52TTllTdrGQb+pR03OvAOaxVhvwjpwIVTQQKuke1uP5iCgMznokal2e5DwsWwzdV4Wcs5kNHGwrgEHrd0gp57uNMmOWplJi12sP8K3FsVLa/93fo4JI1amKhZ+T2CJuZvkewcazReADNcyKDvsLPQ/oeW/Yl500QVxXW0AtV44mp3CCoMgU6m2g90mAvQyWuqBYtp1aHjUM+9NqtBSHWA5w41S9AlvgwmScT5XgFw==
     * url : http://static.2144.cn/sj/sjlp/v1.1/patch/v12.zip
     * condition : between,uid,10000,20000
     */

    private DataBean data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        private String version;
        private String sha1;
        private String sign;
        private String url;
        private String condition;

        public String getVersion() {
            return version;
        }

        public void setVersion(String version) {
            this.version = version;
        }

        public String getSha1() {
            return sha1;
        }

        public void setSha1(String sha1) {
            this.sha1 = sha1;
        }

        public String getSign() {
            return sign;
        }

        public void setSign(String sign) {
            this.sign = sign;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getCondition() {
            return condition;
        }

        public void setCondition(String condition) {
            this.condition = condition;
        }
    }
}
