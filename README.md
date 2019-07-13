## 使用说明
### 第一步导入aar文件
##### 将aar文件添加到libs文件中
```gradle
implementation(name: 'csjsdk-beta', ext: 'aar')
```
##### 在app的build.gradle文件的android{}结构下添加如下代码
```gradle
repositories {
    flatDir {
        dirs 'libs'
    }
}
```
### 第二步引入依赖库
```gradle
implementation 'io.netty:netty-all:4.1.23.Final'
```

## 代码调用
##### 在Application中初始化SDK
```java
CsjRobot.getInstance().init(this);
```
##### 机器人连接状态事件
```java
CsjRobot.getInstance().registerConnectListener(new OnConnectListener() {
            @Override
            public void success() {
                startActivity(new Intent(SplashActivity.this,MainActivity.class));
            }

            @Override
            public void faild() {

            }

            @Override
            public void timeout() {

            }

            @Override
            public void disconnect() {

            }
        });
```
##### 机器人连接成功之后需要加载地图
```java
mCsjBot.getAction().loadMap();
```