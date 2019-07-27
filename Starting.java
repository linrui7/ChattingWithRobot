package com.github7;

import com.baidu.aip.speech.AipSpeech;
import com.baidu.aip.speech.TtsResponse;
import com.baidu.aip.util.Util;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.IOException;
import java.util.HashMap;

public class Starting {
    String APP_ID = "16710735";
    String API_KEY = "B1xMCE1LpwZE7iECzANtFkaO";
    String SECRET_KEY = "DYGGkAooi75KKXrGeBYjGzGvmAgRPTGe";
    String APIKEY = "8c8cbd5c50054fe881fc7fe168d5df4c";
    public static void main(String[] args) throws InterruptedException, IOException {
        System.setProperty("aip.log4j.conf", "/home/linrui7/tuling/tulingdemo/src/main/resources/log4j.properties");
        Starting selectFunction = new Starting();
        LinuxExectue exectue = new LinuxExectue();
        String str = "请选择要执行的功能 比如说：聊天系统";
        selectFunction.robotstrToWav(str);
        exectue.execshellnoshow("cvlc --play-and-exit /home/linrui7/tuling/changeoutput.wav ");
        Thread.sleep(10000);
        System.out.println(str);
        System.out.println("----->功能一：聊天系统");
        System.out.println("----->功能二：文字识别");
        System.out.println("----->功能三：手势识别\n");
        while (true) {
            //录音
            System.out.println("。。。。。Please Speaking。。。。。");
            exectue.execshellnoshow("arecord -t wav -c 1 -r 16000 -d 5 -f S16_LE /home/linrui7/tuling/change.wav  ");
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
            //语音识别
            JSONObject jsonObject = selectFunction.voicetostr();
            if (jsonObject.getLong("err_no") == 0) {
                //获取JSON对象中的第一个JSON数组对象，取出值
                JSONArray jsonArray = jsonObject.getJSONArray("result");
                String manstr = jsonArray.toString();
                String myword = manstr.substring(2, manstr.length() - 2);
                if (myword.contains("聊天系统") || myword.contains("功能一")) {
                    System.out.println("你选择的是：聊天系统");
                    ChattingWithRobot chattingWithRobot = new ChattingWithRobot();
                    chattingWithRobot.chatting();
                } else if (myword.contains("文字识别") || myword.contains("功能二")) {
                    System.out.println("你选择的是：文字识别");
                    CharacterRecognition characterRecognition = new CharacterRecognition();
                    characterRecognition.ocr();
                } else if (myword.contains("手势识别") || myword.contains("功能三")) {
                    System.out.println("你选择的是：手势识别");
                    GestureRecognition gestureRecognition = new GestureRecognition();
                    gestureRecognition.gesturemeth();
                } else if (myword.contains("退出") || myword.contains("不想玩了")) {
                    System.exit(1);
                } else {
                    String string = "你的选择错误,请重新选择以下选项";
                    System.out.println(string);
                    System.out.println("----->功能一：聊天系统");
                    System.out.println("----->功能二：文字识别");
                    System.out.println("----->功能三：手势识别\n");
                    selectFunction.robotstrToWav(string);
                    exectue.execshellnoshow("cvlc --play-and-exit /home/linrui7/tuling/changeoutput.wav ");
                    Thread.sleep(4000);
                }
            } else {
                System.out.println("机器人说：你并没有说话哦\n");
                String string = "你并没有讲话哦";
                selectFunction.robotstrToWav(string);
                exectue.execshellnoshow("cvlc --play-and-exit /home/linrui7/tuling/changeoutput.wav ");
                Thread.sleep(2000);
            }
        }
    }

    //语音转文字
    public JSONObject voicetostr() {
        AipSpeech client = new AipSpeech(APP_ID, API_KEY, SECRET_KEY);
        JSONObject res = client.asr("/home/linrui7/tuling/change.wav", "wav", 16000, null);
        return res;
    }

    //文字转语音
    public void robotstrToWav(String str) {
        AipSpeech client = new AipSpeech(APP_ID, API_KEY, SECRET_KEY);
        HashMap<String, Object> options = new HashMap<String, Object>();
        /*
        spd	选填	语速，取值0-15，默认为5中语速
        pit	选填	音调，取值0-15，默认为5中语调
        vol	选填	音量，取值0-15，默认为5中音量
        per（基础音库）	选填	度小宇=1，度小美=0，度逍遥=3，度丫丫=4
         */
        options.put("spd", "5");
        options.put("pit", "5");
        options.put("per", "4");
        TtsResponse res = client.synthesis(str, "zh", 1, options);
        byte[] data = res.getData();
        JSONObject res1 = res.getResult();
        if (data != null) {
            try {
                Util.writeBytesToFileSystem(data, "/home/linrui7/tuling/changeoutput.wav");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (res1 != null) {
            System.out.println(res1.toString(2));
        }
    }
}
