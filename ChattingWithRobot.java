package com.github7;
import com.baidu.aip.speech.AipSpeech;
import com.baidu.aip.speech.TtsResponse;
import com.baidu.aip.util.Util;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;


public class ChattingWithRobot {
    String APP_ID = "16710735"; //百度智能云API
    String API_KEY = "B1xMCE1LpwZE7iECzANtFkaO";
    String SECRET_KEY = "DYGGkAooi75KKXrGeBYjGzGvmAgRPTGe";
    String APIKEY = "8c8cbd5c50054fe881fc7fe168d5df4c"; //图灵机器人API
    //聊天入口
    public  void chatting () throws InterruptedException, IOException {
        while (true) {
            ChattingWithRobot robot=new ChattingWithRobot();
            LinuxExectue exectue = new LinuxExectue();
            System.out.println("-----------------------------------------------------------");
            System.out.print("请讲话（你可以说 ：查看内存/查看文件/关闭等）时间为5秒");
            exectue.execshellnoshow("arecord -t wav -c 1 -r 16000 -d 5 -f S16_LE /home/linrui7/tuling/input.wav  ");
            Thread.sleep(1000);
            System.out.print("4 ");
            Thread.sleep(1000);
            System.out.print("3 ");
            Thread.sleep(1000);
            System.out.print("2 ");
            Thread.sleep(1000);
            System.out.print("1 ");
            Thread.sleep(1000);
            System.out.print("0 ");
            System.out.println();
            //将刚才录音文件传入到百度云语言识别，获取语音识别后的JSON对象
            JSONObject jsonObject = robot.voicetostr();
            //判断刚才的录音文件是否有说话内容
            if (jsonObject.getLong("err_no") == 0) {
                //获取JSON对象中的第一个JSON数组对象，取出对象为“result”的值
                JSONArray jsonArray = jsonObject.getJSONArray("result");
                //String myword=jsonArray.getJSONObject(0).getString("result");
                String manstr = jsonArray.toString();
                String myword = manstr.substring(2, manstr.length() - 2);
                System.out.println("你说：" + myword);
                //判断我说的话是不是命令
                //保存如下命令
                HashMap<String, String> hashMap = new HashMap<String, String>();
                hashMap.put("文件", "ls  /home/linrui7/tuling/");
                hashMap.put("查看内存", "free");
                hashMap.put("查看硬盘", "df -h");
                hashMap.put("关闭防火墙", "systemctl stop firewalld");
                hashMap.put("打开防火墙", "systemctl start firewalld");
                hashMap.put("查看进程", "ps aux");
                if (myword.contains("退出")) {
                    System.exit(1);
                }
                boolean bo = false;
                for (String s : hashMap.keySet()) {
                    //如果我说的话中包含有Map中的词汇，则执行命令。
                    if (myword.contains(s)) {
                        String ex = hashMap.get(s);
                        exectue.execshell(ex);
                        bo = true;
                        break;
                    }
                }
                if (bo == false) {
                    //保存你说的话
                    String mansay = manstr.substring(2, manstr.length() - 2);
                    //连接机器人
                    StringBuffer stringBuffer = robot.robotstr(mansay);
                    //返回机器人的对话
                    String[] strings = stringBuffer.toString().split(":");
                    String robotstr = strings[2].replace("\"", "").replace("}", "");
                    System.out.println("说：" + robotstr );
                    //将机器人的文字转换成语音并播放
                    robotstrToWav(robotstr);
                    exectue.execshellnoshow("cvlc --play-and-exit /home/linrui7/tuling/output.wav ");
                    System.out.println("-----------------------------------------------------------\n\n");
                }
                System.out.println("4s后进行下一次对话");
                Thread.sleep(1000);
                System.out.print("3 ");
                Thread.sleep(1000);
                System.out.print("2 ");
                Thread.sleep(1000);
                System.out.print("1 ");
                Thread.sleep(1000);
                System.out.print("0 ");
            } else {
                System.out.println("并没有监测到你说话");
                String string = "并没有监测到你说话，请重新对话";
                robotstrToWav(string);
            }
        }
    }
    //识别本地语音文件,返回JSON对象
    public JSONObject voicetostr() {
        AipSpeech client = new AipSpeech(APP_ID, API_KEY, SECRET_KEY);
        JSONObject res = client.asr("/home/linrui7/tuling/input.wav", "wav", 16000, null);
        return res;
    }

    //连接图灵机器人，获取机器人的话
    public StringBuffer robotstr(String js) throws IOException {
        String question = js;
        String INFO = URLEncoder.encode(question, "utf-8");
        String getURL = "http://www.tuling123.com/openapi/api?key=" + APIKEY + "&info=" + INFO;
        URL getUrl = new URL(getURL);
        HttpURLConnection connection = (HttpURLConnection) getUrl.openConnection();
        connection.connect();
        // 取得输入流，并使用Reader读取
        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), "utf-8"));
        StringBuffer stringBuffer = new StringBuffer();
        String line = "";
        while ((line = reader.readLine()) != null) {
            stringBuffer.append(line);
        }
        reader.close();
        // 断开连接
        connection.disconnect();
        return stringBuffer;
    }
    //将机器人返回的话转换成语音
    public void robotstrToWav(String str) {
        AipSpeech client = new AipSpeech(APP_ID, API_KEY, SECRET_KEY);
        HashMap<String, Object> options = new HashMap<String, Object>();
        options.put("spd", "5");
        options.put("pit", "5");
        options.put("per", "4");
        TtsResponse res = client.synthesis(str, "zh", 1, options);
        byte[] data = res.getData();
        JSONObject res1 = res.getResult();
        if (data != null) {
            try {
                Util.writeBytesToFileSystem(data, "/home/linrui7/tuling/output.wav");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (res1 != null) {
            System.out.println(res1.toString(2));
        }
    }
}
