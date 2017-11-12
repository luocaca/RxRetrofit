package luocaca.rxretrofit.bean;

import java.util.List;

/**
 * Created by Administrator on 2017/8/26 0026.
 */

public class AllCity {

    private String resultcode;


    private String error_code = "";

    private String reason = "";


    private List<City> result ;

    public String getError_code() {
        return error_code;
    }

    public void setError_code(String error_code) {
        this.error_code = error_code;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public List<City> getResult() {
        return result;
    }

    public void setResult(List<City> result) {
        this.result = result;
    }

    public String getResultcode() {
        return resultcode;
    }

    public void setResultcode(String resultcode) {
        this.resultcode = resultcode;
    }
}
