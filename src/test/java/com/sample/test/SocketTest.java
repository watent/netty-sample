package com.sample.test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Socket Test
 *
 * @author JueYi
 */
public class SocketTest {


    public static void main(String[] args) throws IOException {

        ServerSocket serverSocket = new ServerSocket(8090);    //  创建一个新的ServerSocket，用以监听指定端口上的连接请求
        Socket clientSocket = serverSocket.accept();    //   ❶ 对accept()方法的调用将被阻塞，直到一个连接建立
        BufferedReader in = new BufferedReader(
                new InputStreamReader(clientSocket.getInputStream()));
        PrintWriter out =
                new PrintWriter(clientSocket.getOutputStream(), true);  //   ❷ 这些流对象都派生于该套接字的流对象
        String request, response;

        while ((request = in.readLine()) != null) {    //   ❸ 处理循环开始
            if ("Done".equals(request)) {
                break;     //  如果客户端发送了“Done”，则退出处理循环
            }
//            response = processRequest(request);     //      ❹ 请求被传递给服务器的处理方法
//            out.println(response);    //  服务器的响应被发送给了客户端
        }    // 继续执行处理循环”

    }
}
