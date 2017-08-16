package com.nowcoder.dao;

import com.nowcoder.model.Comment;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * Created by albert on 2017/8/9.
 */
@Mapper
public interface CommentDAO {
    String TABLE_NAME = " comment ";
    String INSERT_FIELDS = " content,user_id,created_date,entity_id,entity_type,status ";
    String SELECT_FIELDS = " id," + INSERT_FIELDS;

    @Insert({" insert into" + TABLE_NAME + "("+INSERT_FIELDS+
            ") values(#{content},#{userId},#{createdDate},#{entityId},#{entityType},#{status})"})
    int addComment(Comment comment);

    @Update({" update "+ TABLE_NAME + " set status=1 where id=#{id}"})
    int updateComment(int id);

    @Update({" update "+ TABLE_NAME + " set entity_id=#{entityId},entity_type=#{entityType} where id=#{id}"})
    int updateCommentTest(@Param("entityId") int entityId,
                          @Param("entityType") int entityType,
                          @Param("id") int id);

    @Select({" select ",SELECT_FIELDS,"from ",TABLE_NAME,
            "where entity_id=#{entityId} and entity_type=#{entityType} order by id desc limit #{offset},#{limit}"})
    List<Comment> selectCommentByEntity(@Param("entityId") int entityId, @Param("entityType") int entityType,
                                        @Param("offset") int offset, @Param("limit") int limit);

    @Select({" select" , SELECT_FIELDS," from ",TABLE_NAME,"where id = #{id}"})
    Comment getCommentById(int id);


}
