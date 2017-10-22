<%@ page language="java" contentType="text/html;
charset=UTF-8"
         pageEncoding="UTF-8"%>
<html>
<body>
<h1 style="text-align: center;">Lifecycle Lights</h1>

<table style="text-align: left; width: 50px; margin-left: auto;
        margin-right: auto;" border="0" cellpadding="20" cellspacing="0">
    <tbody>
    <tr>
        <td style=" text-align: center; vertical-align: top;">
            <div style="text-align: center;">
                <form action="">
                    <input onclick="open_connection()" value="Open Connection" type="button" id="ocID">
                    <input onclick="send_valid_message()" value="Send Message" type="button" id="svmID">
                    <input onclick="send_invalid_message()" value="Send Bad Message" type="button" id="simID">
                    <input onclick="request_close_connection()" value="Server Close Connection" type="button"  id="rccID">
                    <input onclick="close_connection()" value="Client Close Connection" type="button" id="rcID">
                    <input type="text" size="80" id="msg" name="msg" placeholder="输入聊天内容"/>
                </form>
            </div>
        </td>
    </tr>
    <tr>
        <td style=" text-align: center; vertical-align: top;">
            <canvas id="myDrawing" width="200" height="210"></canvas>
            <div id="traffic_light_display"></div>
        </td>
    </tr>
    </tbody>
</table>
<div id="output" style="width:1000px;height:100px;background-color:#4dff1f;"></div>
</body>

<script language="javascript" type="text/javascript">
    var lifecycle_websocket;
    var datamessage;
    function init() {
        output = document.getElementById("output");
        traffic_light_display = document.getElementById("traffic_light_display");
        update_display("1", "No connection");
        update_buttons();
    }

    function open_connection() {
        lifecycle_websocket = new WebSocket("ws://localhost:8080/connhive/testWebsocket");
        lifecycle_websocket.onmessage = function (evt) {

            update_for_message(evt.data);
            update_buttons();
            datamessage = JSON.parse(evt.data);
            display_data(datamessage);
        };
        lifecycle_websocket.onclose = function (evt) {
            update_buttons();
        };
    }

    function display_data(display_message) {
        var show = document.getElementById('output');
        // 接收、并显示消息
        show.innerHTML += display_message + "<br/>";
        show.scrollTop = show.scrollHeight;

    }


    function get_color(light_index, light_on_index) {
        if (light_index == 1 && light_on_index == 1) {
            return "red"
        } else if (light_index == 2 && light_on_index == 2) {
            return "yellow"
        } else if (light_index == 3 && light_on_index == 3) {
            return "green"
        } else {
            return "grey"
        }
    }

    function get_light_index(message) {
        return message.substring(0, 1)
    }

    function get_display_message(message) {
        return message.substring(2, message.length)
    }

    function update_for_message(message) {
        //var display_message = get_display_message(message);
        var display_message=message;
        var light_index = get_light_index(message);
        update_display(light_index, display_message);
    }

    function update_display(light_index, display_message) {
        var old = traffic_light_display.firstChild;
        var pre = document.createElement("pre");
        pre.style.wordWrap = "break-word";
        pre.innerHTML = "<b><font face='Arial'>"+display_message+"</font></b>";
        if (traffic_light_display.firstChild != null) {
            traffic_light_display.replaceChild(pre, traffic_light_display.firstChild);
        } else {
            traffic_light_display.appendChild(pre)
        }

        var context = document.getElementById('myDrawing').getContext('2d');
        context.beginPath();
        context.fillStyle = "black"
        context.fillRect(65,0,70,210);
        context.fill();

        context.beginPath();
        context.fillStyle = get_color(1, light_index); // grey
        context.arc(100,35,25,0,(2*Math.PI), false)
        context.fill();

        context.beginPath();
        context.fillStyle = get_color(2, light_index);
        context.arc(100,105,25,0,(2*Math.PI), false)
        context.fill();

        context.beginPath();
        context.fillStyle = get_color(3, light_index);
        context.arc(100,175,25,0,(2*Math.PI), false)
        context.fill();


    }

    function isOpen() {
        return lifecycle_websocket != null && lifecycle_websocket.readyState == lifecycle_websocket.OPEN;
    }

    function update_buttons() {
        ocID.disabled = isOpen();
        simID.disabled = !isOpen();
        svmID.disabled = !isOpen();
        rccID.disabled = !isOpen();
        rcID.disabled = !isOpen();
    }

    function send_valid_message() {
        var mes=document.getElementById('msg').value;
        lifecycle_websocket.send(mes)
    }

    function send_invalid_message() {
        lifecycle_websocket.send("Helxxxlo")

    }

    function request_close_connection() {
        lifecycle_websocket.send("close");
    }

    function close_connection() {
        lifecycle_websocket.close();
        update_display("1", "Client closed connection");
    }

    window.addEventListener("load", init, false);
</script>

</html>
