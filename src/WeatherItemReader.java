import org.json.CDL;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by jh on 2014-07-19.
 */
public class WeatherItemReader {
    public static JSONObject getItemFromURL(String where, String baseDate, String baseTime) {
        String urlStr = "http://newsky2.kma.go.kr/service/SecndSrtpdFrcstInfoService/ForecastSpaceData?ServiceKey=PNqRUudJI%2FP5DZOFlscONuFOafdaqBZQ0LbQmlLM7eYFrUuQMh4svcDy9lD5WpIX3vBZFYLcRGoYhocI5ARq5A%3D%3D";
        urlStr += "&base_date=" + baseDate.toString() + "&base_time=" + baseTime.toString() + "&nx=100&ny=75" + "&_type=json";
        JSONObject resItem = new JSONObject();

        try {
            URL url = new URL(urlStr);
            URLConnection conn = url.openConnection();
            InputStream is = conn.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            char[] buff = new char[512];
            int len;

            StringBuilder sb = new StringBuilder("");
            while( (len = br.read(buff)) != -1) {
                String tmp = new String(buff, 0, len);
                sb.append(tmp);
            }
            //System.out.println(sb.toString());
            br.close();


            JSONObject jsonHead = new JSONObject(sb.toString());
            JSONObject response = (JSONObject)jsonHead.get("response");
            JSONObject body = (JSONObject)response.get("body");
            if (body.getInt("totalCount") == 0) { //아무 자료 없는 것이므로 null을 리턴
                return null;
            }

            JSONObject items = (JSONObject)body.get("items");


            JSONArray docs;
            Map<String, Boolean> checkMap = new HashMap<String, Boolean>();
            checkMap.put("POP", false); //강수확률
            checkMap.put("R06", false); //강수량
            checkMap.put("T3H", false); //기온
            checkMap.put("WAV", false); //파고


            docs = items.getJSONArray("item");
            for (int i=0; i<docs.length(); i++) {
                JSONObject item = (JSONObject)docs.get(i);

                for( String key : checkMap.keySet() ) {
                    if (checkMap.get(key)) {
                        continue;
                    }

                    if(item.get("category").equals(key)) { //강수확률
                        resItem.put(key, item.get("fcstValue"));
                        checkMap.put(key, true);
                    } else {
                        resItem.put(key, -1);
                    }
                }
            }


            resItem.put("WHERE", where); //어느 해수욕장?
            JSONObject tmpJSONObject = (JSONObject)docs.get(0);
            resItem.put("BASEDATE", tmpJSONObject.get("fcstDate")); //날짜
            resItem.put("BASETIME", tmpJSONObject.get("fcstTime")); //시간
            /*
            resItem.put("BASEDATE", baseDate); //날짜
            resItem.put("BASETIME", baseTime); //시간
            */
        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return resItem;
    }
}
