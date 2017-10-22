package main.java.laogu;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import javax.websocket.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import javax.websocket.server.ServerEndpoint;


/**
 * Created by laogu on 2017/3/3.
 */
@ServerEndpoint("/testWebsocket")
public class connHiveWebsoket{

    private static String START_TIME = "Start Time";
    private Session session;
    private static  ObjectMapper mapper = new ObjectMapper();
    private  clsTable dtCC=null;

    private  String jsonStringDataColname;
    private String jsonStringDataTable;
    private String jsonStringData;

    private  int countData=0;


    public void getData(){
        try
        {
            jsonStringDataColname=connHiveWebsoket.mapper.writeValueAsString(mesData.datatable.colName);
            jsonStringDataTable=connHiveWebsoket.mapper.writeValueAsString(mesData.datatable.tableData);

            this.countData=mesData.datatable.tableData.length;
            //System.out.println("DataTable JsonData:"+this.countData+"-----" + jsonStringDataTable.substring(1));

            //System.out.println("DataTable JsonData:" + mesData.datatable.colName[0].toString());

        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }


    @OnOpen
    public void whenOpening(Session session) {
        try {
            this.session = session;
            session.getUserProperties().put(START_TIME, System.currentTimeMillis());
            System.out.println("OnOpen websocket:" + System.currentTimeMillis());


            this.getData();

            timer.schedule(timertask,0,1000);



        }
        catch (Exception e){
            e.printStackTrace();
        }

    }
    final Timer timer = new Timer();
    TimerTask timertask=new TimerTask() {

        private int count=1;
        @Override
        public void run() {
            try {
                String data = connHiveWebsoket.mapper.writeValueAsString(mesData.datatable.tableData[count][1]);
                sendMessage(data);
                count++;
                if (count == 99) {
                    timer.cancel();

                }
            }
            catch (Exception e){
                timer.cancel();
                e.printStackTrace();
            }
        }
    };


    @OnMessage
    public void whenGettingAMessage(String message) {
        if (message.indexOf("xxx") != -1) {
            throw new IllegalArgumentException("xxx not allowed !");
        } else if (message.indexOf("close") != -1) {
            try {
                this.sendMessage("1:Server closing after " + this.getConnectionSeconds() + " s");
                session.close();
            } catch (IOException ioe) {
                System.out.println("Error closing session " + ioe.getMessage());
            }
            return;
        }
        System.out.println("Just processed a received message " + message);
        this.sendMessage("3:Just processed a received message"+message);
        this.sendData();
    }

    @OnError
    public void whenSomethingGoesWrong(Throwable t) {
        this.sendMessage("2:Error: " + t.getMessage());
    }

    @OnClose
    public void whenClosing() {
        System.out.println("Goodbye !");
    }

    void sendMessage(String message) {
        try {
            session.getBasicRemote().sendText(message);
        } catch (Throwable ioe) {
            System.out.println("Error sending message " + ioe.getMessage());
        }
    }

    void sendDataTimer(int i) {
        try {

            session.getBasicRemote()
                    .sendText(this.jsonStringDataTable.substring(i));

        } catch (Throwable ioe) {
            System.out.println("Error sending message " + ioe.getMessage());
        }
    }

    void sendData() {
        try {
            session.getBasicRemote()
                    .sendText(this.jsonStringDataColname);
            session.getBasicRemote()
                    .sendText(this.jsonStringDataTable);

        } catch (Throwable ioe) {
            System.out.println("Error sending message " + ioe.getMessage());
        }
    }

    int getConnectionSeconds() {
        long millis =  System.currentTimeMillis() -
                ((Long) this.session.getUserProperties().get(START_TIME));
        return (int) millis / 1000;
    }


}
