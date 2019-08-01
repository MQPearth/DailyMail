package com.github.mqpearth.dao;

import com.github.mqpearth.model.pojo.User;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface UserDao {
    /**
     * 添加一条用户记录
     * @param user
     */
    @Insert("insert into user values(null,#{name},#{mail},#{city})")
    void insertUser(User user);


    /**
     * 根据邮件查询用户
     * @param mail
     * @return
     */
    @Select("select id,name,mail,city from user where mail = #{value}")
    User getUserByMail(String mail);


    /**
     * 查询所有用户
     * @return
     */
    @Select("select id,name,mail,city from user")
    List<User> listUser();

    @Update("update user set name = #{name},city = #{city} where mail = #{mail}")
    void updateUserByMail(User user);

    @Delete("delete from user where mail = #{value}")
    void deleteMail(String mail);
}
