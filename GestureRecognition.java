package com.github7;

import com.baidu.aip.bodyanalysis.AipBodyAnalysis;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Scanner;

public class GestureRecognition {
    String APP_ID = "16727210";
    String API_KEY = "ReD6tYdTfDK0nGwDpxB6CfBV";
    String SECRET_KEY = "IwxWel76PxPZ01kjdDzvewrj77fCpiGQ";
    AipBodyAnalysis client = new AipBodyAnalysis(APP_ID, API_KEY, SECRET_KEY);
    HashMap<String, String> options = new HashMap<String, String>();

    public void gesturemeth() {
        while (true) {
            System.out.println("---请选择---");
            System.out.println("输入1手势识别");
            System.out.println("输入0退出程序");
            Scanner scanner1 = new Scanner(System.in);
            String str=scanner1.next();
            if(str.equals("1")||str.equals("0")) {
                int in = Integer.parseInt(str);

                switch (in) {
                    case 1:
                        ges();
                        break;
                    case 0:
                        System.exit(1);
                        break;
                    default:
                        System.out.println("输入错误,重新选择");
                        gesturemeth();
                }
            }else {
                System.out.println("输入错误,重新选择");
                gesturemeth();
            }
        }
    }

    public void ges() {
        System.out.println("请输入照片名称 比如：aaa");
        System.out.print("当前默认识别路径为/home/linrui7/tuling/picture/,有以下图片，请选择\n");
        LinuxExectue linuxExectue = new LinuxExectue();
        linuxExectue.execshell("ls /home/linrui7/tuling/picture/");
        JSONObject jsonObject = photoResult();
        if (jsonObject.isNull("result")) {
            ges();
        } else {
            JSONArray jsonArray = jsonObject.getJSONArray("result");

            String classname = jsonArray.getJSONObject(0).get("classname").toString();
            String name = action(classname);
            if (name.contains("未能匹配")) {
                System.out.println("不能识别的手势哦");
                System.out.println("请作出以下动作：拳头、OK、祈祷、作揖、作别、单手比心、点赞、\n、Diss、我爱你、掌心向上、双手比心（3种）、数字（9种）、Rock、竖中指。");
            } else {
                System.out.println("图片中的手势是：" + name + "\n");
            }
        }
    }

    //返回图像识别结果
    public JSONObject photoResult() {
        Scanner scanner = new Scanner(System.in);
        String input = scanner.next();
        String location = "/home/linrui7/tuling/picture/" + input + ".png";
        LinuxExectue linuxExectue = new LinuxExectue();
        String locationCheck = linuxExectue.execshelllocation("ls " + location);
        if (locationCheck.equals("") || locationCheck.isEmpty()) {
            System.out.println();
            System.out.println("对不起，图片不存在");
            ges();
        } else {
            JSONObject res = client.gesture(location, options);
            linuxExectue.execshellnoshow("eog " + location);
            return res;
        }
        return new JSONObject();
    }

    public String action(String classname) {
        String gestureName = "";
        if (classname.equals("Thumb_down")) {
            gestureName = "踩|拇指向下";
            return gestureName;
        } else if (classname.equals("Ok")) {
            gestureName = "OK";
            return gestureName;
        } else if (classname.equals("ILY")) {
            gestureName = "Rock";
            return gestureName;
        } else if (classname.equals("Heart_single")) {
            gestureName = "单手比心";
            return gestureName;
        } else if (classname.equals("Thumb_up")) {
            gestureName = "点赞|拇指向上";
            return gestureName;
        } else if (classname.equals("Prayer")) {
            gestureName = "祈祷";
            return gestureName;
        } else if (classname.equals("Fist")) {
            gestureName = "拳头";
            return gestureName;
        } else if (classname.equals("Point")) {
            gestureName = "点|食指";
            return gestureName;
        } else if (classname.equals("Heart_1") || classname.equals("Heart_2") || classname.equals("Heart_3")) {
            gestureName = "双手比心";
            return gestureName;
        } else if (classname.equals("Palm")) {
            gestureName = "掌心向前";
            return gestureName;
        } else if (classname.equals("Palm_up")) {
            gestureName = "掌心向上";
            return gestureName;
        } else if (classname.equals("Honour")) {
            gestureName = "作别|告别";
            return gestureName;
        } else if (classname.equals("Rock")) {
            gestureName = "摇滚";
            return gestureName;
        } else if (classname.equals("Congratulation")) {
            gestureName = "作揖|祝贺";
            return gestureName;
        } else {
            gestureName = "未能匹配的手势:" + classname;
            return gestureName;
        }
    }
}
