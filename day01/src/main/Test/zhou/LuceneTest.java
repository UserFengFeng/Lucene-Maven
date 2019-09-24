package zhou;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queries.function.valuesource.LongFieldSource;
import org.apache.lucene.store.FSDirectory;
import org.junit.Before;
import org.junit.Test;

import java.awt.*;
import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.apache.lucene.document.Field.Store.YES;

public class LuceneTest {
    @Before
    public void setUp() throws Exception{
    }

    /*
    *   导入索引
    * */
    @Test
    public void importIndex() throws IOException {
        // 获得索引库的位置
        // 项目路径下创建索引库的文件夹index_loc
        Path path = Paths.get("D:\\个人文件\\java后端\\Lucene_Demo\\day01\\index_loc");
        //  打开索引库
        FSDirectory dir = FSDirectory.open(path);
        // 创建分词器
        Analyzer al = new StandardAnalyzer();
        // 创建索引的写入的配置对象
        IndexWriterConfig iwc = new IndexWriterConfig(al);
        // 创建索引的Writer
        IndexWriter iw = new IndexWriter(dir, iwc);
        /*
        * 采集原始文档
        * 创建searchsource文件，放入原始文档文件
        * */
        File sourceFile = new File("D:\\个人文件\\java后端\\Lucene_Demo\\day01\\searchsource");
        // 获得文件夹下的所有文件
        File[] files = sourceFile.listFiles();
        // 遍历每一个文件
        for(File file : files) {
            // 获得file的属性
            String fileName = file.getName();

            FileInputStream inputStream = new FileInputStream(file);
            InputStreamReader streamReader = new InputStreamReader(inputStream);
            BufferedReader reader  = new BufferedReader(streamReader);
            String line;
            // StringBuilder builder = new StringBuilder();
            String content = null;
            while ((line =reader.readLine()) != null) {
                //  builder.append(line);
                content += line;
            }
            reader.close();
            inputStream.close();
            String path1 = file.getPath();

            //  StringField不分词
            Field fName = new StringField("fileName", fileName, YES);
            Field fcontent = new TextField("content", content, YES);
            Field fsize = new TextField("size", "1024", YES);
            Field fpath = new TextField("path", path1, YES);
            // 创建文档对象
            Document document = new Document();
            // 把域加入到文档中
            document.add(fName);
            document.add(fcontent);
            document.add(fsize);
            document.add(fpath);
            // 把文档写入到索引库
            iw.addDocument(document);
        }
        // 提交
        iw.commit();
        iw.close();
    }
}
