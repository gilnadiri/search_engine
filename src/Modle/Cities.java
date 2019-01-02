package Modle;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.Iterator;

public class Cities {
    public JsonElement m_JsonE;


    public Cities() {
        try {
            OkHttpClient client = new OkHttpClient();
            HttpUrl.Builder urlBuilder = HttpUrl.parse("https://restcountries.eu/rest/v2/all?fields=capital;name;population;currencies").newBuilder();
            String url = urlBuilder.build().toString();
            Request request = new Request.Builder().url(url).build();
            Response response = client.newCall(request).execute();
            JsonParser parser = new JsonParser();
            m_JsonE = parser.parse(response.body().string());
        }
        catch (UnknownHostException e){}
        catch (IOException ex){}
    }


    public String [] getDetails(String city) {
        String [] ans={"","",""};
        if(m_JsonE==null)
            return ans;
        JsonArray fullDetailsArray = m_JsonE.getAsJsonArray();
        for (int i = 0; i < fullDetailsArray.size(); i++) {
            JsonObject objectCityDetails = (JsonObject) (fullDetailsArray.get(i));
            String capitalCity = objectCityDetails.get("capital").getAsString();

            if (capitalCity.contains(city)||city.contains(capitalCity)) {
                ans[0] = objectCityDetails.get("name").getAsString();
                ans[1] = objectCityDetails.get("population").getAsString();
                JsonArray JE = objectCityDetails.getAsJsonArray("currencies");
                for (Object o : JE) {
                    JsonObject jsonLineItem = (JsonObject) o;
                    JsonElement val=jsonLineItem.get("name");
                    if(val==null) return ans;
                    ans[2]=val.getAsString();
                }
            }
        }
        return ans;
    }
}