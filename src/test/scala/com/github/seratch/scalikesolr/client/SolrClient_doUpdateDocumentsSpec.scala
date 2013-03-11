package com.github.seratch.scalikesolr.client

import com.github.seratch.scalikesolr.request.common.WriterType
import com.github.seratch.scalikesolr.request.UpdateRequest
import java.net.URL
import com.github.seratch.scalikesolr.{ SolrDocumentValue, Solr, SolrDocument }
import org.slf4j.LoggerFactory
import org.scalatest.FlatSpec
import org.scalatest.matchers.ShouldMatchers

class SolrClient_doUpdateDocumentsSpec extends FlatSpec with ShouldMatchers {

  behavior of "SolrClient#doUpdateDocuments"

  val log = LoggerFactory.getLogger("com.github.seratch.scalikesolr.SolrClientSpec")
  val client = Solr.httpServer(new URL("http://localhost:8983/solr")).newClient()

  it should "be available" in {
    val client = Solr.httpServer(new URL("http://localhost:8983/solr")).newClient()
    val request = new UpdateRequest()
    val doc1 = SolrDocument(
      writerType = WriterType.Standard,
      map = Map(
        "id" -> SolrDocumentValue("978-0641723445"),
        "cat" -> SolrDocumentValue("List(book, hardcover)"),
        "title" -> SolrDocumentValue("The Lightning Thief"),
        "author" -> SolrDocumentValue("Rick Riordan"),
        "series_t" -> SolrDocumentValue("Percy Jackson and the Olympians"),
        "sequence_i" -> SolrDocumentValue("1"),
        "genre_s" -> SolrDocumentValue("fantasy"),
        "inStock" -> SolrDocumentValue("true"),
        "price" -> SolrDocumentValue("12.50"),
        "pages_i" -> SolrDocumentValue("384"),
        "timestamp" -> SolrDocumentValue("2006-03-21T13:40:15.518Z")
      )
    )
    val doc2 = SolrDocument(
      writerType = WriterType.Standard,
      map = Map(
        "id" -> SolrDocumentValue("978-1423103349"),
        "cat" -> SolrDocumentValue("List(book, paperback)"),
        "title" -> SolrDocumentValue("The Sea of Monsters"),
        "author" -> SolrDocumentValue("Rick Riordan"),
        "series_t" -> SolrDocumentValue("Percy Jackson and the Olympians"),
        "sequence_i" -> SolrDocumentValue("2"),
        "genre_s" -> SolrDocumentValue("fantasy"),
        "inStock" -> SolrDocumentValue("true"),
        "price" -> SolrDocumentValue("6.49"),
        "pages_i" -> SolrDocumentValue("304"),
        "timestamp" -> SolrDocumentValue("2006-03-21T13:40:15.518Z")
      )

    )
    request.documents = List(doc1, doc2)
    val response = client.doUpdateDocuments(request)
    client.doCommit(new UpdateRequest())

    response should not be null
    response.responseHeader.status should equal(0)
    response.responseHeader.qTime should be > 0
    response.rawBody.replaceAll("\r", "").replaceAll("\n", "").trim should fullyMatch regex """<\?xml version="1.0" encoding="UTF-8"\?>
                                                                                              |<response>
                                                                                              |<lst name="responseHeader"><int name="status">0</int><int name="QTime">\d+</int></lst>
                                                                                              |</response>
                                                                                              | """.stripMargin.replaceAll("\r", "").replaceAll("\n", "").trim
  }

  it should "send updateInCSV" in {
    val client = Solr.httpServer(new URL("http://localhost:8983/solr")).newClient()
    val request = new UpdateRequest(requestBody =
      """id,cat,name,price,inStock,author,series_t,sequence_i,genre_s
        |0553573403,book,A Game of Thrones,7.99,true,George R.R. Martin,"A Song of Ice and Fire",1,fantasy
        |0553579908,book,A Clash of Kings,7.99,true,George R.R. Martin,"A Song of Ice and Fire",2,fantasy
        |055357342X,book,A Storm of Swords,7.99,true,George R.R. Martin,"A Song of Ice and Fire",3,fantasy
        |0553293354,book,Foundation,7.99,true,Isaac Asimov,Foundation Novels,1,scifi
        |0812521390,book,The Black Company,6.99,false,Glen Cook,The Chronicles of The Black Company,1,fantasy
        |0812550706,book,Ender's Game,6.99,true,Orson Scott Card,Ender,1,scifi
        |0441385532,book,Jhereg,7.95,false,Steven Brust,Vlad Taltos,1,fantasy
        |0380014300,book,Nine Princes In Amber,6.99,true,Roger Zelazny,the Chronicles of Amber,1,fantasy
        |0805080481,book,The Book of Three,5.99,true,Lloyd Alexander,The Chronicles of Prydain,1,fantasy
        |080508049X,book,The Black Cauldron,5.99,true,Lloyd Alexander,The Chronicles of Prydain,2,fantasy""".stripMargin)
    val response = client.doUpdateInCSV(request)
    println(response)
    response should not be null
    response.responseHeader.status should equal(0)
    response.responseHeader.qTime should be > 0
  }

  it should "send updateInJSON" in {
    val client = Solr.httpServer(new URL("http://localhost:8983/solr")).newClient()
    val request = new UpdateRequest(requestBody =
      """[
        |  {
        |    "id" : "978-0641723445",
        |    "cat" : ["book","hardcover"],
        |    "name" : "The Lightning Thief",
        |    "author" : "Rick Riordan",
        |    "series_t" : "Percy Jackson and the Olympians",
        |    "sequence_i" : 1,
        |    "genre_s" : "fantasy",
        |    "inStock" : true,
        |    "price" : 12.50,
        |    "pages_i" : 384
        |  }
        |,
        |  {
        |    "id" : "978-1423103349",
        |    "cat" : ["book","paperback"],
        |    "name" : "The Sea of Monsters",
        |    "author" : "Rick Riordan",
        |    "series_t" : "Percy Jackson and the Olympians",
        |    "sequence_i" : 2,
        |    "genre_s" : "fantasy",
        |    "inStock" : true,
        |    "price" : 6.49,
        |    "pages_i" : 304
        |  }
        |,
        |  {
        |    "id" : "978-1857995879",
        |    "cat" : ["book","paperback"],
        |    "name" : "Sophie's World : The Greek Philosophers",
        |    "author" : "Jostein Gaarder",
        |    "sequence_i" : 1,
        |    "genre_s" : "fantasy",
        |    "inStock" : true,
        |    "price" : 3.07,
        |    "pages_i" : 64
        |  }
        |,
        |  {
        |    "id" : "978-1933988177",
        |    "cat" : ["book","paperback"],
        |    "name" : "Lucene in Action, Second Edition",
        |    "author" : "Michael McCandless",
        |    "sequence_i" : 1,
        |    "genre_s" : "IT",
        |    "inStock" : true,
        |    "price" : 30.50,
        |    "pages_i" : 475
        |  }
        |]""".stripMargin)
    val response = client.doUpdateInJSON(request)
    println(response)
    response should not be null
    response.responseHeader.status should equal(0)
    response.responseHeader.qTime should be > 0
  }

}
