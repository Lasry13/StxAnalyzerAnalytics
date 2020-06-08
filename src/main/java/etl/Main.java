package etl;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import utils.MySqlConnector;

public class Main {
    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        MySqlConnector mySqlConnector = new MySqlConnector();
        String sql = "SELECT Symbol from sp_500";
        mySqlConnector.connect();
        ResultSet resultSet = mySqlConnector.query(sql);
        String symbol = "";
        AlphaVentage alphaVentage = new AlphaVentage("TIME_SERIES_INTRADAY", null, "60min");
        int counter = 1;
        while (resultSet.next()) {
            symbol = resultSet.getString("Symbol");
            alphaVentage.setSymbol(symbol);
            try {
                String jsonTransactions = alphaVentage.getResponseFromAlpha();
                insertTransactionsToDb(jsonTransactions, connection);
                if (counter % 5 == 0)
                    Thread.sleep(70000);
                counter++;
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        }

        mySqlConnector.disconnect();
    }

    public static void insertTransactionsToDb(String jsonTransactions, Connection connection) throws IOException, SQLException {
        JsonFactory factory = new JsonFactory();
        JsonParser parser = factory.createParser(jsonTransactions);
        String sql = "INSERT INTO raw_data_sp_500_hourly values (?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        parser.nextToken();
        while (!parser.isClosed()) {
            JsonToken jsonToken = parser.nextToken();
            String fieldName = parser.getCurrentName();
            if (JsonToken.FIELD_NAME.equals(jsonToken) && fieldName.contains("-"))
                preparedStatement.setString(7, parser.getCurrentName());
            else if (JsonToken.VALUE_STRING.equals(jsonToken)) {
                switch (fieldName) {
                    case "2. Symbol":
                        preparedStatement.setString(1, parser.getValueAsString());
                        break;
                    case "1. open":
                        preparedStatement.setDouble(2, parser.getValueAsDouble());
                        break;
                    case "2. high":
                        preparedStatement.setDouble(3, parser.getValueAsDouble());
                        break;
                    case "3. low":
                        preparedStatement.setDouble(4, parser.getValueAsDouble());
                        break;
                    case "4. close":
                        preparedStatement.setDouble(5, parser.getValueAsDouble());
                        break;
                    case "5. volume":
                        preparedStatement.setLong(6, parser.getValueAsLong());
                        preparedStatement.executeUpdate();
                        break;
                }
            }
        }
    }
}