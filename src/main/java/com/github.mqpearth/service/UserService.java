package com.github.mqpearth.service;

import com.github.mqpearth.dao.UserDao;
import com.github.mqpearth.model.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import javax.mail.internet.MimeMessage;
import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private JavaMailSender mailSender;


    /**
     * 添加一条用户记录
     *
     * @param userInfo
     */
    public void insertUser(String[] userInfo) {
        User user = new User();
        user.setName(userInfo[1]);
        user.setMail(userInfo[2]);
        user.setCity(userInfo[3]);
        userDao.insertUser(user);

    }

    /**
     * 发送邮件
     *
     * @param mail
     */
    public void sendMail(MimeMessage mail) {
        mailSender.send(mail);
    }


    /**
     * 查询所有用户
     *
     * @return
     */
    public List<User> listUser() {

        return userDao.listUser();
    }

    /**
     * 根据查询用户
     * @param mail
     * @return
     */
    public User findUserByMail(String mail) {
        return userDao.getUserByMail(mail);
    }

    /**
     * 根据邮箱修改用户
     * @param userInfo 用户信息
     */
    public void updateUserByMail(String[] userInfo) {
        User user = new User();
        user.setName(userInfo[1]);
        user.setMail(userInfo[2]);
        user.setCity(userInfo[3]);
        userDao.updateUserByMail(user);
    }

    /**
     * 根据邮箱删除用户
     */
    public void deleteUserByMail(String mail) {
        userDao.deleteMail(mail);

    }
}
