package luocaca.rxretrofit.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/12/3 0003.
 */

public class Result implements Serializable {


    /**
     * code : 200
     * message : success
     * data : {"userid":1711111,"vip":"0","livevip":"1","credit":"0"}
     */

    public String code;
    public String message;
    public Databean data;

    public static class Databean implements Serializable {
        /**
         * userid : 1711111
         * vip : 0
         * livevip : 1
         * credit : 0
         */

        public int userid;
        public int vip;
        public int livevip;
        public String credit;

        @Override
        public String toString() {
            return "Databean{" +
                    "userid=" + userid +
                    ", vip='" + vip + '\'' +
                    ", livevip='" + livevip + '\'' +
                    ", credit='" + credit + '\'' +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "result{" +
                "code='" + code + '\'' +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}';
    }
}
