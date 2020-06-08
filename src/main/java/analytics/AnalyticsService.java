package analytics;

import utils.MySqlConnector;
import utils.Utils;

import java.sql.*;

public class AnalyticsService implements AnalyticsRepository {

    private final MySqlConnector mySqlConnector;

    public AnalyticsService(MySqlConnector mySqlConnector) {
        this.mySqlConnector = mySqlConnector;
    }

    public String getSummary(){
        String res = "";
        try {
            String sql = "select sp.symbol , sp.name, sp.sector, rd.open open, rd.high, rd.low, rd.close close, rd.volume,\n" +
                    "CASE\n" +
                    "    WHEN rd.open < rd.close THEN ROUND(100 - open/close*100,2)\n" +
                    "    WHEN rd.open > rd.close THEN ROUND(-1 * (100 - close/open*100),2)\n" +
                    "    ELSE 0\n" +
                    "END daily_change_percent,\n" +
                    "CASE\n" +
                    "    WHEN rd.open = rd.close THEN 0\n" +
                    "    ELSE ROUND(close-open,2)\n" +
                    "END daily_change_value\n" +
                    "from sp_500 sp \n" +
                    "join raw_data_sp_500_daily rd on sp.symbol = rd.symbol\n" +
                    "where open > 0 and close > 0 and date = '2020-05-13' \n" +
                    "order by daily_change_percent desc";
            mySqlConnector.connect();
            res = Utils.convertResultSetToJsonArray(mySqlConnector.query(sql));
        }

        catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            res = "";
        }

        finally {
            mySqlConnector.disconnect();
        }

        return res;
    }

    public String getSp500() throws SQLException, ClassNotFoundException {
        String sql = "SELECT * FROM sp_500";
        mySqlConnector.connect();
        String res = Utils.convertResultSetToJsonArray(mySqlConnector.query(sql));
        mySqlConnector.disconnect();
        return res;
    }
}