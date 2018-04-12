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
            Connection con = DriverManager.getConnection("jdbc:hive2://localhost:10000/default","root","root");
            PreparedStatement sta = con.prepareStatement("select * from b_results");
            ResultSet result = sta.executeQuery();
            while(result.next()){
                System.out.println(result.getString(1));
                System.out.println(result.getString(2));
                System.out.println(result.getString(3));
            }
        } catch(SQLException e) {
            //e.printStackTrace();
           System.out.println("--------------test---------------");
        }
    }
}
