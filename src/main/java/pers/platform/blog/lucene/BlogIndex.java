package pers.platform.blog.lucene;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.cn.smart.SmartChineseAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.*;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.*;
import org.apache.lucene.search.highlight.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import pers.platform.blog.model.Blog;
import pers.platform.blog.utils.DateUtil;
import pers.platform.blog.utils.StringUtil;

import java.io.StringReader;
import java.nio.file.Paths;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;


/**
 * 博客索引类
 * 
 * @author Administrator
 *
 */
@Component
public class BlogIndex {

    private Directory dir;

    @Value("${lucene.url}")
    private String lucuneUrl;

    private IndexWriter getWriter() throws Exception {
        dir = FSDirectory.open(Paths.get(lucuneUrl));
        SmartChineseAnalyzer analyzer = new SmartChineseAnalyzer();
        IndexWriterConfig iwc = new IndexWriterConfig(analyzer);
        IndexWriter writer = new IndexWriter(dir, iwc);
        return writer;
    }

    /**
     * 添加博客索引
     * 
     * @param blog
     * @throws Exception
     */
    public void addIndex(Blog blog) throws Exception {
        IndexWriter writer = getWriter();
        Document doc = new Document();
        doc.add(new StringField("id", String.valueOf(blog.getId()),
                Field.Store.YES));
        doc.add(new TextField("title", blog.getTitle(), Field.Store.YES));
        doc.add(new StringField("releaseDate",
                DateUtil.formatDate(new Date(), "yyyy-MM-dd"),
                Field.Store.YES));
        doc.add(new TextField("content", blog.getContentNoTag(),
                Field.Store.YES));
        writer.addDocument(doc);
        writer.close();
    }

    public void updateIndex(Blog blog) throws Exception {
        IndexWriter writer = getWriter();
        Document doc = new Document();
        doc.add(new StringField("id", String.valueOf(blog.getId()),
                Field.Store.YES));
        doc.add(new TextField("title", blog.getTitle(), Field.Store.YES));
        doc.add(new StringField("releaseDate",
                DateUtil.formatDate(new Date(), "yyyy-MM-dd"),
                Field.Store.YES));
        doc.add(new TextField("content", blog.getContentNoTag(),
                Field.Store.YES));
        writer.updateDocument(new Term("id", String.valueOf(blog.getId())),
                doc);
        writer.close();
    }

    /**
     * 查询博客信息
     * 
     * @param q
     * @return
     * @throws Exception
     */
    public List<Blog> searchBlog(String q) throws Exception {
        dir = FSDirectory.open(Paths.get(lucuneUrl));
        IndexReader reader = DirectoryReader.open(dir);
        IndexSearcher is = new IndexSearcher(reader);
        BooleanQuery.Builder booleQuery = new BooleanQuery.Builder();
        SmartChineseAnalyzer analyzer = new SmartChineseAnalyzer();
        QueryParser parser = new QueryParser("title", analyzer);
        Query query = parser.parse(q);

        QueryParser parser2 = new QueryParser("content", analyzer);
        Query query2 = parser2.parse(q);

        booleQuery.add(query, BooleanClause.Occur.SHOULD);
        booleQuery.add(query2, BooleanClause.Occur.SHOULD);

        TopDocs hits = is.search(booleQuery.build(), 100);
        QueryScorer scorer = new QueryScorer(query);
        Fragmenter fragmenter = new SimpleSpanFragmenter(scorer);
        SimpleHTMLFormatter simpleHTMLFormatter = new SimpleHTMLFormatter(
                "<b><font color='red'>", "</font></b>");
        Highlighter highlighter = new Highlighter(simpleHTMLFormatter, scorer);
        highlighter.setTextFragmenter(fragmenter);

        List<Blog> blogList = new LinkedList<>();
        for (ScoreDoc scoreDoc : hits.scoreDocs) {
            Document doc = is.doc(scoreDoc.doc);
            Blog blog = new Blog();
            blog.setId(doc.get("id"));
            blog.setKeyWord(doc.get("keyWord"));
            blog.setReleaseDateStr(doc.get("releaseDate"));
            String title = doc.get("title");
            String content = StringEscapeUtils.escapeHtml4(doc.get("content"));
            if (title != null) {
                TokenStream tokenStream = analyzer.tokenStream("title",
                        new StringReader(title));
                String hTitle = highlighter.getBestFragment(tokenStream, title);
                if (StringUtil.isNotEmpty(hTitle)) {
                    blog.setTitle(hTitle);
                } else {
                    blog.setTitle(title);
                }
            }
            if (content != null) {
                TokenStream tokenStream = analyzer.tokenStream("content",
                        new StringReader(content));
                String hContent = highlighter.getBestFragment(tokenStream,
                        content);
                if (StringUtil.isNotEmpty(hContent)) {
                    blog.setContent(hContent);
                } else {
                    if (content.length() <= 150) {
                        blog.setContent(content);
                    } else {
                        blog.setContent(content.substring(0, 150));
                    }
                }
            }
            blogList.add(blog);
        }
        return blogList;
    }

    /**
     * 删除制定博客的索引
     * 
     * @param blogId
     * @throws Exception
     */
    public void deleteindex(String blogId) throws Exception {
        IndexWriter indexWriter = getWriter();
        indexWriter.deleteDocuments(new Term("id", blogId));
        indexWriter.forceMergeDeletes(); // 强制删除
        indexWriter.commit();
        indexWriter.close();
    }

    /*
     * public static void main(String[] args) throws Exception { BlogIndex
     * blogIndex = new BlogIndex(); blogIndex.deleteindex("63"); }
     */
}
