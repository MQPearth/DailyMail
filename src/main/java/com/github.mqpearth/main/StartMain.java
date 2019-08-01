package com.github.mqpearth.main;


import com.github.mqpearth.model.pojo.User;
import com.github.mqpearth.service.ApiService;
import com.github.mqpearth.service.UserService;
import com.github.mqpearth.util.FormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.util.List;


@Component
public class StartMain implements CommandLineRunner {

    @Autowired
    private UserService userService;

    @Autowired
    private FormatUtils formatUtils;

    @Autowired
    private ApiService apiService;

    private String appName = "每日邮件：";

    /**
     * 实际的入口函数
     *
     * @param args
     * @throws Exception
     */
    public void run(String... args) throws Exception {
        if (null != args && args.length > 0) {
            String command = args[0]; // 获取启动命令
            if ("-a".equals(command)) {
                //-a 添加模式   添加数据   args[1] 是 name args[2] 是 mail args[3] 是 city
                if (!formatUtils.checkStringNull(args) || args.length < 4) {
                    System.out.println(appName + "添加失败--字段不规范");
                    return;
                }
                if (!formatUtils.checkMail(args[2])) {
                    System.out.println(appName + "添加失败--邮箱格式错误");
                    return;
                }
                try {
                    User user = userService.findUserByMail(args[2]);
                    if (null == user) {
                        userService.insertUser(args);
                        System.out.println(appName + "添加成功");
                    } else {
                        userService.updateUserByMail(args);
                        System.out.println(appName + "邮箱为" + args[2] + "的用户信息修改成功");
                    }

                } catch (RuntimeException re) {
                    System.out.println(appName + "添加失败--" + re.getMessage());
                } catch (Exception e) {
                    System.out.println(appName + "添加失败--" + e.getMessage());
                }

            } else if ("-l".equals(command)) {
                //查询模式   查询通知列表
                System.out.println("name \tmail \t\tcity");
                List<User> users = userService.listUser();
                for (User user : users) {
                    System.out.println(user.getName() + " \t" + user.getMail() + " \t\t" + user.getCity());
                }

            } else if ("-d".equals(command)) {
                //删除模式
                if (!formatUtils.checkMail(args[1]))
                    System.out.println(appName + "邮箱格式错误");
                userService.deleteUserByMail(args[1]);
                System.out.println(appName + "删除成功");


            } else if ("-h".equals(command)) {
                //帮助模式
                System.out.println("-a\t添加用户信息 [name] [mail] [city] 重复的mail视为修改信息 例: -a 马云 110@qq.com 杭州");
                System.out.println("-d\t删除用户信息 [mail] 例: -d 110@qq.com");
                System.out.println("-h\t查看命令 例: -h");
                System.out.println("-l\t查询用户列表 例: -l");

            } else {
                System.out.println(appName + "没有这个命令");
                //打印帮助信息
                System.out.println("-h\t查看命令 例: -h");
            }

        } else {
            //不带参数启动 发送邮件提示
            try {
                List<MimeMessage> mails = apiService.getApiData();
//                发送邮件
                for (MimeMessage mail : mails) {
                    userService.sendMail(mail);
                    Thread.sleep(1000);
                }

                System.out.println(appName + "邮件发送成功");
            } catch (MessagingException me) {
                System.out.println(appName + "邮件发送失败--" + me.getMessage());
            } catch (IOException e) {
                System.out.println(appName + "数据获取失败--" + e.getMessage());
            }
        }
    }


}
