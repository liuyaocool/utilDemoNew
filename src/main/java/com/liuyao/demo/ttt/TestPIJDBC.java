package com.liuyao.demo.ttt;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

public class TestPIJDBC {

    public static void main(String[] args) {
        // arguments: java GetBAMetaData dasName [trustedConnection protocolOrder logLevel]
        // example: java GetBAMetaData myDas yes nettcp:5462 2
        String dasName = "localhost";
        String dataSourceName = "192.168.1.32";
        String isTrustedConnection = "Yes";
        String protocolOrder = "Https/Soap:5461,NetTcp:5462";
        String logLevel = "0";

        System.out.println("\nArguments:");
        System.out.println("\tData Access Server Name: " + dasName);
        System.out.println("\tUse trusted connection?: " + isTrustedConnection);
        System.out.println("\tProtocol order: " + protocolOrder);
        System.out.println("\tLog level: " + logLevel);


        Connection connection = null;
        String url = "";
        String driverClassName = "com.osisoft.jdbc.Driver";
        ResultSet resultSet = null;
        Properties properties = new Properties();

        //url = "jdbc:piintegrator://" + dasName;
        //url = "jdbc:pioledb://" + dasName + "/Data Source=" + dataSourceName + "; Integrated Security=SSPI";
        url = "jdbc:pioledb://" + dasName + "/Data Source=" + dataSourceName + "; Integrated Security=SSPI";
        System.out.println(url);
        properties.put("TrustedConnection", isTrustedConnection);
        properties.put("ProtocolOrder", protocolOrder);

        properties.put("LogConsole", "True");
        properties.put("LogLevel", logLevel);

        try {
            Class.forName(driverClassName).newInstance();
//            connection = DriverManager.getConnection(url, properties);
            connection = DriverManager.getConnection(url);

            DatabaseMetaData metaData = connection.getMetaData();
            System.out.println(metaData.getDriverName() + " " + metaData.getDriverVersion());
            System.out.println(metaData.getDatabaseProductName());
            System.out.println(metaData.getDatabaseProductVersion() + "\n");

            // get all tables and views
            String tTypes[] = new String[]{"TABLE", "VIEW"};
            resultSet = metaData.getTables(null, null, "%", tTypes);

            if (!resultSet.first())
                throw new Exception("No rows in the result set.");

            // we are just interested in those 4 columns
            System.out.print("Catalog || ");
            System.out.print("Schema || ");
            System.out.print("Table || ");
            System.out.println("Table Type\n");

            // read the data
            while (!resultSet.isAfterLast()) {
                System.out.print(resultSet.getString("TABLE_CAT") + " || ");
                System.out.print(resultSet.getString("TABLE_SCHEM") + " || ");
                System.out.print(resultSet.getString("TABLE_NAME") + " || ");
                System.out.println(resultSet.getString("TABLE_TYPE"));
                resultSet.next();
            }

        } catch (Exception ex) {
            System.err.println(ex);
        } finally {
            try {
                if (resultSet != null) resultSet.close();
                if (connection != null) connection.close();
            } catch (SQLException ex) {
                System.err.println(ex);
            }
        }
    }
}
