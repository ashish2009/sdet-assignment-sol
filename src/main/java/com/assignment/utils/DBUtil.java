package com.assignment.utils;

import java.sql.*;
import java.util.ArrayList;

public class DBUtil {

    private ArrayList<String> testData;

    public synchronized ArrayList<String> readDatabaseRecords(Connection databaseConnection, String mysqlquery) throws SQLException {
        try (Statement stmt = databaseConnection.createStatement();
             ResultSet rs = stmt.executeQuery(mysqlquery);) {
            testData = new ArrayList<>();
            ResultSetMetaData meta = rs.getMetaData();
            int columncount = meta.getColumnCount();
            while (rs.next()) {
                int i = 1;
                while (i <= columncount) {
                    testData.add(rs.getString(i++));

                }
            }

        } catch (Exception exp) {
            System.out.println(exp.getMessage());
        }

        if (testData.size() != 0) {
            return testData;
        } else {
            System.out.println("No data found for this user");
            return null;
        }
    }
}
