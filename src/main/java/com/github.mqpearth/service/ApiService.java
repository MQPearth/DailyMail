package com.github.mqpearth.service;

import com.github.mqpearth.dao.ApiDao;
import com.github.mqpearth.dao.UserDao;
import com.github.mqpearth.model.entity.MailMessage;
import com.github.mqpearth.model.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 对网页中提取的数据进行封装
 */
@Service
public class ApiService {

    @Autowired
    private ApiDao apiDao;

    @Autowired
    private UserDao userDao;

    @Autowired
    private MailMessage mailMessage;

    /**
     * 获取api的数据，并将数据封装进SimpleMailMessage
     *
     * @return
     */
    public List<MimeMessage> getApiData() throws IOException, MessagingException {

        List<MimeMessage> mails = new ArrayList<>(10);

        //获取所有用户
        List<User> users = userDao.listUser();
        String sentence = apiDao.getSentence();
        String blog = apiDao.getBlog();

        for (User user : users) {
            StringBuffer message = new StringBuffer();
            message.append("<html>\n" +
                    "    <head>\n" +
                    "        <title>通知邮件</title>\n" +
                    "        <meta charset=\"utf-8\"/>" +

                    "\n</head>" +
                    " <body>");


            String cityId = apiDao.getCityId(user.getCity());
            String weather = "<h4>城市 [" + user.getCity() + "]</h4>" + apiDao.getWeatherByCityId(cityId);

            message.append("<h3>你好，" + user.getName() + "，今天是").append(apiDao.getDate()).append("</h3><br />");

            message.append("<h3>【每日天气】</h3>\n");
            message.append(weather);


            message.append("<h3>【每日鸡汤】</h3>\n");
            message.append(sentence).append("<br />");


            message.append("<h3>【每日博客】</h3>\n");
            message.append(blog).append("\n");

            message.append("<hr />");

            message.append("来自开源项目:DailyMail 项目地址<a href=\"https://github.com/MQPearth/DailyMail\">https://github.com/MQPearth/DailyMail</a>");

            message.append("</body>\n" +
                    "</html>");
//            System.out.println(message.toString());
            mails.add(mailMessage.create(user.getMail(), "每日邮件", message.toString()));
        }

        return mails;
    }
}
