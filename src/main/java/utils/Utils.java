package utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;


public class Utils {
    public static String convertResultSetToJsonArray(ResultSet rs) {
        String json = "";
        ObjectMapper mapper = new ObjectMapper();
        ArrayNode arrayNode = mapper.createArrayNode();
        try {
            ResultSetMetaData resultSetMetaData = rs.getMetaData();
            while (rs.next()) {
                ObjectNode objectNode = mapper.createObjectNode();
                for (int i = 1; i <= resultSetMetaData.getColumnCount(); i++) {
                    String column_name = resultSetMetaData.getColumnName(i);
                    String columnType = resultSetMetaData.getColumnTypeName(i);
                    switch (columnType) {
                        case "DATE":
                        case "DATETIME":
                            objectNode.put(column_name, String.valueOf(rs.getDate(column_name)));
                            break;
                        case "VARCHAR":
                            objectNode.put(column_name, rs.getString(column_name));
                            break;
                        case "BIGINT":
                            objectNode.put(column_name, rs.getLong(column_name));
                            break;
                        case "FLOAT":
                        case "DOUBLE":
                            objectNode.put(column_name, rs.getFloat(column_name));
                            break;
                        case "TEXT": {
                            objectNode.put(column_name, rs.getString(column_name));
                            break;
                        }
                    }
                }
                arrayNode.add(objectNode);
            }
            json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(arrayNode);
        } catch (SQLException | JsonProcessingException ex) {
            ex.printStackTrace();
        }
        return json;
    }
}
