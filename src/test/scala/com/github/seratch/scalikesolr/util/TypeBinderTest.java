package com.github.seratch.scalikesolr.util;

import com.github.seratch.scalikesolr.SolrDocument;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.scalatest.junit.JUnitRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

public class TypeBinderTest {

    Logger log = LoggerFactory.getLogger(this.getClass());

    static class PageI {

        private String value;

        public PageI(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }

    }

    public static class Book {

        private String id;
        private List<String> cat;
        private String name;
        private Double price;
        private PageI pageI;
        private Integer sequenceI;

        private void setPrivate(String arg) {
        }

        protected void setProtected(String arg) {
        }

        void sePackageLocal(String arg) {
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public List<String> getCat() {
            return cat;
        }

        public void setCat(List<String> cat) {
            this.cat = cat;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Double getPrice() {
            return price;
        }

        public void setPrice(Double price) {
            this.price = price;
        }

        public PageI getPageI() {
            return pageI;
        }

        public void setPageI(PageI pageI) {
            this.pageI = pageI;
        }

        public Integer getSequenceI() {
            return sequenceI;
        }

        public void setSequenceI(Integer sequenceI) {
            this.sequenceI = sequenceI;
        }

    }

    @Test
    public void bind() throws Exception {
        String csvString = "id,cat,name,price,page_i,sequence_i\n0553573403,book,A Game of Thrones,7.99,32,1\n,,,.,\n,,,a.b,c\n";
        List<SolrDocument> docs = UpdateFormatLoader.fromCSVStringInJava(csvString);
        Book book1 = docs.get(0).bindInJava(Book.class);
        assertThat(book1.id, is(equalTo("0553573403")));
        assertThat(book1.cat, is(notNullValue()));
        assertThat(book1.name, is(equalTo("A Game of Thrones")));
        assertThat(book1.price, is(equalTo(7.99)));
        assertThat(book1.pageI.getValue(), is(equalTo(new PageI("32").getValue())));
        assertThat(book1.sequenceI, is(equalTo(1)));
        for (SolrDocument doc : docs) {
            Book book = doc.bindInJava(Book.class);
            log.debug(book.id); // "978-1423103349"
            log.debug(book.cat.toString()); // ["book", "paperback"]
            log.debug(book.name); // "A Game of Thrones"
            log.debug(String.valueOf(book.price)); // 7.99
            log.debug(String.valueOf(book.pageI)); // "32"
            log.debug(String.valueOf(book.sequenceI)); // 1
        }
    }

}
