package admin;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Label;
 
import admin.UserStatus;
import admin.ViewFilter;
 
public class ViewUser {
 
    private static List<UserStatus> userList = new ArrayList<UserStatus>();
    private static void initData() {
        if (!userList.isEmpty())
        	userList.clear();
        PreparedStatement stmt;
		Connection conn;
		ResultSet rs;
		try
		{
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection("jdbc:mysql://localhost/BTS?user=root&password=password");
			stmt = conn.prepareStatement("select u_id,name,unm,desig,email,pm,dept from user");
			rs = stmt.executeQuery();
			while(rs.next())
			{
				String id = rs.getString(1);
				String name = rs.getString(2);
				String unm = rs.getString(3);
				String desig = rs.getString(4);
				String email = rs.getString(5);
				String pm = rs.getString(6);
				String dept = rs.getString(7);
				userList.add(new UserStatus(new User(id, name, unm, desig, email, pm, dept),false));
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
    }
    
    public static void getDeveloper(){
    	PreparedStatement stmt;
		Connection conn;
		ResultSet rs;
		try
		{
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection("jdbc:mysql://localhost/BTS?user=root&password=password");
			stmt = conn.prepareStatement("select u_id,name from user where desig='Developer'");
			rs = stmt.executeQuery();
			userList.clear();
			while(rs.next()){
				String u_id = rs.getString(1);
				String name = rs.getString(2);
				userList.add(new UserStatus(new User(u_id,name),false));
			}
			conn.close();
		}catch(Exception e){}
		
    }
    
    public static List<UserStatus> getAllDevelopers(){
    	getDeveloper();
    	return new ArrayList<UserStatus>(userList);
    }
    
    public static void getTester(){
    	PreparedStatement stmt;
		Connection conn;
		ResultSet rs;
		try
		{
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection("jdbc:mysql://localhost/BTS?user=root&password=password");
			stmt = conn.prepareStatement("select u_id,name from user where desig='Tester'");
			rs = stmt.executeQuery();
			userList.clear();
			while(rs.next()){
				String u_id = rs.getString(1);
				String name = rs.getString(2);
				userList.add(new UserStatus(new User(u_id,name),false));
			}
			conn.close();
		}catch(Exception e){}
		
    }
    
    public static List<UserStatus> getAllTesters(){
    	getTester();
    	return new ArrayList<UserStatus>(userList);
    }
    
    public static List<UserStatus> getAllUsers() {
    	initData();
        return new ArrayList<UserStatus>(userList);
    }
    public static User[] getAllFoodsArray() {
        return userList.toArray(new User[userList.size()]);
    }
 
    // This Method only used in "Data Filter" Demo
    public static List<UserStatus> getFilterUsers(User userFilter) {
        List<UserStatus> someusers = new ArrayList<UserStatus>();
        String name = userFilter.getName().toLowerCase();
        String unm = userFilter.getUsername().toLowerCase();
        String dept = userFilter.getDept().toLowerCase();
         
        for (Iterator<UserStatus> i = userList.iterator(); i.hasNext();) {
        	UserStatus tmp = i.next();
            if (tmp.getUser().getName().toLowerCase().contains(name) &&
                tmp.getUser().getUsername().toLowerCase().contains(unm)  &&
                tmp.getUser().getDept().toLowerCase().contains(dept)) {
                someusers.add(tmp);
            }
        }
        return someusers;
    }
}