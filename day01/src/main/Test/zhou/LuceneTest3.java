package zhou;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.FSDirectory;
import org.junit.Before;
import org.junit.Test;
import org.wltea.analyzer.lucene.IKAnalyzer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.apache.lucene.document.Field.Store.YES;

public class LuceneTest3 {
    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void queryIndex() throws IOException {
        Path path = Paths.get("D:\\个人文件\\java后端\\Lucene_Demo\\day01\\index_loc");
        FSDirectory open = FSDirectory.open(path);
        // 创建索引的读取对象
        DirectoryReader reader = DirectoryReader.open(open);
        // 创建索引库的所有对象
        IndexSearcher is = new IndexSearcher(reader);
        // 创建语汇单元的对象(查询语汇单元文件名称为test.txt的文件)
        Term term = new Term("fileName", "test.txt");
        // 创建分词的语汇查询对象
        TermQuery tq = new TermQuery(term);
        // 查询（前多少条）
        TopDocs result = is.search(tq, 100);
        // 总记录数
        int total = (int) result.totalHits;
        System.out.println("总记录数是：" + total);

        for (ScoreDoc sd : result.scoreDocs) {
            // 获得文档的id
            int id = sd.doc;
            // 获得文档对象
            Document doc = is.doc(id);
            String fileName = doc.get("fileName");
            String size = doc.get("size");
            String content = doc.get("content");
            String path1 = doc.get("path");

            System.out.println("文件名:" + fileName);
            System.out.println("大小:" + size);
            System.out.println("内容:" + content);
            System.out.println("路径:" + path);
            System.out.println("-------------------------");
        }

    }
}
