package com.example.parserexcelbellsoftmavenfx.DB;

import com.example.parserexcelbellsoftmavenfx.Configuration.Configuration;
import com.example.parserexcelbellsoftmavenfx.Errors.Errno;
import com.example.parserexcelbellsoftmavenfx.Models.TemplateOne;
import com.example.parserexcelbellsoftmavenfx.Models.TemplateThree;
import com.example.parserexcelbellsoftmavenfx.Models.TemplateTwo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


import java.sql.*;
import java.util.ArrayList;

public class Database extends Errno {

    private String user;
    private String password;
    private String host;
    private String databaseName;

    private String url  = "jdbc:mysql://HOST:PORT/DBNAME";

    private int port = 0;

    private Connection conn;

    private final Logger log;

    public Database(String user, String password, String host, int port, String databaseName) {
        this.user = user;
        this.password = password;
        this.host = host;
        this.databaseName = databaseName;
        this.port = port;

        log = LogManager.getLogger(getClass().getName());
    }

    public boolean openConnection(){

        if(conn != null){
            setState(STATE_CONNECTION_ALREADY_OPEN, "");
            log.error(this.getErrnoStr());
            return false;
        }

        this.url = this.url.replace("HOST", this.host).replace("PORT", Integer.toString(port)).replace("DBNAME", databaseName);

        try {
            this.conn = DriverManager.getConnection(this.url, this.user, this.password);
        }
        catch (Exception ex){
            setState(STATE_OTHER_EXCEPTION, ex.getMessage());
            log.error(this.getErrnoStr());
            return false;
        }
        finally {
            setState(STATE_OK, "");
            log.info("Database connection is opened!");
            return true;
        }
    }

    public void insertDataTableOne(ArrayList<ArrayList<String>> data){
        String query = "insert into SimpleTableForTemplateOne values(null, ?, ?)";
        PreparedStatement stmt = null;

        for(int counter = 0; counter < data.size(); ++counter) {

            try {
                stmt = conn.prepareStatement(query);

                stmt.setString(1, data.get(counter).get(0));
                stmt.setString(2, data.get(counter).get(1));

                stmt.execute();
            } catch (Exception ex) {
                setState(STATE_OTHER_EXCEPTION, ex.getMessage());
                log.error(this.getErrnoStr());
                return;
            }
            log.info("Inserted a line from table SimpleTableForTemplateOne");
        }
    }

    public void insertDataTableOne(String name, String description){
        String query = "insert into SimpleTableForTemplateOne values(null, ?, ?)";
        PreparedStatement stmt = null;

        try {
            stmt = conn.prepareStatement(query);

            stmt.setString(1, name);
            stmt.setString(2, description);

            stmt.execute();
        } catch (Exception ex) {
            setState(STATE_OTHER_EXCEPTION, ex.getMessage());
            log.error(this.getErrnoStr());
            return;
        }
        finally {
            log.info("Inserted a line from table SimpleTableForTemplateOne");
        }
    }

    public void insertDataTableTwo(ArrayList<ArrayList<String>> data){
        String query = "insert into SimpleTableForTemplateTwo values(null, ?, ?, ?)";
        PreparedStatement stmt = null;
        Double num = 0.0;

        for(int counter = 0; counter < data.size(); ++counter) {

            try {
                num = Double.parseDouble(data.get(counter).get(2));

                stmt = conn.prepareStatement(query);

                stmt.setString(1, data.get(counter).get(0));
                stmt.setString(2, data.get(counter).get(1));
                stmt.setInt(3, num.intValue());

                stmt.execute();
            } catch (Exception ex) {
                setState(STATE_OTHER_EXCEPTION, ex.getMessage());
                log.error(this.getErrnoStr());
                return;
            }
            finally {
                log.info("Inserted a line from table SimpleTableForTemplateTwo");
            }
        }
    }

    public void insertDataTableTwo(String name, String description, long value){
        String query = "insert into SimpleTableForTemplateTwo values(null, ?, ?, ?)";
        PreparedStatement stmt = null;

        try {
            stmt = conn.prepareStatement(query);

            stmt.setString(1, name);
            stmt.setString(2, description);
            stmt.setLong(3, value);

            stmt.execute();
            } catch (Exception ex) {
                setState(STATE_OTHER_EXCEPTION, ex.getMessage());
                log.error(this.getErrnoStr());
                return;
            }
            finally {
            log.info("Inserted a line from table SimpleTableForTemplateTwo");
        }
    }

    public void insertDataTableThree(ArrayList<ArrayList<String>> data){
        String query = "insert into SimpleTableForTemplateThree values(null, ?, ?, ?, ?)";
        PreparedStatement stmt = null;
        Double num = 0.0;
        Double num2 = 0.0;

        for(int counter = 0; counter < data.size(); ++counter) {

            try {
                num = Double.parseDouble(data.get(counter).get(2));
                num2 = Double.parseDouble(data.get(counter).get(3));

                stmt = conn.prepareStatement(query);

                stmt.setString(1, data.get(counter).get(0));
                stmt.setString(2, data.get(counter).get(1));
                stmt.setInt(3, num.intValue());
                stmt.setBoolean(4, (num2 != 0.0)? true : false);

                stmt.execute();
            } catch (Exception ex) {
                setState(STATE_OTHER_EXCEPTION, ex.getMessage());
                log.error(this.getErrnoStr());
                return;
            }
            finally {
                log.info("Inserted a line from table SimpleTableForTemplateThree");
            }
        }
    }

