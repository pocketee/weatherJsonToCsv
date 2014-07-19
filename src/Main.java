import org.json.CDL;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by jh on 2014-07-19.
 */
public class Main {
    public static void main(String args[]) {
        URL url;
        try {
            url = new URL("http://newsky2.kma.go.kr/service/SecndSrtpdFrcstInfoService/ForecastSpaceData?ServiceKey=PNqRUudJI%2FP5DZOFlscONuFOafdaqBZQ0LbQmlLM7eYFrUuQMh4svcDy9lD5WpIX3vBZFYLcRGoYhocI5ARq5A%3D%3D&base_date=20140715&base_time=1400&nx=100&ny=75&_type=json");
            URLConnection conn = url.openConnection();
            InputStream is = conn.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            char[] buff = new char[512];
            int len = -1;

            StringBuilder sb = new StringBuilder("");
            while( (len = br.read(buff)) != -1) {
                String tmp = new String(buff, 0, len);
                sb.append(tmp);
            }
            System.out.println(sb.toString());
            br.close();




            JSONObject jsonHead = new JSONObject(sb.toString());
            JSONObject header = (JSONObject)jsonHead.get("response");
            JSONObject body = (JSONObject)header.get("body");
            JSONObject items = (JSONObject)body.get("items");


            JSONArray docs;
            JSONArray resDocs = new JSONArray();

            Map<String, Boolean> checkMap = new HashMap<String, Boolean>();
            checkMap.put("POP", false); //강수확률
            checkMap.put("R06", false); //강수량
            checkMap.put("T3H", false); //기온
            checkMap.put("WAV", false); //파고


            docs = items.getJSONArray("item");

            JSONObject resItem = new JSONObject();
            resItem.put("WHERE", "Haeundae"); //어느 해수욕장?
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
            resDocs.put(resItem);

            String csvString = CDL.toString(resDocs);

            /*
            BufferedWriter file = new BufferedWriter(new FileWriter("weather"+".csv"));
            file.write(csvString);
            file.close();
            */

            System.out.println(csvString);
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

    }
}
