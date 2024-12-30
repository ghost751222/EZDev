import java.sql.*;

public class ConnectionTest {
	public static void main(String [] args)
	 {
	  String driverName="com.microsoft.sqlserver.jdbc.SQLServerDriver";
	  String dbURL="jdbc:sqlserver://127.0.0.1:1433;DatabaseName=uniagent1996;encrypt=false";  // school为数据库名，此处填写你的数据库名

	  String userName="sa";   // 填写你的登录账户名名，我的是sa
	  String userPwd="1wax@QSZz";   // 填写你的密码

	  try
	  {
	   Class.forName(driverName);
	   Connection dbConn=DriverManager.getConnection(dbURL,userName,userPwd);
	    System.out.println("连接数据库成功");
	  }
	  catch(Exception e)
	  {
	   e.printStackTrace();
	   System.out.print("连接失败");
	  }
	 }
}
