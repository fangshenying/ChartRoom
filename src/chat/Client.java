package chat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

/**
 * 	聊天室客服端
 * @author fangshenying
 *
 */
public class Client {
	/*java.net.Socket
	 * 封装了TCP协议
	 * 使用它可以与远程计算机连接并进行
	 * 数据交换，实现通讯的目的
	 */
	private Socket socket;
	
	/**
	 * 构造方法，用来初始化客服端
	 * @throws IOException 
	 * @throws UnknownHostException 
	 */
	public Client() throws UnknownHostException, IOException{
		/*
		 * 初始化Socket
		 * 初始化需要传入两个参数
		 * 参数1：远端计算机IP地址(找到特定的计算机)
		 * 参数2：远端计算机的服务端口(找到计算机上特定的应用程序)
		 * 
		 * IP地址用来找到服务端所在的计算机，
		 * 端口用来连接上该计算机上的服务端应用程序。
		 * 
		 * 实例化Socket的过程就是连接的过程
		 * 若远程计算机没有响应会抛出异常
		 */
		System.out.println("正在连接服务端...");
//		socket = new  Socket("192.168.121.78",8088);//李德强
//		socket = new  Socket("192.168.121.112",8088);//刘胜
//		socket = new  Socket("192.168.121.43",8088);//薛小强
//		socket = new  Socket("192.168.121.65",8088);//万明宇
		socket = new  Socket("localhost",8088);
		System.out.println("与服务端建立连接！");
	}
	
	/**
	 * 客服端开始工作的方法
	 */
	public void start(){
		try {
			//启动读取服务端消息的线程
			ServerHandler handler = new ServerHandler();
			Thread t = new Thread(handler);
			t.start();
			
			Scanner scanner = new Scanner(System.in);
			
			/*
			 * Socket的方法：
			 * OutputStream getOutputStream()
			 * 用于获取一个输入流，通过该输入流写出的字节会发送至远端的计算机，
			 * 而远端计算机可以通过输入流读取。
			 */
			
			OutputStream out = socket.getOutputStream();//发送
			OutputStreamWriter osw = new OutputStreamWriter(out,"UTF-8");//编码方式
			PrintWriter pw = new PrintWriter(osw, true);//自动行刷新
			
			String message = null;
			while(true){
				message = scanner.nextLine();
				pw.println(message);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
	}
	
	public static void main(String[] args) {
		try{
			//网络有很多不可靠因素，要捕获异常
			Client client = new Client();
			client.start();
		} catch(Exception e){
			e.printStackTrace();
			System.out.println("客服端启动失败！");
		}
		
	}
	
	/**
	 * 该线程的任务是读取服务器端发送过来的每一条消息，并输入到控制台。
	 */
	class ServerHandler implements Runnable{
		public void run() {
			try {
				InputStream in = socket.getInputStream();
				InputStreamReader isr = new InputStreamReader(in,"UTF-8");
				BufferedReader br = new BufferedReader(isr);
				String message = null;
				//读取服务端发送的每一条消息并输出
				while((message=br.readLine())!=null){
					System.out.println(message);
				}
			} catch (Exception e) {
				
			}
			
		}
		
	}
	
	
	
}
