# 数据传输协议

data packet   （max length ）

|LENGTH| BODY |
|:---:| :---: |
|2 bytes| Variable length |

## BODY段

### 心跳包 (DATA TYPE：0x01)

| DATA TYPE |
| :-------: |
|   1byte   |

### 验证指令 (DATA TYPE ：0x02)     验证协议(模仿socks)--构建server和client之间的通道

#### 第一步 client向server发送
|DATA TYPE|VER|NMETHODS|METHODS|
|:-----:|:-----:|:-----:|:-----:|
|1byte |1 byte|1 byte|1 to 255 byte|
 - VER 是指协议版本，目前是 0x01
 - NMETHODS 是指有多少个可以使用的方法，也就是客户端支持的认证方法，有以下值：
    - 0x00 NO AUTHENTICATION REQUIRED 不需要认证
    - 0x01 USERNAME/PASSWORD 用户名密码认证
    - 0xFF NO ACCEPTABLE METHODS 不接受任何方法/没有合适的方法
- METHODS 就是方法值，有多少个方法就有多少个byte

#### 第二步 server向client响应验证支持的验证方法

|DATA TYPE|  VER   | NMETHODS |    METHODS    |
|:-----:| :----: | :------: | :-----------: |
| 1 byte| 1 byte |  1 byte  | 1 to 255 byte |

#### 第三步 client向server发送验证信息

|DATA TYPE|  VER   | METHOD | LENGTH  |      DATA       |
|:-----:| :----: | :----: | :-----: | :-------------: |
| 1 byte| 1 byte | 1 byte | 2 bytes | Variable length |

- LENGTH 验证信息的长度
- DATA验证信息数据

#### 第四步 server向client发送验证结果

|DATA TYPE|  VER   | RESULT |
|:-----:| :----: | :----: |
| 1 byte| 1 byte | 1 byte |

RESULT 验证结果

- 0x01 认证成功
- 0xFF 认证失败

### 代理连接指令 (DATA TYPE ：0x03)

#### 客户端给服务端发送连接远程指令

|DATA TYPE|  TARGRT TYPE  |     TARGRT      |  PORT   | LOCAL PORT |
|:----:| :----: | :-------------: | :-----: | :--------: |
|1 byte| 1 byte | Variable length | 2 bytes |  2 bytes   |

- TARGRT TYPE：TARGRT类型
  - 0x00：IP v4
  - 0x01：域名（第一个字节为域名长度）
  - 0x02：IP v6
- TARGRT：HOST
- PORT：端口
- LOCAL PORT: 应用连接client的本地端口

#### 服务端给客户端发送连接结果

|DATA TYPE|   RESULT  | LOCAL PORT |
|:----:|:-------------: | :--------: |
|1 byte| 1 byte |  2 bytes   |

- RESULT
  - 0x01: 连接成功

### 数据 (DATA TYPE ：0x04)

| DATA TYPE | LOCAL PORT |      DATA       |
| :-------: | :--------: | :-------------: |
|  1 byte   |  2 bytes   | Variable length |

DATA暂时限制最大1024*10个字节

data_length <=  max_data_packet_length - 5