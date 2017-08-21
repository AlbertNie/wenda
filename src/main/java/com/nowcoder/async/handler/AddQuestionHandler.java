package com.nowcoder.async.handler;

import com.nowcoder.async.EvenModel;
import com.nowcoder.async.EvenType;
import com.nowcoder.async.EventHandler;
import com.nowcoder.controller.CommentController;
import com.nowcoder.model.Message;
import com.nowcoder.service.MessageServive;
import com.nowcoder.service.SearchService;
import com.nowcoder.service.UserService;
import com.nowcoder.util.WendaUtil;
import org.apache.solr.client.solrj.SolrServerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Created by albert on 2017/8/15.
 */
@Component
public class AddQuestionHandler implements EventHandler {
    private static final Logger logger = LoggerFactory.getLogger(CommentController.class);
    @Autowired
    MessageServive messageServive;
    @Autowired
    UserService userService;
    @Autowired
    SearchService searchService;

    @Override
    public void doHandle(EvenModel event) {
        try {
            searchService.indexQuestion(event.getEntityId(),event.getExt("question_title"),event.getExt("question_content"));
        } catch (Exception e) {
            logger.error("添加索引失败："+e.getMessage());
            doHandle(event);
        }
    }

    @Override
    public List<EvenType> getSupportEventTypes() {
        return Arrays.asList(EvenType.ADDQUESTION);
    }
}
