package com.xh.onetwothreeupload.config;


import com.xh.onetwothreeupload.to.HostVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;

import java.io.IOException;
import java.io.InputStream;

/**
 * 通过FTP上传文件
 *
 * @Author lvhaibao
 * @Date 2018/2/11 21:43
 */
@Slf4j
public class FTPTools {
    /** 本地字符编码 */
    private static String LOCAL_CHARSET = "gb2312";

    //设置私有不能实例化
    private FTPTools() {

    }

    /**
     * 上传
     *
     * @param hostname
     * @param port
     * @param username
     * @param password
     * @param workingPath 服务器的工作目录
     * @param inputStream 文件的输入流
     * @param saveName    要保存的文件名
     * @param hostVo
     * @return
     */
    public static boolean upload(InputStream inputStream, String saveName, HostVo hostVo) {
        boolean flag = false;
        FTPClient ftpClient = new FTPClient();

        if (connect(ftpClient,hostVo.getHostname(),hostVo.getPort(),hostVo.getUsername(),hostVo.getPassword())) {
            try {
                //2 检查工作目录是否存在
                if (ftpClient.changeWorkingDirectory("/")) {

                    // 3 检查是否上传成功
                    if (storeFile(ftpClient, saveName, inputStream)) {
                        flag = true;
                        disconnect(ftpClient);
                    }
                }
            } catch (IOException e) {
                log.error("工作目录不存在");
                e.printStackTrace();
                disconnect(ftpClient);
            }
        }
        return flag;
    }

    /**
     * 断开连接
     *
     * @param ftpClient
     * @throws Exception
     */
    public static void disconnect(FTPClient ftpClient) {
        if (ftpClient.isConnected()) {
            try {
                ftpClient.disconnect();
//                log.error("已关闭连接");
            } catch (IOException e) {
                log.error("关闭连接错误");
                e.printStackTrace();
            }
        }
    }

    /**
     * 测试是否能连接
     *
     * @param ftpClient
     * @param hostname  ip或域名地址
     * @param port      端口
     * @param username  用户名
     * @param password  密码
     * @return 返回真则能连接
     */
    public static boolean connect(FTPClient ftpClient,String hostname,Integer port,String username,String password) {
        boolean flag = false;
        try {
            ftpClient.connect(hostname,port);


            if (ftpClient.login(username,password)) {
                if (FTPReply.isPositiveCompletion(ftpClient.sendCommand(
                        "OPTS UTF8", "ON"))) {// 开启服务器对UTF-8的支持，如果服务器支持就用UTF-8编码，否则就使用本地编码（GBK）.
                    LOCAL_CHARSET = "UTF-8";
                }
                ftpClient.setControlEncoding(LOCAL_CHARSET);
                ftpClient.enterLocalPassiveMode();// 设置被动模式
                ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);

                flag = true;

            } else {
                log.error(hostname+"连接ftp失败，可能用户名或密码错误");
                try {
                    disconnect(ftpClient);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            log.error(hostname+"连接失败，可能ip或端口错误");
            e.printStackTrace();
        }
        return flag;
    }

    /**
     * 上传文件
     *
     * @param ftpClient
     * @param saveName        全路径。如/home/public/a.txt
     * @param fileInputStream 输入的文件流
     * @return
     */
    public static boolean storeFile(FTPClient ftpClient, String saveName, InputStream fileInputStream) {
        boolean flag = false;
        try {
            if (ftpClient.storeFile(new String(saveName.getBytes("UTF-8"),"iso-8859-1"), fileInputStream)) {
                flag = true;
                log.error(saveName+"上传成功");
                disconnect(ftpClient);
            }else {
                log.error(saveName+"上传失败");
            }
        } catch (IOException e) {
            log.error(saveName+"上传失败");
            disconnect(ftpClient);
            e.printStackTrace();
        }
        return flag;
    }
}
