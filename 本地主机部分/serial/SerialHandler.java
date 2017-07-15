







/**

 * SerialHandler类是串口操作类
 */
package com.zhenxuyang.smarthome.serial;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.SeekableByteChannel;
import java.util.TooManyListenersException;

import javax.sound.sampled.AudioFormat.Encoding;

import com.zhenxuyang.smarthome.http.HttpHandler;

import purejavacomm.CommPortIdentifier;
import purejavacomm.NoSuchPortException;
import purejavacomm.PortInUseException;
import purejavacomm.SerialPort;
import purejavacomm.SerialPortEvent;
import purejavacomm.SerialPortEventListener;
import purejavacomm.UnsupportedCommOperationException;

/**
 * @author Administrator
 * @Date 2017年7月9日
 */
public class SerialHandler {

	/**
	 * 使用串口的应用名称
	 */
	static String NAME_APPLICATION = "SmartHome";
	/**
	 * 串口名称
	 */
	static String SERIALPORT_NAME = "COM3";
	/**
	 * 端口标志类
	 */
	CommPortIdentifier portd;
	/**
	 * 串口类
	 */
	SerialPort sPort;
	/**
	 * 串口输入流
	 */
	InputStream ins;
	/**
	 * 串口输出流
	 */
	OutputStream outs;
	/**
	 * 输入流缓冲区
	 */
	byte[] buffer;
	/*
	 * 输入流缓冲大小
	 */
	int remained;

	int loc;

	int addr;

	HttpHandler handler;

	/**
	 * 构造方法
	 */
	public SerialHandler() {
		// TODO Auto-generated constructor stub
		System.out.println("串口处理类运行");
		init();
		serialDataReceiver();
	}

	/**
	 * 初始化方法
	 */
	void init() {
		// 波特率
		int baudrate = 57600;
		// 数据位数
		int datebit = 8;
		// 停止位数
		int stopbit = 1;
		// 奇偶校验位
		int parity = SerialPort.PARITY_NONE;
		try {
			buffer = new byte[10000];
			remained = 0;

			handler=new HttpHandler();
			// 获得端口标志，并生成串口实例
			portd = CommPortIdentifier.getPortIdentifier(SERIALPORT_NAME);
			// 1000ms为打开串口时阻塞的时间
			sPort = (SerialPort) portd.open(NAME_APPLICATION, 1000);
			sPort.setSerialPortParams(baudrate, datebit, stopbit, parity);
			sPort.notifyOnDataAvailable(true);
			sPort.notifyOnOutputEmpty(true);
			ins = sPort.getInputStream();
			outs = sPort.getOutputStream();
		} catch (NoSuchPortException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (PortInUseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedCommOperationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 接受串口信息并解析
	 */
	public void serialDataReceiver() {

		try {
			sPort.addEventListener(new SerialPortEventListener() {
				public void serialEvent(SerialPortEvent event) {
					// TODO Auto-generated method stub
					if(event.getEventType()==SerialPortEvent.DATA_AVAILABLE) {
						try {
							int n=ins.available();
							byte[] buffer=new byte[n];
							ins.read(buffer,0,n);
							String str="";
							for(int i=0;i<n;i++) {
								if(buffer[i]!=13&&buffer[i]!=10) {
									str+=(char)buffer[i];
								}
							}
							//System.out.println(str);
							if(str.length()==12&&str.charAt(0)=='t') {
								int t_index=str.indexOf('t');
								int h_index=str.indexOf('h');
								System.out.println(str.substring(t_index+2, t_index+6));
								System.out.println(str.substring(h_index+2, h_index+6));
								handler.sendData(Float.parseFloat(str.substring(t_index+2, t_index+6)),
										Float.parseFloat(str.substring(h_index+2, h_index+6)));
								Thread.sleep(3000);
							}
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
			});
			while(true) {

			}
		} catch (TooManyListenersException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void bufferGetter() {
		Thread thread1=new Thread(new Runnable() {

			public void run() {
				// TODO Auto-generated method stub
				System.out.println("Thread1运行");
				while(true) {
					//System.out.print("1");
					try {
						buffer[addr]=(byte) ins.read();
						addr=(addr+1)%10000;
						remained++;
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					try {
						Thread.sleep(500);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		});
		thread1.start();
	}

	/*
	 * 输入缓冲的处理线程
	 */
	public void bufferReader() {
		Thread thread2 = new Thread(new Runnable() {
			public void run() {
				// TODO Auto-generated method stub
				System.out.println("Thread2运行");
			while(true) {
				//System.out.print("2");
				if (remained != 0) {
					System.out.print((char) buffer[loc]);
					loc = (loc + 1) % 10000;
					remained--;
				}
			}

			}
		});
		thread2.start();
	}
}
