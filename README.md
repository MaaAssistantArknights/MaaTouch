使用参考 [minitouch](https://github.com/DeviceFarmer/minitouch)

说明

c 提交指令

r 重置触点指令

d id x y pressure 按下触点

m id x y pressure 移动触点

u id 抬起触点

w ms 指令执行等待

k key d 按键按下

k key u 按键抬起

k key o 单次按键,按下抬起

t text 输入文本

示例:

1:在10 10点击

d 0 10 10 1

u 0

c

2:滑动

d 0 10 10 1

w 100

m 0 20 20 1

w 100

m 0 30 30 1

w 100

u 0

c

3: 点亮屏幕

k 224 o

c

4:删除文本框内容

k 123 o

k 67 d

w 1000

k 67 u

c

5:输入文本

t 内容

c
