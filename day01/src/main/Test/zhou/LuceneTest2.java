package zhou;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.queries.function.valuesource.LongFieldSource;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.store.FSDirectory;
import org.junit.Before;
import org.junit.Test;
import org.wltea.analyzer.lucene.IKAnalyzer;

import java.awt.*;
import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.apache.lucene.document.Field.Store.YES;

public class LuceneTest2 {
    @Before
    public void setUp() throws Exception {
    }

    /*
     *   导入索引
     * */
    @Test
    public void importIndex() throws IOException {
        IndexWriter iw = getIndexWriter();
        /*
         * 采集原始文档
         * 创建searchsource文件，放入原始文档文件
         * */
        File file = new File("D:\\个人文件\\java后端\\Lucene_Demo\\day01\\searchsource\\test.txt");
        String content = readFileContent(file);
        String fileName = file.getName();
        String filePath = file.getPath();
        String length = String.valueOf(file.length());
        //  StringField不分词
        Field fName = new StringField("fileName", fileName, YES);
        Field fcontent = new TextField("content", content, YES);
        Field fsize = new TextField("size", length, YES);
        Field fpath = new TextField("path", filePath, YES);
        // 创建文档对象
        Document document = new Document();
        // 把域加入到文档中
        document.add(fName);
        document.add(fcontent);
        document.add(fsize);
        document.add(fpath);
        // 把文档写入到索引库
        iw.addDocument(document);
        // 提交
        iw.commit();
        iw.close();
    }

    @Test
    public void deleteIndex() throws IOException {
        IndexWriter iw = getIndexWriter();
        iw.deleteAll();
        iw.commit();
        iw.close();
    }

    @Test
    public void deleteIndexByQuery() throws IOException {
        IndexWriter iw = getIndexWriter();
        // 创建语汇单元项
        Term term = new Term("content", "三");
        // 创建根据语汇单元的查询对象
        TermQuery query = new TermQuery(term);
        iw.deleteDocuments(query);
        iw.commit();
        iw.close();
    }

    public IndexWriter getIndexWriter() throws IOException {
        // 获得索引库的位置
        // 项目路径下创建索引库的文件夹index_loc
        Path path = Paths.get("D:\\个人文件\\java后端\\Lucene_Demo\\day01\\index_loc");
        //  打开索引库
        FSDirectory dir = FSDirectory.open(path);
        // 创建分词器
        Analyzer al = new IKAnalyzer();
        // 创建索引的写入的配置对象
        IndexWriterConfig iwc = new IndexWriterConfig(al);
        // 创建索引的Writer
        IndexWriter iw = new IndexWriter(dir, iwc);
        return iw;
    }

    //  获取文件内容
    public String readFileContent(File file) {
        BufferedReader reader = null;
        StringBuffer sbf = new StringBuffer();
        try {
            reader = new BufferedReader(new FileReader(file));
            String tempStr;
            while ((tempStr = reader.readLine()) != null) {
                sbf.append(tempStr);
            }
            reader.close();
            return sbf.toString();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
        return sbf.toString();
    }
}
