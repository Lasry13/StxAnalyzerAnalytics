package etl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class AlphaVentage {
    private static final String BASE_URL = "https://www.alphavantage.co/query?";
    private final String function;
    private String symbol;
    private final String interval;
    private static final String API_KEY = "B71KGBZUF4G538AQ";

    public AlphaVentage(String function, String symbol, String interval) {
        this.function = function;
        this.symbol = symbol;
        this.interval = interval;
    }

    public String getUrlForRequest(){
        return BASE_URL + "function=" + function + "&symbol=" + symbol + "&interval=" + interval + "&apikey=" + API_KEY;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getResponseFromAlpha() throws IOException {
        URL obj = new URL(getUrlForRequest());
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod("GET");
        int responseCode = con.getResponseCode();
        System.out.println("GET Response Code :: " + responseCode);
        if (responseCode == HttpURLConnection.HTTP_OK) {
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            System.out.println(response.toString());
            return response.toString();
        } else {
            System.out.println("GET request not worked");
            return null;
        }


    }
}
