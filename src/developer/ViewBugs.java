package developer;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.zkoss.zk.ui.Sessions;

import tester.Bug;
import tester.BugStatus;


public class ViewBugs {

	private static List<Bug> bugList = new ArrayList<Bug>();
	public static void viewBugs(){
		if(!bugList.isEmpty())
			bugList.clear();
		String priority,summary,description,pname,status,version;
		int p_id,d_id,b_id,t_id;
		byte[] img;
		Date createDate,updateDate,closeDate;
		Connection conn;
		PreparedStatement stmt1;
		Statement stmt2,stmt,stmt3;
		ResultSet rs,rs1,rs2,rs3;
		try{
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection("jdbc:mysql://localhost/BTS?user=root&password=password");
			String user = String.valueOf(Sessions.getCurrent().getAttribute("user"));
			stmt = conn.createStatement();
			rs = stmt.executeQuery("select u_id from user where name='"+user+"'");
			while(rs.next()){
				int id = rs.getInt(1);
				stmt1 = conn.prepareStatement("select p_id,priority,summary,description,d_id,b_id,status,img,t_id,version from bug where d_id="+id);
				rs1 = stmt1.executeQuery();
				while(rs1.next()){
					p_id = rs1.getInt(1);
					priority = rs1.getString(2);
					summary = rs1.getString(3);
					description = rs1.getString(4);
					d_id = rs1.getInt(5);
					b_id = rs1.getInt(6);
					status = rs1.getString(7);
					img = rs1.getBytes(8);
					t_id = rs1.getInt(9);
					version = rs1.getString(10);
					stmt2 = conn.createStatement();
					rs2 = stmt2.executeQuery("select name from project where p_id="+p_id);
					while(rs2.next()){
						pname = rs2.getString(1);
						stmt3 = conn.createStatement();
						rs3 = stmt3.executeQuery("select create_date,update_date,close_date from bug_log where b_id="+b_id);
						while(rs3.next()){
							createDate = rs3.getDate(1);
							updateDate = rs3.getDate(2);
							closeDate = rs3.getDate(3);
							bugList.add(new Bug(b_id,p_id,t_id,pname,priority,summary,description,version,d_id,status,img,createDate,updateDate,closeDate));
						}
					}
				}
			}
		}catch(Exception e){}
	}
	
	public static List<Bug> getAllBugs(){
		viewBugs();
		return new ArrayList<Bug>(bugList);
	}
	
	public static List<BugStatus> getFilterBug(ViewFilter bugFilter)
	{
		List<BugStatus> someBugs = new ArrayList<BugStatus>();
		String priority = bugFilter.getPriority().toLowerCase();
		String pname = bugFilter.getPname().toLowerCase();
		
		for (Iterator<Bug> i = bugList.iterator(); i.hasNext();) {
            Bug tmp = i.next();
            if (tmp.getPname().toLowerCase().contains(pname) &&
                tmp.getPriority().toLowerCase().contains(priority)) {
                someBugs.add(new BugStatus(false, tmp));
            }
        }
        return someBugs;
	}
}
