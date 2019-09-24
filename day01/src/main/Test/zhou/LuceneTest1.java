package zhou;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.cjk.CJKAnalyzer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.OffsetAttribute;
import org.junit.Before;
import org.junit.Test;
import org.wltea.analyzer.core.IKSegmenter;
import org.wltea.analyzer.core.Lexeme;
import org.wltea.analyzer.lucene.IKAnalyzer;

import java.io.*;

public class LuceneTest1 {
    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void importAnalyzer() throws IOException {
        // 创建分词器
//        StandardAnalyzer al = new StandardAnalyzer();
//        Analyzer al = new CJKAnalyzer();
        Analyzer al = new IKAnalyzer();

        // 分词
        TokenStream stream = al.tokenStream("content", "当前市场不稳定，得赶紧稳盘抛出。");
        // 分词对象的重置
        stream.reset();
        // 获得每一个语汇的偏移量属性对象
        OffsetAttribute oa = stream.addAttribute(OffsetAttribute.class);
        // 获得分词的语汇属性
        CharTermAttribute ca = stream.addAttribute(CharTermAttribute.class);
        // 遍历分词的语汇流
        while (stream.incrementToken()) {
            System.out.println("------------------");
            System.out.println("开始索引" + oa.startOffset() + "结束索引" + oa.endOffset());
            System.out.println(ca);
        }
    }
}
