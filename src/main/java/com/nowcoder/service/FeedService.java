package com.nowcoder.service;

import com.nowcoder.dao.FeedDAO;
import com.nowcoder.model.Feed;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by albert on 2017/8/18.
 */
@Service
public class FeedService {
    @Autowired
    FeedDAO feedDAO;

    public List<Feed> getUserFeeds(int maxId, List<Integer>userIds, int count){
        return feedDAO.selectFeedStream(maxId,userIds,count);
    }

    public boolean addFeed(Feed feed){
        return feedDAO.addFeed(feed)>0?true:false;
    }

    public Feed selectFeedById(int id){
        return feedDAO.getFeedById(id);
    }
}
