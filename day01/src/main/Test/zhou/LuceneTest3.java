package zhou;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.*;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.NumericUtils;
import org.junit.Before;
import org.junit.Test;
import org.wltea.analyzer.lucene.IKAnalyzer;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class LuceneTest3 {
    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void queryIndex() throws IOException {
        IndexSearcher is = getDirReader();

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
            System.out.println("路径:" + path1);
            System.out.println("-------------------------");
        }

    }

    @Test
    public void rangeQuery() throws IOException {
//        IndexSearcher is = getDirReader();
//        // 创建数值范围查询对象
//        Query tq = NumericRangeQuery.newLongRange("size", 01, 1001, true, true);
//        printDoc(is, tq);
    }

    /*
     * 多个条件的组合查询
     * */
    @Test
    public void queryIndex2() throws IOException {
//        IndexSearcher is = getDirReader();
//        // 创建BooleanQuery查询对象,这种查询对象可以控制是& | !
//        BooleanQuery bq = new BooleanQuery();
//        // 创建一个分词的语汇查询对象
//        Query query = new TermQuery(new Term("fileName", "test.txt"));
//        Query query1 = new TermQuery(new Term("content", "test.txt"));
//        bq.add(query, BooleanClause.Occur.MUST);
//        // SHOULD 可有可无
//        bq.add(query1, BooleanClause.Occur.SHOULD);
//        System.out.println("查询条件" + bq);
//        printDoc(is, bq);
    }

    /*
    *   解析查询
    *   第一种
    * */
    @Test
    public void queryIndex3() throws IOException, ParseException {
        IndexSearcher is = getDirReader();
        IKAnalyzer ik = new IKAnalyzer();
        // 创建查询解析对象
        QueryParser parser = new QueryParser("content", ik);
        // 解析查询对象(换言之就是根据如下这句话解析出来后，查询在fileName这个域中的内容)
        Query query = parser.parse("我在学习全文检索技术Lucene");
        System.out.println("打印查询条件" + query);
        printDoc(is, query);
    }

    /*
     *   解析查询
     *   第二种
     * */
    @Test
    public void queryIndex4() throws IOException, ParseException {
        IndexSearcher is = getDirReader();
        IKAnalyzer ik = new IKAnalyzer();
        // 创建查询解析对象
        QueryParser parser = new QueryParser("content", ik);
        // 自己写查询对象条件 AND OR || !
        Query query = parser.parse("content: 我 AND 你是 ! 好的");
        System.out.println("打印查询条件" + query);
        printDoc(is, query);
    }

    /*
     *   多域条件解析查询
     * */
    @Test
    public void multiFieldQuery() throws IOException, ParseException {
        IndexSearcher is = getDirReader();
        IKAnalyzer ik = new IKAnalyzer();

        String[] fields = {"fileName", "content"};
        MultiFieldQueryParser parser = new MultiFieldQueryParser(fields, ik);
        Query query = parser.parse("我在学习全文检索技术Lucene");

        System.out.println("打印查询条件" + query);
        printDoc(is, query);
    }

    public IndexSearcher getDirReader() throws IOException {
        Path path = Paths.get("D:\\个人文件\\java后端\\Lucene_Demo\\day01\\index_loc");
        FSDirectory open = FSDirectory.open(path);
        // 创建索引的读取对象
        DirectoryReader reader = DirectoryReader.open(open);
        // 创建索引库的所有对象
        IndexSearcher is = new IndexSearcher(reader);
        return is;
    }

    // 打印结果
    public static void printDoc(IndexSearcher is, Query tq) throws IOException {
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
            System.out.println("路径:" + path1);
            System.out.println("-------------------------");
        }
    }
}
