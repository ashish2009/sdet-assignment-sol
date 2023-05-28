package com.assignment.helper;


import com.assignment.UserInsertConstant;
import com.assignment.utils.ConnectionUtil;
import com.assignment.utils.DBUtil;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import static org.awaitility.Awaitility.await;

public class CustomerDbHelper {

    private String query;
    private DBUtil util;

    private ArrayList<String> data;
    private Connection connection;
    private ConnectionUtil connectionUtil;

    private static CustomerDbHelper instance;

    private CustomerDbHelper() {
        connectionUtil = ConnectionUtil.getInstance();
        connection = connectionUtil.DatabaseConnector(System.getProperty("dbPath")+"/"+UserInsertConstant.DB);
        util = new DBUtil();
    }

    public static synchronized CustomerDbHelper getInstance(){
        if(instance == null){
            instance = new CustomerDbHelper();
        }
        return instance;
    }

    public ArrayList<String> execute(String query){
        this.query = query;
        try {
            data = util.readDatabaseRecords(connection,query);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return data;
    }

    public String getName(){
        return data.get(1);
    }

    public String getId(){
        return data.get(0);
    }

    public String getPhoneNumber(){
        return data.get(2);
    }

    public String getSMSSent(){
        await().atMost(20, TimeUnit.SECONDS).ignoreExceptions().until(isSmsSent());
        return data.get(3);
    }

    private Callable<Boolean> isSmsSent(){
        return () -> execute(query).get(3).equals("1");
    }

    public void closeDbConnection(){
        connectionUtil.close(connection);
    }

}
