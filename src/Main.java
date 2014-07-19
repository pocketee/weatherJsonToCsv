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

        try {
            JSONArray resDocs = new JSONArray();
            JSONObject item = WeatherItemReader.getItemFromURL("Haeundae", "20140714", "1000");
            resDocs.put(item);

            String csvString = CDL.toString(resDocs);

            /*
            BufferedWriter file = new BufferedWriter(new FileWriter("weather"+".csv"));
            file.write(csvString);
            file.close();
            */

            System.out.println(csvString);
        }   catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }
}
