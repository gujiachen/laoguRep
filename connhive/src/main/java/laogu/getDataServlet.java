package main.java.laogu;

import javax.servlet.http.HttpServlet;
import java.sql.Connection;
import java.sql.Statement;
import java.io.IOException;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
/**
 * Created by laogu on 2017/3/17.
 */
public class getDataServlet extends HttpServlet {
    public   static String driverName;
    public   static  String url;
    public   static  String user;
    public   static  String password;
    public   static Connection conn;
    public   static Statement stmt;
    private  String sqlString;

        @Override
    public void init() throws ServletException
    {

        try
        {
            ServletConfig servCong = getServletConfig();
            getDataServlet.driverName = servCong.getInitParameter("driverName");
            getDataServlet.url = servCong.getInitParameter("url");
            getDataServlet.user = servCong.getInitParameter("user");
            getDataServlet.password = servCong.getInitParameter("password");

            this.sqlString = "select * from CC limit 100";
            getDataServlet.conn = userDao.getConn(driverName, url, user, password);

//			            stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
            getDataServlet.stmt = conn.createStatement();

            mesData.datatable = userDao.selectTableData(stmt, sqlString);



        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
}
