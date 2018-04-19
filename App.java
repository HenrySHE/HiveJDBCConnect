//App.java
package twitter.bijection.Program;

import java.sql.*;

public class App
{

    public static void main( String[] args ) throws ClassNotFoundException
    {
                // Create a local StreamingContext with two working thread and batch interval of 1 second
        System.out.println("Testing Hive connection");
        Class.forName("org.apache.hive.jdbc.HiveDriver");
        try{
            //The connection needs: 1. Tunneling + 2. HKUCS VPN Connection.
            Connection con = DriverManager.getConnection("jdbc:hive2://202.45.128.135:16859/default","root","root");
            //PreparedStatement sta = con.prepareStatement("select * from test");
            ///merged/April13_student59_1523769336533_1523769336513_April13.csv
            Statement sta = con.createStatement();
            String testVal = "testHiveJDBC";
            //String sql = "LOAD DATA INPATH '/merged/April13_student59_1523770973995_1523770973975_April13.csv' OVERWRITE INTO TABLE temp2";

            //String sql = "LOAD DATA INPATH '/merged/*' INTO TABLE b_results";
            
            String sql = "LOAD DATA INPATH '/merged/*' OVERWRITE INTO TABLE b_results";
            //ResultSet result = sta.executeQuery(sql);
            sta.execute(sql);
            String insertionSQL = "INSERT OVERWRITE TABLE evla select t_date, t_time, max(b_price), s_output, count(s_output) from b_results group by t_date, t_time, s_output";
            sta.execute(insertionSQL);
            String leftJoinSQL = "INSERT OVERWRITE TABLE evl SELECT evla.t_date, evla.t_time, evla.b_price, evla.s_output, evlb.r_weight, evla.number FROM evla JOIN evlb ON evla.s_output=evlb.s_output ORDER BY evla.t_time";
            sta.execute(leftJoinSQL);
            String resultSQL = "INSERT INTO TABLE evl_result select evl.t_date,evl.t_time,max(evl.b_price),sum(evl.number* evl.coefficient) from evl group by evl.t_date,evl.t_time";
            sta.execute(resultSQL);
            // while(result.next()){
            //     System.out.println(result.getString(1));
            //     System.out.println(result.getString(2));
            //     System.out.println(result.getString(3));
            // }
        } catch(SQLException e) {
           e.printStackTrace();
           //System.out.println("--Error Happened--");
        }
    }
}
