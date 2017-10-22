package main.java.laogu;

/**
 * Created by laogu on 2017/4/20.
 */
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
import java.util.List;
import java.util.Map;
import net.sf.json.JSONArray;

import net.sf.json.JSONObject;

public class testEasyUI extends HttpServlet{

    private  String jsonStringDataColname;
    private String jsonStringDataTable;


    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String errorMsg="";

        try {

            ServletConfig servCong=getServletConfig();


            String sqlString=request.getParameter("sqlStr");
            String testInt=request.getParameter("testInt");
            String testDouble=request.getParameter("testDouble");
            String testDate=request.getParameter("testDate");

            int code=Integer.parseInt(testInt);
            double dou=Double.parseDouble(testDouble);


            //System.out.print(String.valueOf(code)+"---"+String.valueOf(dou)+"---"+testDate);


            JSONObject jsonDataGrid = userDao.selectJsonDataGrid(getDataServlet.stmt,sqlString);


            //clsEasyDataGrid jsongrid=new clsEasyDataGrid();
//            int colnum=dataMap.get(0).length;
//            int rownum=dataMap.size()-1;
//            String [][] tabledata=new String[rownum][colnum];
//
//            for(int i=1;i<dataMap.size();i++)
//            {
//                tabledata[i-1]=dataMap.get(i);
//            }

//            jsongrid.setTotals(rownum);
//            jsongrid.setRows(listData);


            System.out.print(jsonDataGrid.toString());

//            Map<Integer,String[]> dataMap=userDao.selectMapData(getDataServlet.stmt,sqlString);
//
//            String jsonStringTableMap=testAjaxServlet.mapper.writeValueAsString(dataMap);


//            jsonStringDataColname=testAjaxServlet.mapper.writeValueAsString(resTable.colName);
//            jsonStringDataTable=testAjaxServlet.mapper.writeValueAsString(resTable.tableData);




            response.setContentType("application/json;charset=UTF-8");
            response.setCharacterEncoding("UTF-8");

            //response.getWriter().println(jsonStringDataColname);
            response.getWriter().write(jsonDataGrid.toString());
            //response.getWriter().write(jsonStringDataTable);
            response.flushBuffer();







        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
