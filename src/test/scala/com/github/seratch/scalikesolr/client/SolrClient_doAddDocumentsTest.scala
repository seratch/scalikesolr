package com.github.seratch.scalikesolr.client

import com.github.seratch.scalikesolr.request.common.WriterType
import com.github.seratch.scalikesolr.request.{AddRequest, UpdateRequest}
import java.net.URL
import com.github.seratch.scalikesolr.{Solr, SolrDocument}
import org.slf4j.LoggerFactory
import org.junit._
import org.scalatest.Assertions

class SolrClient_doAddDocumentsTest extends Assertions {

  val log = LoggerFactory.getLogger("com.github.seratch.scalikesolr.SolrClientSpec")
  val client = Solr.httpServer(new URL("http://localhost:8983/solr")).newClient()

  @Test
  def available() {
    val request = new AddRequest()
    val doc1 = SolrDocument(
      writerType = WriterType.JSON,
      rawBody = """
      {"id" : "978-0641723445",
       "cat" : ["book","hardcover"],
       "title" : "The Lightning Thief",
       "author" : "Rick Riordan",
       "series_t" : "Percy Jackson and the Olympians",
       "sequence_i" : 1,
       "genre_s" : "fantasy",
       "inStock" : true,
       "price" : 12.50,
       "pages_i" : 384
     }
     """
    )
    val doc2 = SolrDocument(
      writerType = WriterType.JSON,
      rawBody = """
     {
        "id" : "978-1423103349",
        "cat" : ["book","paperback"],
        "title" : "The Sea of Monsters",
        "author" : "Rick Riordan",
        "series_t" : "Percy Jackson and the Olympians <script>foovar</script>",
        "sequence_i" : 2,
        "genre_s" : "fantasy",
        "inStock" : true,
        "price" : 6.49,
        "pages_i" : 304
      }
    """
    )
    request.documents = List(doc1, doc2)
    val response = client.doAddDocuments(request)
    client.doCommit(new UpdateRequest())
    log.debug(response.toString)
    assert(response.responseHeader.status >= 0)
    assert(response.responseHeader.qTime >= 0)
  }

  @Test
  def availableInJSON() {
    val request = new UpdateRequest(
      requestBody = "id,cat,name,price,inStock,author_t,series_t,sequence_i,genre_s\n0553573403,book,A Game of Thrones,7.99,true,George R.R. Martin,\"A Song of Ice and Fire\",1,fantasy\n0553579908,book,A Clash of Kings,7.99,true,George R.R. Martin,\"A Song of Ice and Fire\",2,fantasy\n055357342X,book,A Storm of Swords,7.99,true,George R.R. Martin,\"A Song of Ice and Fire\",3,fantasy\n0553293354,book,Foundation,7.99,true,Isaac Asimov,Foundation Novels,1,scifi\n0812521390,book,The Black Company,6.99,false,Glen Cook,The Chronicles of The Black Company,1,fantasy\n"
    )
    val response = client.doAddDocumentsInCSV(request)
    client.doCommit(new UpdateRequest)
    assert(response != null)
  }

}