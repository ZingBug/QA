package com.zingbug.qa.serviec.impl;

import com.zingbug.qa.comm.Const;
import com.zingbug.qa.serviec.SensitiveService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.CharUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by ZingBug on 2019/7/1.
 */
@Slf4j
@Service
public class SensitiveServiceImpl implements SensitiveService, InitializingBean {

    /**
     * 根节点
     */
    private TrieNode rootNode;

    @Override
    public void afterPropertiesSet() {
        rootNode = new TrieNode();
        try {
            InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream("SensitiveWords.txt");
            InputStreamReader reader = new InputStreamReader(is);
            BufferedReader bufferedReader = new BufferedReader(reader);
            String lineText;
            while ((lineText = bufferedReader.readLine()) != null) {
                lineText = lineText.trim();
                addWord(lineText);
            }
            reader.close();
        } catch (Exception e) {
            log.error("读取敏感词文件出错 " + e.getMessage());
        }
    }

    //前缀树
    private class TrieNode {
        /**
         * true 关键词的终结 ; false 继续
         */
        private boolean end = false;
        /**
         * key下一个字符，value是对应的节点
         */
        private Map<Character, TrieNode> subNodes = new HashMap<>();

        /**
         * 向指定位置添加节点
         *
         * @param key
         * @param node
         */
        void addSubNode(Character key, TrieNode node) {
            subNodes.put(key, node);
        }

        /**
         * 获取下一个节点
         *
         * @param key
         * @return
         */
        TrieNode getSubNode(Character key) {
            return subNodes.get(key);
        }

        boolean isKeywordEnd() {
            return end;
        }

        void setKeywordEnd(boolean end) {
            this.end = end;
        }

        public int getSubNodeCount() {
            return subNodes.size();
        }


    }

    public void addWord(String lineText) {
        TrieNode tempNode = rootNode;
        for (int i = 0; i < lineText.length(); i++) {
            Character c = lineText.charAt(i);
            //忽略空格
            if (isSymbol(c)) {
                continue;
            }
            TrieNode node = tempNode.getSubNode(c);

            if (node == null) {
                //没有初始化
                node = new TrieNode();
                tempNode.addSubNode(c, node);
            }
            tempNode = node;
            if (i == lineText.length() - 1) {
                //关键词结束，设置结束标志
                tempNode.setKeywordEnd(true);
            }

        }
    }

    /**
     * 判断是否是一个符号
     *
     * @param c
     * @return
     */
    private boolean isSymbol(Character c) {
        int ic = (int) c;
        // 0x2E80-0x9FFF 东亚文字范围
        return !CharUtils.isAsciiAlphanumeric(c) && (ic < 0x2E80 || ic > 0x9FFF);
    }


    @Override
    public String filter(String text) {
        if (StringUtils.isBlank(text)) {
            return text;
        }
        TrieNode tempNode = rootNode;
        String replacement = Const.DEFAULT_REPLACEMENT;
        int begin = 0;
        int position = 0;
        StringBuilder result = new StringBuilder();
        while (position < text.length()) {
            Character c = text.charAt(position);
            //特殊字符直接跳过
            if (isSymbol(c)) {
                if (tempNode.isKeywordEnd()) {
                    result.append(c);
                    ++begin;
                }
                ++position;
                continue;
            }
            tempNode = tempNode.getSubNode(c);
            //当前位置匹配结束
            if (tempNode == null) {
                //以begin开始的字符不存在敏感词
                result.append(text.charAt(begin));
                //跳到下一个字符开始测试
                position = begin + 1;
                begin = position;
                //回到树的根节点
                tempNode = rootNode;
            } else if (tempNode.isKeywordEnd()) {
                //发现敏感词
                result.append(replacement);
                //跳到敏感词的下一个词
                position = position + 1;
                begin = position;
                tempNode = rootNode;
            } else {
                ++position;
            }
        }
        result.append(text.substring(begin));
        return result.toString();
    }
}
