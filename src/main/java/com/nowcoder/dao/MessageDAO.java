package com.nowcoder.dao;

import com.nowcoder.model.Message;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * Created by albert on 2017/8/9.
 */
@Mapper
public interface MessageDAO {
    String TABLE_NAME = " message ";
    String INSERT_FIELDS = " from_id,to_id,content,conversation_id,created_date,has_read ";
    String SELECT_FIELDS = " id," + INSERT_FIELDS;

    @Insert({" insert into" + TABLE_NAME + "(" + INSERT_FIELDS +
            ") values(#{fromId},#{toId},#{content},#{conversationId},#{createdDate},#{hasRead})"})
    int addMessage(Message message);


    @Select({" select",SELECT_FIELDS,"from (select ",SELECT_FIELDS,"from ",TABLE_NAME,
            "where conversation_id=#{conversationId} order by created_date desc limit #{offset},#{limit}) tt order by created_date asc"})
    List<Message> selectMessageByConversationId(@Param("conversationId") String conversationId,
                                        @Param("offset") int offset, @Param("limit") int limit);
    //SELECT * FROM (select * FROM message WHERE to_id=1 ORDER BY created_date) tt GROUP BY from_id ORDER BY created_date
    @Select({" select ",INSERT_FIELDS, ",count(id) as id from (select",SELECT_FIELDS, "FROM",TABLE_NAME,
            " WHERE to_id=#{userId} or from_id=#{userId} ORDER BY created_date desc) tt GROUP BY conversation_id ORDER BY created_date desc ",
            "limit #{offset},#{limit}"})
    List<Message> getConversationList(@Param("userId") int userId,
                                      @Param("offset") int offset,
                                      @Param("limit") int limit);

    @Select({" select count(id) from ",TABLE_NAME,"where to_id=#{userId} and conversation_id=#{conversationId} and has_read=0"})
    int getConversationCountUnread(@Param("userId") int userId,
                                   @Param("conversationId") String conversationId);

    @Update({" update",TABLE_NAME," set has_read=1 where id=#{id}"})
    int updateHasRead(@Param("id") int id);

}
