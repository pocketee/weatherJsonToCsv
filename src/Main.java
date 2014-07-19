import org.json.CDL;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

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


            JSONArray docs;

            String jsonString = "{\"infile\": [{\"field1\": 11,\"field2\": 12,\"field3\": 13},{\"field1\": 21,\"field2\": 22,\"field3\": 23},{\"field1\": 31,\"field2\": 32,\"field3\": 33}]}";
            //JSONObject output = new JSONObject(jsonString);


            JSONObject jsonHead = new JSONObject(sb.toString());
            JSONObject header = (JSONObject)jsonHead.get("response");
            JSONObject body = (JSONObject)header.get("body");
            JSONObject items = (JSONObject)body.get("items");

            System.out.println(items.toString());
            System.out.println();

            docs = items.getJSONArray("item");


            BufferedWriter file = new BufferedWriter(new FileWriter("weather"+".csv"));
            String csvString = CDL.toString(docs);
            file.write(csvString);
            file.close();
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
