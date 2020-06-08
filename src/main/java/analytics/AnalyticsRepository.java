package analytics;

import java.sql.SQLException;

public interface AnalyticsRepository {
    String getSummary() throws SQLException, ClassNotFoundException;
    String getSp500() throws SQLException, ClassNotFoundException;
}
