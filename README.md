# MaaTouch

minitouch 输入协议的安卓原生实现

## 使用说明

参考 [minitouch usage](https://github.com/DeviceFarmer/minitouch#usage)

### 指令一览

- `c` 提交指令
- `r` 重置触点指令
- `d id x y pressure` 按下触点
- `m id x y pressure` 移动触点
- `u id` 抬起触点
- `w ms` 指令执行等待（不建议使用，推荐调用方自行延迟）
- `k key d` 按键按下
- `k key u` 按键抬起
- `k key o` 单次按键，按下抬起
- `t text` 输入文本

### 示例

- 在 (10, 10) 点击

  ```text
  d 0 10 10 1
  u 0
  c
  ```

- 滑动

  ```text
  d 0 10 10 1
  w 100
  m 0 20 20 1
  w 100
  m 0 30 30 1
  w 100
  u 0
  c
  ```

- 点亮屏幕

  ```text
  k 224 o
  c
  ```

- 删除文本框内容

  ```text
  k 123 o
  k 67 d
  w 1000
  k 67 u
  c
  ```

- 输入文本

  ```text
  t 内容
  c
  ```
