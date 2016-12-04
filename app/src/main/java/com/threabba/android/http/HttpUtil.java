package com.threabba.android.http;

import com.threabba.android.config.Const;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

/**
 * Created by jun on 16. 12. 4.
 */

public class HttpUtil {

    // http 요청
    public static String connHttpGet(String addr, Map<String, String> params){
        StringBuffer response = new StringBuffer();
        try {
            URL url;
            if(params != null){
                StringBuffer buf = new StringBuffer(addr);
                buf.append("?");
                for(String entry : params.keySet()){
                    buf.append("&");
                    buf.append(entry+"="+params.get(entry));
                }
                url = new URL(buf.toString());
            }else{
                url = new URL(addr);
            }
            HttpURLConnection con = (HttpURLConnection)url.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("X-Naver-Client-Id", Const.CLIENT_ID);
            con.setRequestProperty("X-Naver-Client-Secret", Const.CLIENT_SECRET);
            int responseCode = con.getResponseCode();
            BufferedReader br;
            if(responseCode==200) { // 정상 호출
                br = new BufferedReader(new InputStreamReader(con.getInputStream()));
            } else {  // 에러 발생
                br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
            }
            String inputLine;
            while ((inputLine = br.readLine()) != null) {
                response.append(inputLine);
            }
            br.close();
            //System.out.println(response.toString());
        } catch (Exception e) {
            return null;
        }
        return response.toString();
    }


}
