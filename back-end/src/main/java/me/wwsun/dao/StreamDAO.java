package me.wwsun.dao;

import PathCal.Log2Stream;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
/**
 * Created by Administrator on 2015/3/14.
 */
public class StreamDAO {
    private String URI;
    private String db;
    private Connection conn;
    public StreamDAO(String URI,String db){
        try{
            this.URI=URI;
            this.db=db;
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection(URI+"/"+ db, "root", "million");
        }
        catch (ClassNotFoundException e){
            e.printStackTrace();
        }
        catch (SQLException e){
            e.printStackTrace();
        }
    }

    protected void finalize(){
        try{
            conn.close();
        }
        catch(SQLException e){
            e.printStackTrace();
        }
    }

    public boolean insertStreams(List<ArrayList<String[]>> Streams,String session){
        try{
            String params[]={
                    "mongodb://223.3.75.101:27017",
                    "jiaodian",
            };
            Log2Stream log = new Log2Stream(params);
            Statement stmt=conn.createStatement();
            String insert;
            for(List<String[]> stream:Streams){
                insert="insert into streams values (";
                for(String[] point:stream){
                    String value=point[Log2Stream.url].equals("nul")?"nul":log.getIndxByUrl(point[Log2Stream.url]).toString();
                    insert+="'"+value+"',";
                }
                insert+="'"+session+"');";
                System.out.println(insert);
                stmt.executeUpdate(insert);
            }
        }
        catch(Exception e){
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
