package main.java.laogu;

import javax.validation.constraints.Null;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;
import java.util.List;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class userDao {


//	private static String driverName = "org.apache.hive.jdbc.HiveDriver";
//	private static String url = "jdbc:hive2://10.0.83.21:10000/default";
//	private static String user = "hive";
//	private static String password = "";

//	private static String driverName = "com.mysql.jdbc.Driver";
//    private static String url = "jdbc:mysql://localhost:3306/test";
//    private static String user = "root";
//    private static String password = "";

	
	
    private static ResultSet res; 
    
	
	  public static Object[][] resultSetToObjectArray(ResultSet rs) {  
		     Object[][] data = null;  
		     try {     
		         rs.last();  
		         int rows = rs.getRow();  
		         data = new Object[rows][];    
		         ResultSetMetaData md = rs.getMetaData();//��ȡ��¼����Ԫ����  
		         int columnCount = md.getColumnCount();//����  
		         rs.first();  
		         int k = 0;  
		         while(rs.next()) {  
		             //System.out.println("i"+k);  
		             Object[] row = new Object[columnCount];  
		             for(int i=0; i<columnCount; i++) {  
		                 row[i] = rs.getObject(i+1).toString();  
		             }  
		             data[k] = row;  
		             k++;  
		         }  
		     } catch (Exception e) {  
		     }  
		     return data;  
		 }  
	  

	  
	  public static String[] getColName(ResultSet rs){
		String[] colName = null;
		try {

			ResultSetMetaData md = rs.getMetaData();// ��ȡ��¼����Ԫ����
			int columnCount = md.getColumnCount();// ����
			colName = new String[columnCount];
			//rs.first();
			int k = 0;
			rs.next();
			for (int i = 0; i < columnCount; i++) {
				colName[i] = rs.getString(i + 1);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return colName;
	  }
	  
	
	  
	   
	   public static  Connection getConn(String driverName,String url,String user,String password) throws ClassNotFoundException, SQLException {  
		   Connection conn=null;
		   try{
		   Class.forName(driverName);  
		   conn = DriverManager.getConnection(url, user, password);  
		   }
		   catch(SQLException e){
			   e.printStackTrace();
		   }
		   catch(ClassNotFoundException e){
			   e.printStackTrace();
		   }
		   catch(Exception e){
			   e.printStackTrace();
		   }
		   
		   return conn;
		    
	   	}
	   
	   private static String countData(Statement stmt, String tableName)  
	            throws SQLException {  
	        String sql = "select count(*) from " + tableName;  
	        System.out.println("Running:" + sql);  
	        res = stmt.executeQuery(sql);  
	        String results="";
	        
//	        System.out.println("ִ�С�regular hive query�����н��:");  
	        while (res.next()) {  
	        	results= res.getString(1); 
	        	break;
	        }  
	        return results;
	    }

	public static JSONObject selectJsonDataGrid(Statement stmt,String sql)

	{
		JSONObject jsonResult = new JSONObject();
		try {
			// json数组
			JSONArray jsonArray = new JSONArray();
			res = stmt.executeQuery(sql);
			System.out.println("Running:" + sql);
			// 获取列数
			ResultSetMetaData md = res.getMetaData();//
			int columnCount = md.getColumnCount();
			String columnName="";
			Integer k=0;

			// 遍历ResultSet中的每条数据
			while (res.next()) {
				JSONObject jsonObj = new JSONObject();

				// 遍历每一列
				for (int i = 1; i <= columnCount; i++) {
					columnName =md.getColumnName(i);
					//System.out.print(columnName);
					String value = res.getString(columnName);
					jsonObj.put(columnName, value);
				}
				jsonArray.add(jsonObj);
				k++;
			}
			jsonResult.put("total", k);
			jsonResult.put("rows", jsonArray);
		}
		catch (Exception e){
			e.printStackTrace();
		}

		return jsonResult;

	}

	public static Map<Integer,String[]> selectMapData(Statement stmt,String sql) throws SQLException{

		Map<Integer,String[]> dataMap = new HashMap<Integer, String[]>();
		try {


			res = stmt.executeQuery(sql);
			System.out.println("Running:" + sql);


			ResultSetMetaData md = res.getMetaData();//
			int columnCount = md.getColumnCount();//

			String[] colName=new String[columnCount];
			for(int i=0; i<columnCount; i++) {
				colName[i]=md.getColumnName(i+1);
				//System.out.print(colName[i]);
			}
			dataMap.put(0, colName);
			Integer k=1;
			while(res.next()) {
				//System.out.println("i"+k);
				String[] row = new String[columnCount];
				for(int i=0; i<columnCount; i++) {
					row[i] = res.getObject(i+1).toString();

				}

				dataMap.put(k, row);
				k++;
			}



		} catch (Exception e) {
			e.printStackTrace();
		}

		return dataMap;

	}

	public static List<Map<String, Object>> selectListData(Statement stmt,String sql) throws SQLException{

		List<Map<String, Object>> listdata = new ArrayList<Map<String, Object>>();
		try {


			res = stmt.executeQuery(sql);



			ResultSetMetaData md = res.getMetaData();//��ȡ��¼����Ԫ����
			int columnCount = md.getColumnCount();//����

			String[] colName=new String[columnCount];
			for(int i=0; i<columnCount; i++) {
				colName[i]=md.getColumnName(i+1);
			}

			Integer k=1;
			while(res.next()) {
				Map<String,Object> rowdata=new HashMap<String, Object>();
				String rowvalue ="";
				for(int i=0; i<columnCount; i++) {
					rowvalue = res.getObject(i+1).toString();
					rowdata.put(colName[i],rowvalue);
				}

				listdata.add(rowdata);
				k++;
			}



		} catch (Exception e) {
			e.printStackTrace();
		}

		return listdata;

	}
	   public static clsTable selectTableData(Statement stmt,String sql) throws SQLException{
		   clsTable myTable=new clsTable();
		   Map<String,String[]> data = new HashMap<String,String[]>();  
		     try {     
		         
		    	 System.out.println("Running:" + sql);  
			        res = stmt.executeQuery(sql);  

			        
		         
		         ResultSetMetaData md = res.getMetaData();//��ȡ��¼����Ԫ����  
		         int columnCount = md.getColumnCount();//����  
		         myTable.colName=new String[columnCount];
		         Integer k=0;
		         while(res.next()) {  
		             //System.out.println("i"+k);  
		             String[] row = new String[columnCount];  
		             for(int i=0; i<columnCount; i++) {
						 row[i] = res.getObject(i+1).toString();
						 if (k==0) {
							 myTable.colName[i]=md.getColumnName(i+1);
						 }
					 }

					 data.put(k.toString(), row);
		             k++;  
		         }
		         
		         int rowCount=data.size();
		         myTable.tableData=new String[rowCount][columnCount];
		         
		         for (int i=0;i<rowCount;i++){
		        	 Integer li=new Integer(i);
		        	 myTable.tableData[i] = data.get(li.toString()); 
		         }
		         
		     } catch (Exception e) {  
		     }  
	        
	        return myTable;
		   
	   }

	public static Map<String,String[]> tableToJsonMap(clsTable table) {
		Map<String,String[]> dataMap = new HashMap<String,String[]>();

		try {

			int columnCount = table.colName.length;//列数
			int rowCount=table.tableData.length; //行数

			for(int i=0;i<columnCount;i++){
				String[] tmp=new String[rowCount];
				for(int j=0;j<rowCount;j++){
					tmp[j]=table.tableData[j][i];
				}
				dataMap.put(table.colName[i],tmp);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return dataMap;
	}

	    public static clsTable selectData(Statement stmt, String sql)  
	            throws SQLException {  
	    	
	    	clsTable myTable=new clsTable();
	    	
	    	
//	    	String[][] tableData=null;
//	    	String[] colName=null;
//	    	DefaultTableModel dataModel=null;
	        
	        try {     
	        	
	        	System.out.println("Running:" + sql);  
		        res = stmt.executeQuery(sql);  
		        System.out.println("ִ��"+sql+" ���н��:");  
		        
		        
		        
	        	 res.last();  
		         int rowCount = res.getRow();  
		          
		         ResultSetMetaData md = res.getMetaData();//��ȡ��¼����Ԫ����  
		         int columnCount = md.getColumnCount();//����  
		         
		         
		         myTable.tableData=new String[rowCount][columnCount];
		         myTable.colName=new String[columnCount];
		         
		         for(int i=0;i<columnCount;i++){
		        	 myTable.colName[i]=md.getColumnName(i+1);
		         }
		         
		         res.beforeFirst();  
		         int k = 0;  
		         while(res.next()) {  
		             //System.out.println("i"+k);  
		             String[] row = new String[columnCount];  
		             for(int i=0; i<columnCount; i++) {  
		                 row[i] = res.getObject(i+1).toString();  
		             }  
		             myTable.tableData[k] = row;  
		             k++;  
		         }
		         
//		         dataModel=new DefaultTableModel(tableData,colName);
		     } catch (Exception e) {  
		    	 e.printStackTrace();
		     }  
	        
	        return myTable;
	         
	    }  
	  
	   
	  
	   
}
