package com.github.seratch.scalikesolr.util;

import com.github.seratch.scalikesolr.SolrDocument;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;

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
		String csvString = "id,cat,name,price,page_i,sequence_i\n0553573403,book,A Game of Thrones,7.99,32,1\n";
		List<SolrDocument> docs = UpdateFormatLoader.fromCSVStringInJava(csvString);
		for (SolrDocument doc : docs) {
			Book book = doc.bindInJava(Book.class);
			assertThat(book.id, is(equalTo("0553573403")));
			assertThat(book.cat, is(notNullValue()));
			assertThat(book.name, is(equalTo("A Game of Thrones")));
			assertThat(book.price, is(equalTo(7.99)));
			assertThat(book.pageI.getValue(), is(equalTo(new PageI("32").getValue())));
			assertThat(book.sequenceI, is(equalTo(1)));
			log.debug(book.id); // "978-1423103349"
			log.debug(book.cat.toString()); // ["book", "paperback"]
			log.debug(book.name); // "A Game of Thrones"
			log.debug(book.price.toString()); // 7.99
			log.debug(book.pageI.getValue()); // "32"
			log.debug(book.sequenceI.toString()); // 1
		}
	}

}
