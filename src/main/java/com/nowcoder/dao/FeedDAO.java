package com.nowcoder.dao;

import com.nowcoder.model.Comment;
import com.nowcoder.model.Feed;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * Created by albert on 2017/8/9.
 */
@Mapper
public interface FeedDAO {
    String TABLE_NAME = " feed ";
    String INSERT_FIELDS = " type,user_id,created_date,data ";
    String SELECT_FIELDS = " id," + INSERT_FIELDS;

    @Insert({" insert into" + TABLE_NAME + "("+INSERT_FIELDS+
            ") values(#{type},#{userId},#{createdDate},#{data})"})
    int addFeed(Feed feed);


    @Select({" select" , SELECT_FIELDS," from ",TABLE_NAME,"where id = #{id}"})
    Feed getFeedById(int id);


    List<Feed> selectFeedStream(@Param("maxId") int maxId, @Param("userIds") List<Integer> userIds,
                                        @Param("count") int count);



}
