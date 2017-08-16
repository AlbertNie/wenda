package com.nowcoder.dao;

import com.nowcoder.model.LoginTicket;
import com.nowcoder.model.User;
import org.apache.ibatis.annotations.*;

/**
 * Created by nowcoder on 2016/7/2.
 */
@Mapper
public interface LoginTicketDAO {
    // 注意空格
    String TABLE_NAME = " login_ticket ";
    String INSERT_FIELDS = " user_id, ticket, expired, status ";
    String SELECT_FIELDS = " id, " + INSERT_FIELDS;

    @Insert({"insert into ", TABLE_NAME, "(", INSERT_FIELDS,
            ") values (#{userId},#{ticket},#{expired},#{status})"})
    int addLoginTicket(LoginTicket loginTicket);

    @Select({"select ", SELECT_FIELDS, " from ", TABLE_NAME, " where ticket=#{ticket}"})
    LoginTicket selectByTicket(String ticket);


    @Update({"update ", TABLE_NAME, " set status=#{status} where ticket=#{ticket}"})
    void updateTicketByTicket(@Param("status") int status,
                      @Param("ticket") String ticket);

    @Update({"update ", TABLE_NAME, " set status=#{status} where user_id=#{userId}"})
    void updateTicketByUserId(@Param("status") int status,
                      @Param("userId") int userId);


}
