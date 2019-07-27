package com.github7;
import com.baidu.aip.ocr.AipOcr;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Scanner;

public class CharacterRecognition {
    String APP_ID = "16727054";
    String API_KEY = "UmiPujVa32oa2C3hbg6w5ATj";
    String SECRET_KEY = "8Zb8u2QVm0QFm0v8k99mL4GNQCHyv93a";
    HashMap<String, String> options = new HashMap<String, String>();
    AipOcr client = new AipOcr(APP_ID, API_KEY, SECRET_KEY);

    public void ocr() {
        while (true) {
            System.out.println("---请选择---");
            System.out.println("输入1文字识别");
            System.out.println("输入0退出程序");
            Scanner scanner1 = new Scanner(System.in);
            String str=scanner1.next();
            if(str.equals("1")||str.equals("0")) {
                int in = Integer.parseInt(str);
                switch (in) {
                    case 1:
                        character();
                        break;
                    case 0:
                        System.exit(1);
                        break;
                    default:
                        System.out.println("输入错误,重新选择");
                        ocr();
                }
            }else {
                System.out.println("输入错误,重新选择");
                ocr();
            }
        }
    }

    //运行方法
    public void character() {
        System.out.println("请输入照片名称 比如：aaa");
        System.out.print("当前默认识别路径为/home/linrui7/tuling/picture/,有以下图片，请选择\n");
        LinuxExectue linuxExectue = new LinuxExectue();
        linuxExectue.execshell("ls /home/linrui7/tuling/picture/");
        Scanner scanner = new Scanner(System.in);
        String input = scanner.next();
        JSONObject jsonObject = getJson(input);
        //判断如果是空路径的情况下，有没有结果
        if (jsonObject.isNull("result")) {
            character();
        } else {
            JSONArray jsonArray = jsonObject.getJSONArray("words_result");
            for (int i = 0; i < jsonObject.getInt("words_result_num"); i++) {
                System.out.println(jsonArray.getJSONObject(i).getString("words").toString());
            }
            System.out.println("");
        }
    }

    //获取图片的JSon串
    public JSONObject getJson(String str) {
        options.put("recognize_granularity", "big");//big - 不定位单字符位置，small - 定位单字符位置
        options.put("language_type", "CHN_ENG");//语言类型
        options.put("detect_direction", "true");//检测图片朝向
        options.put("detect_language", "true");//检测图片语言
        options.put("vertexes_location", "true");//外接多边形顶点位置默认false
        options.put("probability", "false");//是否返回识别结果中每一行的置信度
        LinuxExectue linuxExectue=new LinuxExectue();
        String location = "/home/linrui7/tuling/picture/" + str + ".png";
        String locationCheck=linuxExectue.execshelllocation("ls "+location);
        if(locationCheck.equals("")||locationCheck.isEmpty()){
            System.out.println();
            System.out.println("对不起，图片不存在");
            character();
        }else {
            linuxExectue.execshellnoshow("eog " + location);
            JSONObject res = client.basicGeneral(location, options);
            //System.out.println(res.toString(2));
            return res;
        }
        return new JSONObject();

    }
}
