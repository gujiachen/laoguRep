package main.java.laogu;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.sql.Connection;
import java.sql.Statement;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;
/**
 * Created by laogu on 2017/4/4.
 */
public class testAjaxServlet extends HttpServlet {

    private  String jsonStringDataColname;
    private String jsonStringDataTable;
    private static  ObjectMapper mapper = new ObjectMapper();

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String errorMsg="";

        try {

            ServletConfig servCong=getServletConfig();
            String driverName= servCong.getInitParameter("driverName");
            String url=servCong.getInitParameter("url");
            String user=servCong.getInitParameter("user");
            String password=servCong.getInitParameter("password");


            Connection conn = userDao.getConn(driverName, url, user, password);

            String sqlString=request.getParameter("sqlStr");
            String testInt=request.getParameter("testInt");
            String testDouble=request.getParameter("testDouble");
            String testDate=request.getParameter("testDate");

            int code=Integer.parseInt(testInt);
            double dou=Double.parseDouble(testDouble);


            System.out.print(String.valueOf(code)+"---"+String.valueOf(dou)+"---"+testDate);

//			            stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
            Statement stmt = conn.createStatement();

//            clsTable resTable= userDao.selectTableData(stmt, sqlString);
//            Map<String,String[]> dataMap = userDao.tableToJsonMap(resTable);

            Map<Integer,String[]> dataMap=userDao.selectMapData(getDataServlet.stmt,sqlString);

            String jsonStringTableMap=testAjaxServlet.mapper.writeValueAsString(dataMap);

//            jsonStringDataColname=testAjaxServlet.mapper.writeValueAsString(resTable.colName);
//            jsonStringDataTable=testAjaxServlet.mapper.writeValueAsString(resTable.tableData);




            response.setContentType("application/json;charset=UTF-8");
            response.setCharacterEncoding("UTF-8");

            //response.getWriter().println(jsonStringDataColname);
            response.getWriter().write(jsonStringTableMap);
            //response.getWriter().write(jsonStringDataTable);
            response.flushBuffer();







        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