    public void insertDataTableThree(String name, String description, long value, boolean availability){
        String query = "insert into SimpleTableForTemplateThree values(null, ?, ?, ?, ?)";
        PreparedStatement stmt = null;

        try {
            stmt = conn.prepareStatement(query);

            stmt.setString(1, name);
            stmt.setString(2, description);
            stmt.setLong(3, value);
            stmt.setBoolean(4, availability);

            stmt.execute();
        } catch (Exception ex) {
            setState(STATE_OTHER_EXCEPTION, ex.getMessage());
            log.error(this.getErrnoStr());
            return;
        }
        finally {
            log.info("Inserted a line from table SimpleTableForTemplateThree");
        }
    }

    public ArrayList<ArrayList<String>> getDataTableOne(String template){
        String query = "select * from SimpleTableForTemplateOne";
        PreparedStatement stmt = null;
        TemplateOne templateOne = null;
        ResultSet resultSet = null;

        ArrayList<ArrayList<String>> list = new ArrayList<ArrayList<String>>();
        ArrayList<String> listElement = new ArrayList<String>();

        try {
            stmt = conn.prepareStatement(query);

            resultSet = stmt.executeQuery();

            while (resultSet.next()){
                log.info("Got a line from table SimpleTableForTemplateOne");
                listElement.clear();

                templateOne = new TemplateOne(0, resultSet.getString(template.split(":")[0]), resultSet.getString(template.split(":")[1]));

                //listElement.add(Integer.toString(templateOne.getId()));
                listElement.add(templateOne.getName());
                listElement.add(templateOne.getDescription());

                list.add((ArrayList<String>) listElement.clone());
            }
        }
        catch (Exception ex){
            setState(STATE_OTHER_EXCEPTION, ex.getMessage());
            log.error(this.getErrnoStr());
            return list;
        }

        return list;
    }

    public ArrayList<ArrayList<String>> getDataTableTwo(String template){
        String query = "select * from SimpleTableForTemplateTwo";
        PreparedStatement stmt = null;
        TemplateTwo templateTwo = null;
        ResultSet resultSet = null;

        ArrayList<ArrayList<String>> list = new ArrayList<ArrayList<String>>();
        ArrayList<String> listElement = new ArrayList<String>();

        try {
            stmt = conn.prepareStatement(query);

            resultSet = stmt.executeQuery();

            while (resultSet.next()){
                log.info("Got a line from table SimpleTableForTemplateTwo");
                listElement.clear();

                templateTwo = new TemplateTwo(0, resultSet.getString(template.split(":")[0]), resultSet.getString(template.split(":")[1]), resultSet.getLong(template.split(":")[2]));

                //listElement.add(Integer.toString(templateTwo.getId()));
                listElement.add(templateTwo.getName());
                listElement.add(templateTwo.getDescription());
                listElement.add(Long.toString(templateTwo.getValue()));

                list.add((ArrayList<String>) listElement.clone());
            }
        }
        catch (Exception ex){
            setState(STATE_OTHER_EXCEPTION, ex.getMessage());
            log.error(this.getErrnoStr());
            return list;
        }

        return list;
    }

    public ArrayList<ArrayList<String>> getDataTableThree(String template){
        String query = "select * from SimpleTableForTemplateThree";
        PreparedStatement stmt = null;
        TemplateThree templateThree = null;
        ResultSet resultSet = null;

        ArrayList<ArrayList<String>> list = new ArrayList<ArrayList<String>>();
        ArrayList<String> listElement = new ArrayList<String>();

        try {
            stmt = conn.prepareStatement(query);

            resultSet = stmt.executeQuery();

            while (resultSet.next()){
                log.info("Got a line from table SimpleTableForTemplateThree");
                listElement.clear();

                templateThree = new TemplateThree(0, resultSet.getString(template.split(":")[0]), resultSet.getString(template.split(":")[1]), resultSet.getLong(template.split(":")[2]), resultSet.getBoolean(template.split(":")[3]));

                //listElement.add(Integer.toString(templateThree.getId()));
                listElement.add(templateThree.getName());
                listElement.add(templateThree.getDescription());
                listElement.add(Long.toString(templateThree.getValue()));
                listElement.add(Boolean.toString(templateThree.getAvailability()));

                list.add((ArrayList<String>) listElement.clone());
            }
        }
        catch (Exception ex){
            setState(STATE_OTHER_EXCEPTION, ex.getMessage());
            log.error(this.getErrnoStr());
            return list;
        }

        return list;
    }

    public void closeConnection(){
        if(conn == null){
            setState(STATE_CONNECTION_IS_NOT_OPEN, "");
            log.error(this.getErrnoStr());
            return;
        }

        try {
            this.conn.close();
        }
        catch (Exception ex){
            setState(STATE_OTHER_EXCEPTION, ex.getMessage());
            log.error(this.getErrnoStr());
            return;
        }
        finally {
            log.info("Database connection is closed!");
        }
    }

    public int getPort(){
        return this.port;
    }

    public String getHost(){
        return this.host;
    }

    public String getUser(){
        return this.user;
    }

    public String getPassword(){
        return this.password;
    }

    public String getDatabaseName(){
        return this.databaseName;
    }

    public String getUrl(){
        return this.url;
    }

    public void setHost(String host){
        this.host = host;
    }

    public void setUser(String user){
        this.user = user;
    }

    public void setPassword(String password){
        this.password = password;
    }

    public void setDatabaseName(String databaseName){
        this.databaseName = databaseName;
    }

    public void setPort(int port){
        this.port = port;
    }
}
