## 验证协议(模仿socks)
### 第一步 client向server发送
|VER|NMETHODS|METHODS|
|:-----:|:-----:|:-----:|
|1 byte|1 byte|1 to 255 byte|
 - VER 是指协议版本，目前是 0x01
 - NMETHODS 是指有多少个可以使用的方法，也就是客户端支持的认证方法，有以下值：
    - 0x00 NO AUTHENTICATION REQUIRED 不需要认证
    - 0x01 USERNAME/PASSWORD 用户名密码认证
    - 0xFF NO ACCEPTABLE METHODS 不接受任何方法/没有合适的方法
- METHODS 就是方法值，有多少个方法就有多少个byte

### 第二步 server向client响应验证支持的验证方法

|  VER   | NMETHODS |    METHODS    |
| :----: | :------: | :-----------: |
| 1 byte |  1 byte  | 1 to 255 byte |

### 第三步 client向server发送验证信息

|  VER   | METHOD | LENGTH  |      DATA       |
| :----: | :----: | :-----: | :-------------: |
| 1 byte | 1 byte | 2 bytes | Variable length |

- LENGTH 验证信息的长度
- DATA验证信息数据

### 第四步 server向client发送验证结果

|  VER   | RESULT |
| :----: | :----: |
| 1 byte | 1 byte |

RESULT 验证结果

- 0x01 认证成功
- 0xFF 认证失败

## 数据传输协议

|  TYPE  |     TARGRT      |  PORT   | LENGTH  |      DATA       |
| :----: | :-------------: | :-----: | :-----: | :-------------: |
| 1 byte | Variable length | 2 bytes | 2 bytes | Variable length |

- TYPE：TARGRT类型
  - 0x00：IP v4
  - 0x01：域名（第一个字节为域名长度）
  - 0x02：IP v6

- TARGRT：HOST

- PORT：端口

- LENGTH：数据长度

