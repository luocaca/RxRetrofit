package luocaca.rxretrofit.http;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Administrator on 2017/11/23 0023.
 */

public class GetUtil {


    public static String get(String actionUrl) throws IOException {


        URL uri = new URL(actionUrl);
        HttpURLConnection conn = (HttpURLConnection) uri.openConnection();
        conn.setReadTimeout(30 * 1000); // 缓存的最长时间
        conn.setDoInput(true);// 允许输入
        conn.setDoOutput(true);// 允许输出
        conn.setUseCaches(false); // 不允许使用缓存
        conn.setRequestMethod("GET");
        conn.setRequestProperty("connection", "keep-alive");
//        conn.setRequestProperty("Charsert", "UTF-8");

//        StringBuilder sb = new StringBuilder();
//
//        DataOutputStream outStream = new DataOutputStream(
//                conn.getOutputStream());
//        if (!TextUtils.isEmpty(sb.toString())) {
//            outStream.write(sb.toString().getBytes());
//        }

        Log.i("HttpUtil", "conn.getContentLength():" + conn.getContentLength());

        // 得到响应码
        int res = conn.getResponseCode();
        InputStream in = conn.getInputStream();
        if (res == 200) {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in, "UTF-8"));
            StringBuffer buffer = new StringBuffer();
            String line = "";
            while ((line = bufferedReader.readLine()) != null) {
                buffer.append(line);
            }

//          int ch;
//          StringBuilder sb2 = new StringBuilder();
//          while ((ch = in.read()) != -1) {
//              sb2.append((char) ch);
//          }
//            conn.disconnect();
            return buffer.toString();
        }
//        outStream.close();

//        String sessionId = "";
//        String cookieVal = "";
//        String key = null;
        //取cookie
//        for (int i = 1; (key = conn.getHeaderFieldKey(i)) != null; i++) {
//            if (key.equalsIgnoreCase("set-cookie")) {
//                cookieVal = conn.getHeaderField(i);
//                cookieVal = cookieVal.substring(0, cookieVal.indexOf(";"));
//                sessionId = sessionId + cookieVal + ";";
//            }
//        }
        conn.disconnect();
        return "";

    }


    /**
     * post方式登录
     *
     * @param username
     * @param password
     * @param loginAction
     * @return
     * @throws Exception
     */
    public static String getCookie(String username, String password, String loginAction) throws Exception {
        //登录
        URL url = new URL(loginAction);
        String param = "username=" + username + "&password=" + password;
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setDoInput(true);
        conn.setDoOutput(true);
        conn.setRequestMethod("POST");
        OutputStream out = conn.getOutputStream();
        out.write(param.getBytes());
        out.flush();
        out.close();
        String sessionId = "";
        String cookieVal = "";
        String key = null;
        //取cookie
        for (int i = 1; (key = conn.getHeaderFieldKey(i)) != null; i++) {
            if (key.equalsIgnoreCase("set-cookie")) {
                cookieVal = conn.getHeaderField(i);
                cookieVal = cookieVal.substring(0, cookieVal.indexOf(";"));
                sessionId = sessionId + cookieVal + ";";
            }
        }
        return sessionId;
    }


}
