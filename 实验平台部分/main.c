/* 包含头文件 */
/********************************************************************/
#include <stdio.h>
#include <string.h>
#include <math.h>

#include "./uart/hal_board_cfg.h"
#include "./uart/hal_uart.h"
#include "./sht11/sht11.h"
/********************************************************************/
//定义控制灯的端口
#define LED1 P1_0
#define LED2 P1_1

#define uint unsigned int
#define uchar unsigned char

void LedInit(void)
{
  P1DIR |= 0x03; //设置LED
  LED1 = 1;
  LED2 = 1;
}
/*********************************************************************
 * 函数名称：main
 * 功    能：main函数入口
 * 入口参数：无
 * 出口参数：无
 * 返 回 值：无
 ********************************************************************/
void main(void)
{
    char temp_buf[10];
    char humi_buf[10];
    float humi,temp;
    
    LED1 = 0;       //LED1点亮，表示程序启动
    InitUart();     // 初始化串口   bautrate:57600
    Sht11Init();   
    
    while(1)
    {
        if(GetHumiAndTemp(&humi,&temp) == 0)
        {
             sprintf(humi_buf, (char *)"%f", humi);
             sprintf(temp_buf, (char *)"%f", temp);
      
             prints("temp:");	 prints(temp_buf);	
             prints("     humi:");	 prints(humi_buf);	
             prints("\r\n");
             
             Sht11Delay(50000);  
             Sht11Delay(50000);   
             Sht11Delay(50000);  
             
             LED2 = !LED2;           //LED2闪烁，表示温湿度采样正常
        }
    }
}
