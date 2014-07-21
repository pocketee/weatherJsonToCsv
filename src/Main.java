import org.json.CDL;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Created by jh on 2014-07-19.
 */
public class Main {
    public static void main(String args[]) {
        final int DAYS_TO_PULL = 5;

        try {
            JSONArray resDocs = new JSONArray();

            LocalDate ld = LocalDate.of(2014, 07, 15);
            LocalTime lt;

            String dateStr;
            String timeStr;
            JSONObject item;


            for (int i=0; i<DAYS_TO_PULL; i++) { //몇일
                dateStr = strFromLocalDate(ld.plusDays(i));
                lt = LocalTime.of(0, 0);
                for (int j=0; j<24; j++) { //하루루
                    timeStr = strFromLocalTime(lt.plusMinutes(j*60));
                    item = WeatherItemReader.getItemFromURL("Haeundae", dateStr, timeStr);
                    if ( item == null ) {
                        continue;
                    }
                    resDocs.put(item);
                }
            }


            //item = WeatherItemReader.getItemFromURL("Haeundae", "20140718", "1100");
            //resDocs.put(item);

            String csvString = CDL.toString(resDocs);



            BufferedWriter file = new BufferedWriter(new FileWriter("weather"+".csv"));
            file.write(csvString);
            file.close();

            System.out.println("end");
        }   catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String strFromLocalDate(LocalDate ld) {
        String dateStr = "";
        dateStr += ld.getYear();
        dateStr += ld.getMonthValue()/10==0?"0"+ld.getMonthValue():ld.getMonthValue();
        dateStr += ld.getDayOfMonth()/10==0?"0"+ld.getDayOfMonth():ld.getDayOfMonth();

        return dateStr;
    }

    public static String strFromLocalTime(LocalTime lt) {
        String timeStr = "";

        timeStr += lt.getHour()/10==0?"0"+lt.getHour():lt.getHour();
        timeStr += lt.getMinute()/10==0?"0"+lt.getMinute():lt.getMinute();

        return timeStr;
    }
}
