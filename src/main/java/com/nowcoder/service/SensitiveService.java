package com.nowcoder.service;

import com.nowcoder.controller.LoginController;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang.CharUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map;

/**
 * Created by albert on 2017/8/11.
 */
@Service
public class SensitiveService implements InitializingBean {
    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);
    private String replace = "（不可描述）";
    private TrieNode root = new TrieNode();

    @Override
    public void afterPropertiesSet() throws Exception {
        BufferedReader reader = null;
        try {
            InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("SensitiveWords.txt");
            InputStreamReader is = new InputStreamReader(inputStream);
            reader = new BufferedReader(is);
            String nextLine = null;

            while ((nextLine = reader.readLine()) != null) {
                nextLine = nextLine.trim();
                addWord(nextLine);
            }
        } catch (IOException e) {
            logger.error("初始化屏蔽字出错："+e.getMessage());
        } finally {
            reader.close();
        }
    }

    private void addWord(String word){
        TrieNode node = root;
        word = word.trim();
        for (int i = 0; i < word.length(); i++) {
            char c = word.charAt(i);
            if (isSymbol(c))
                continue;
            if (!node.contains(c))
                node.addTrie(c);
            node = node.getNextTrieNode(c);
            if (i == word.length() - 1)
                node.setEnd();
        }
    }

    private class TrieNode {
        boolean End = false;
        Map<Character, TrieNode> next = new HashedMap();

        public boolean contains(Character c) {
            return next.get(c) != null;
        }

        public TrieNode getNextTrieNode(Character c) {
            return next.get(c);
        }

        public boolean isEnd() {
            return End;
        }

        public void addTrie(Character c) {
            next.put(c, new TrieNode());
        }

        public void setEnd() {
            End = true;
        }
    }

    private boolean isSymbol(char c) {
        int ic = (int) c;
        // 0x2E80-0x9FFF 东亚文字范围
        return !CharUtils.isAsciiAlphanumeric(c) && (ic < 0x2E80 || ic > 0x9FFF);
    }

    public String filte(String txt) {
        if (StringUtils.isBlank(txt))
            return txt;
        StringBuilder sb = new StringBuilder();

        TrieNode teamNode = root;
        int begin = 0;
        int position = 0;

        while (position < txt.length()) {
            char c = txt.charAt(position);

            if (isSymbol(c)) {
                if (teamNode == root) {
                    sb.append(c);
                    begin++;
                }
                position++;
                continue;
            }
            teamNode = teamNode.getNextTrieNode(c);

            if (teamNode == null) {
                sb.append(c);
                begin = begin + 1;
                position = begin;
                teamNode = root;
            } else if (teamNode.isEnd()) {
                sb.append(replace);
                begin = position + 1;
                position = begin;
                teamNode = root;
            } else {
                position++;
            }
        }

        sb.append(txt.substring(begin));

        return sb.toString();
    }

    public static void main(String[] args) {
        SensitiveService s = new SensitiveService();
        System.out.println(s.filte("hah哈哈 我日你妈苏打粉卡省地方"));
    }
}

