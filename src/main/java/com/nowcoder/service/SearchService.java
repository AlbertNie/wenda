package com.nowcoder.service;

import com.nowcoder.model.Question;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.request.UpdateRequest;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.client.solrj.response.UpdateResponse;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by albert on 2017/8/21.
 */
@Service
public class SearchService {
    private static final String SOLR_URL = "http://localhost:8983/solr/wenda";
    private static HttpSolrClient client = new HttpSolrClient.Builder(SOLR_URL).build();
    private static final String QUESTION_TITLE = "question_title";
    private static final String QUESTION_CONTENT = "question_content";

    public List<Question> searchQuestion(String key, int offset, int count,String hlpre, String hlpos) throws IOException, SolrServerException {
        List<Question> questionList = new ArrayList<>();
        SolrQuery query = new SolrQuery(key);
        query.setRows(count);
        query.setStart(offset);
        query.setHighlight(true);
        query.setHighlightSimplePre(hlpre);
        query.setHighlightSimplePost(hlpos);
        query.set("h1.f1",QUESTION_TITLE+","+QUESTION_CONTENT);
        QueryResponse response = client.query(query);
        for (Map.Entry<String, Map<String, List<String>>> entry : response.getHighlighting().entrySet()) {
            Question question = new Question();
            question.setId(Integer.parseInt(entry.getKey()));
            if (entry.getValue().containsKey(QUESTION_TITLE)){
                List<String> titles = entry.getValue().get(QUESTION_TITLE);
                if (!titles.isEmpty())
                    question.setTitle(titles.get(0));
            }
            if (entry.getValue().containsKey(QUESTION_CONTENT)){
                List<String> contents = entry.getValue().get(QUESTION_CONTENT);
                if (!contents.isEmpty())
                    question.setContent(contents.get(0));
            }
            questionList.add(question);
        }
        return questionList;
    }

    public boolean indexQuestion(int questionId,String title, String content) throws IOException, SolrServerException {
        SolrInputDocument doc = new SolrInputDocument();
        doc.setField("id",questionId);
        doc.setField("question_title",title);
        doc.setField("question_content",content);
        UpdateResponse response = client.add(doc,1000);
        return response != null && response.getStatus() == 0;
    }
}
