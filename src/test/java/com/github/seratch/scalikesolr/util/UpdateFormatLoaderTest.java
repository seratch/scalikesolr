package com.github.seratch.scalikesolr.util;

import com.github.seratch.scalikesolr.SolrDocument;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class UpdateFormatLoaderTest {

    Logger log = LoggerFactory.getLogger(this.getClass());

    @Test
    public void fromXMLString() throws Exception {
        String xmlString = "<add><doc><field name=\"employeeId\">05991</field><field name=\"office\">Bridgewater</field><field name=\"skills\">Perl</field><field name=\"skills\">Java</field></doc><doc><field name=\"employeeId\">05992</field><field name=\"office\">Charles</field><field name=\"skills\">Ruby</field></doc></add>";
        List<SolrDocument> docs = UpdateFormatLoader.fromXMLStringInJava(xmlString);
        for (SolrDocument doc : docs) {
            log.debug(doc.toString());
        }
    }

    @Test
    public void fromCSVString() throws Exception {
        String csvString = "id,cat,name,price,inStock,author_t,series_t,sequence_i,genre_s\r\n0553573403,book,A Game of Thrones,7.99,true,George R.R. Martin,\"A Song of Ice and Fire\",1,fantasy\r\n0553579908,book,A Clash of Kings,7.99,true,George R.R. Martin,\"A Song of Ice and Fire\",2,fantasy\n";
        List<SolrDocument> docs = UpdateFormatLoader.fromCSVStringInJava(csvString);
        for (SolrDocument doc : docs) {
            log.debug(doc.toString());
        }
    }

}
