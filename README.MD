### 功能

1. 邮件提醒
2. 每日天气
3. 每日鸡汤
4. 每日博客
5. 增删改查用户



#### 邮件格式

```text
你好，xxx，今天是2019年7月21日，农历六月十五

【每日天气】
城市[北京]
天气[晴]
温度[31℃ ~ 24℃]
白天风力[东南风<3级]
夜间风力[东北风<3级]
紫外线指数[最弱--辐射弱，涂擦SPF8-12防晒护肤品]
穿衣指数[短袖--适合穿T恤、短薄外套等夏季服装]
空气污染扩散指数[良--气象条件有利于空气污染物扩散]

【每日鸡汤】
有钱要什么鸡汤

【每日博客】
1. xxxx
2. xxxx
...
10. xxxx

```


##### 命令
1. -l 查看所有用户
2. -a [name] [mail] [city]添加一个用户 如果[mail]已存在，则视为修改
3. -d [mail] 删除一个用户
4. -h 查看帮助


##### 天气api


> ```url
> http://toy1.weather.com.cn/search?cityname=北京   通过此接口查询到城市id
> http://www.weather.com.cn/weather1d/{城市id}.shtml 
> ```


##### 鸡汤api

> ```url
> https://tool.lu/timestamp/
> ```

##### 博客api

> ```url
> https://www.oschina.net/blog
> ```

#### 设置定时发送邮件
linux 的 crontab 命令 ，每天的8：30发送邮件
crontab -e 进入编辑 ， 内容如下
```shell
SHELL=/bin/bash
PATH=/sbin:/bin:/usr/sbin:/usr/bin
MAILTO=root
HOME=/
30 8 * * * java -jar /root/dailymail.jar
```
