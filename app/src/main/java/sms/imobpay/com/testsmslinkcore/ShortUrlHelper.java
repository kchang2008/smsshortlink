package sms.imobpay.com.testsmslinkcore;

import android.annotation.SuppressLint;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by jun on 17/9/19.
 * 短链生成器:新浪微博的短链实现方式
 */

public class ShortUrlHelper {
    private String TAG = "ShortUrlHelper";
    public static final String DEF_CHATSET = "UTF-8";//默认字符编码
    public static final int DEF_CONN_TIMEOUT = 60000;//默认超时连接时间
    public static final int DEF_READ_TIMEOUT = 60000;//默认下载连接时间
    public static final String GET = "GET";
    public static final String POST = "POST";
    public static final String BITLY_CODE = "568898243";//APPKEY
    //客户端浏览器类型
    //public static String userAgent =  "Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/29.0.1547.66 Safari/537.36";

    public static String get(String strUrl, Map<String, Object> params, String flag){
        return net(strUrl, params, GET, flag);
    }
    public static String post(String strUrl,Map<String, Object> params,String flag){
        return net(strUrl, params, POST, flag);
    }
    @SuppressLint("NewApi")//警告：如果url连接失败返回为null！！！
    private static String net(String strUrl, Map<String, Object> params,String method,String flag) {
        HttpURLConnection con = null;
        BufferedReader reader = null;
        String rs = null;
        try {
            //System.setProperty("http.keepAlive", "false");

            StringBuffer sb = new StringBuffer();
            if(method==null || method.equals("GET")){
                if(params!=null && params.size()>0)//如果有参数
                strUrl = strUrl+"?"+urlencode(params);//捎带手把字符给照url编码
            }
            URL url = new URL(strUrl);
            con = (HttpURLConnection) url.openConnection();
            if(method==null || method.equals("GET")){
                con.setRequestMethod("GET");
            }else{
                con.setRequestMethod("POST");
                con.setDoOutput(true);
            }
            con.setRequestProperty("Content-Type","application/x-www-form-urlencoded;charset=UTF-8");
            con.setReadTimeout(DEF_CONN_TIMEOUT);
            con.setConnectTimeout(DEF_READ_TIMEOUT);
            con.setDoOutput(true);
            con.setDoInput(true);
            con.connect();
            int respCode = con.getResponseCode();
            if (respCode != 200) {
                return "";
            }
            if (method.equals("POST")) {
                if(params!= null && params.size() > 0)
                try (DataOutputStream out = new DataOutputStream(con.getOutputStream())) {
                    out.writeBytes(urlencode(params));
                    out.flush();
                    out.close();
                }
            }
            InputStream is = con.getInputStream();
            reader = new BufferedReader(new InputStreamReader(is, DEF_CHATSET));//设置解析编码
            String strRead = null;
            while ((strRead = reader.readLine()) != null) {
                sb.append(strRead);
            }
            rs = sb.toString();
        } catch (IOException e) {
            Log.i("TAG", flag + "网络连接失败");
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.i("TAG",flag + "连接关闭失败");
                }
            }
            if (con != null) {
                con.disconnect();
            }
        }
        return rs;//如果失败返回为null
    }

    //将map型转为请求参数型
    public static String urlencode(Map<String, ?> data) {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, ?> i : data.entrySet()) {
            try {
                sb.append(i.getKey()).append("=").append(URLEncoder.encode(i.getValue()+"",DEF_CHATSET)).append("&");//按照默认参数对字符进行编码
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }

    public static String getShortUrl(String longUrl){
//        https://api-ssl.bitly.com/v3/user/link_save?access_token=3ae922985a170d3a7cc3e6e7e55b980b413e34e6&longUrl=www.baidu.com
        Log.i("TAG", "压缩前网址为" + longUrl);
        longUrl.replace("?", "/?");
        Map<String,Object> params = new HashMap<String,Object>();
        params.put("url_long",longUrl);
        params.put("source",BITLY_CODE); //source=3271760578

        String shortUrl = get("http://api.t.sina.com.cn/short_url/shorten.json",params,"获取短连接");
        Log.i("TAG","获取的短连接为"+shortUrl);
//[
//        {
//            "url_short":"http://t.cn/RpkU7Zl",
//                "url_long":"http://www.imobpay.com/test/v3_test/openapp/testSms.html?sms_extras=eyJmdW5jVHlwZSI6IjAiLCJjbGFzc05hbWVCeUFuZHJvaWQiOiJEcmF3aW5nQWNjb3VudExpc3QifQ==",
//                "type":0
//        }
//]
        try {
            JSONArray returnArray = new JSONArray(shortUrl);
            JSONObject data = returnArray.getJSONObject(0);
            shortUrl = data.getString("url_short");
            Log.i("TAG","最终获得的短连接为"+shortUrl);
        } catch (JSONException e) {
            Log.i("TAG","短url返回Json格式异常"+shortUrl);
            e.printStackTrace();
        }

        if(!StringUtils.isNotEmptyOrNull(shortUrl))return longUrl;
        return shortUrl;
    }

}
