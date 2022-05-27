package com.wavecom.nowcoder.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.CharUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author liujilong
 * @Project nowcoder-backend
 * @File SensitiveUtil
 * @Date 2022/5/25 5:27 下午
 **/
@Slf4j
@Component
public class SensitiveUtil {

    /**
     * 前缀树
     */
    private class TrieNode {
        private boolean isKeywordEnd = false;
        private Map<Character, TrieNode> subNodes = new HashMap<>();

        public boolean isKeywordEnd() {
            return isKeywordEnd;
        }

        public void setKeywordEnd(boolean keywordEnd) {
            isKeywordEnd = keywordEnd;
        }

        /**
         * 添加子节点
         * @param c
         * @param node
         */
        public void addSubNode(Character c, TrieNode node) {
            subNodes.put(c, node);
        }

        /**
         * 获取子节点
         * @param c
         * @return
         */
        public TrieNode getSubNode(Character c) {
            return subNodes.get(c);
        }
    }

    /**
     * 替换后的字符
     */
    private static final String REPLACEMENT = "***";

    private TrieNode root = new TrieNode();

    /**
     * 将一个敏感词加入前缀树
     * @param keyword
     */
    private void addKeyword(String keyword) {
        TrieNode temp = root;
        for (int i = 0; i < keyword.length(); i++) {
            char c = keyword.charAt(i);
            TrieNode subNode = temp.getSubNode(c);
            if (subNode == null) {
                //初始化子节点
                subNode = new TrieNode();
                temp.addSubNode(c, subNode);
            }

            //指向子节点，进入下一轮循环
            temp = subNode;
            //设置结束标识
            if (i == keyword.length() - 1) {
                temp.setKeywordEnd(true);
            }
        }
    }

    /**
     * 判断是否为符号
     * @param c
     * @return
     */
    private boolean isSymbol(Character c) {
        //0x2E80-0x9FFFF是东亚文字范围
        return !CharUtils.isAsciiAlphanumeric(c) && (c < 0x2E80 || c > 0x9FFFF);
    }

    /**
     * 在项目启动之后执行，加载数据
     */
    @PostConstruct
    public void loadSensitiveWords() {
        try (
                InputStream is = this.getClass().getClassLoader().getResourceAsStream("sensitive-words.txt");
                BufferedReader reader = new BufferedReader(new InputStreamReader(is));
                ) {
            String keyword;
            while ((keyword = reader.readLine()) != null) {
                //添加到前缀树
                this.addKeyword(keyword);
            }
        } catch (IOException e) {
            log.error("敏感词加载失败");
        }
    }

    /**
     * 过滤敏感词
     * @param text
     * @return
     */
    public String filter(String text) {
        if (StringUtils.isBlank(text)) {
            return null;
        }

        TrieNode temp = root;
        int begin = 0, position = 0;
        //结果
        StringBuilder sb = new StringBuilder();

        while (position < text.length()) {
            char c = text.charAt(position);
            //跳过符号
            if (isSymbol(c)) {
                if (temp == root) {
                    sb.append(c);
                    begin++;
                }
                position++;
                continue;
            }

            temp = temp.getSubNode(c);
            if (temp == null) {
                //以begin开头的字符串不是敏感词
                sb.append(text.charAt(begin));
                //进入下一个位置
                position = ++begin;
                //重新指向根节点
                temp = root;
            } else if (temp.isKeywordEnd()) {
                //发现敏感词，将begin-position字符串替换掉
                sb.append(REPLACEMENT);
                //进入下一个位置
                begin = ++position;
                //重新指向根节点
                temp = root;
            } else {
                //检查下一个字符
                position++;
            }
        }
        //将最后一批自负计入结果
        sb.append(text.substring(begin));

        return sb.toString();
    }
}
