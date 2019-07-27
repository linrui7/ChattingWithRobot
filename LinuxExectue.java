package com.github7;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

public class LinuxExectue {
    //创建执行Linux命令的类，并且打印输出后的结果
    public String  execshell(String str) {
        String cmd = str;
        String strings="";
        try {
            Process ps = Runtime.getRuntime().exec(cmd);
            strings = loadStream(ps.getInputStream());
            System.out.print(strings);
            System.err.print(loadStream(ps.getErrorStream()));
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
        return strings;
    }
    //判断文件是否存在

    public String  execshelllocation(String str) {
        String cmd = str;
        String strings="";
        try {
            Process ps = Runtime.getRuntime().exec(cmd);
            strings = loadStream(ps.getInputStream());
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
        return strings;
    }
    //创建执行Linux命令的类，只执行命令。
    public void execshellnoshow(String str) {
        String cmd = str;
        try {
            Process ps = Runtime.getRuntime().exec(cmd);
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    //打印结果
    public String loadStream(InputStream in) throws IOException {
        int ptr = 0;
        in = new BufferedInputStream(in);
        StringBuffer buffer = new StringBuffer();
        while ((ptr = in.read()) != -1) {
            buffer.append((char) ptr);
        }
        return buffer.toString();
    }
}
