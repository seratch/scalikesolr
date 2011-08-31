package com.github.seratch.scalikesolr.util

import org.slf4j.LoggerFactory
import com.github.seratch.scalikesolr.request.common.WriterType
import com.github.seratch.scalikesolr.{Solr, SolrDocument}
import java.net.URL
import com.github.seratch.scalikesolr.request.{QueryRequest, UpdateRequest, AddRequest}
import com.github.seratch.scalikesolr.request.query.Query
import org.joda.time.DateTime

case class PageI(val value: String = "")

case class Book(
                 var id: String = "",
                 var cat: List[String] = Nil,
                 var name: String = "",
                 var price: Double = 0.0,
                 var pageI: PageI = PageI(),
                 var sequenceI: Int = 0,
                 var lastModified: DateTime) {

  private var secret: String = ""

  def this() = {
    this ("", Nil, "", 0.0, PageI(), 0, new DateTime())
  }
}

case class Item(name: String)

case class Person(var name: String = "",
                  var age: Int = 0,
                  var birthDay: DateTime = null,
                  var items: List[Item] = Nil) {
  def this() = {
    this ("", 0, null, Nil)
  }
}

import org.junit.runner.RunWith
import org.specs._
import org.specs.runner.{JUnitSuiteRunner, JUnit}

@RunWith(classOf[JUnitSuiteRunner])
class TypeBinderSpec extends Specification with JUnit {

  val log = LoggerFactory.getLogger("com.github.seratch.scalikesolr.util.TypeBinderSpec")

  "bind" should {
    "be available" in {
      val csvString = "id,cat,name,price,page_i,sequence_i\n0553573403,book,A Game of Thrones,7.99,32,1\n"
      val docs = UpdateFormatLoader.fromCSVString(csvString)
      docs foreach {
        case doc => {
          val book = TypeBinder.bind(doc, classOf[Book])
          log.debug(book.toString)
          book.id must beEqual("0553573403")
          book.cat.size must beEqual(1)
          book.name must beEqual("A Game of Thrones")
          book.price must beEqual(7.99)
          book.pageI must beEqual(PageI("32"))
          book.sequenceI must beEqual(1)
          val book2 = doc.bind(classOf[Book])
          log.debug(book.toString)
        }
      }
    }
    "be available" in {
      val doc = SolrDocument
      val xmlString = "<add><doc><field name=\"name\">aaa</field><field name=\"age\">23</field><field name=\"birth_day\">2009-02-01T00:00:00Z</field><field name=\"items\">[\"bbb\",\"ccc\"]</field></doc></add>"
      val docs = UpdateFormatLoader.fromXMLString(xmlString)
      docs foreach {
        case doc => {
          val person = TypeBinder.bind(doc, classOf[Person])
          log.debug(person.toString)
          person.name must beEqual("aaa")
          person.birthDay mustNot beNull
          person.items.size must beEqual(2)
        }
      }
    }
  }

  "toSnakeCase" should {
    "be available" in {
      TypeBinder.toSnakeCase("inTheNameOfLove") must beEqual("in_the_name_of_love")
    }
  }

  val client = Solr.httpServer(new URL("http://localhost:8983/solr")).newClient()

  "DateTime mapping" should {
    "be available" in {

      // Arrange
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
         "pages_i" : 384,
         "last_modified" : "2006-08-13T11:43:14.000Z"
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
          "series_t" : "Percy Jackson and the Olympians",
          "sequence_i" : 2,
          "genre_s" : "fantasy",
          "inStock" : true,
          "price" : 6.49,
          "pages_i" : 304,
          "last_modified" : "2010-12-31T23:59:59.345Z"
       }
      """
      )
      request.documents = List(doc1, doc2)
      val response = client.doAddDocuments(request)
      client.doCommit(new UpdateRequest())
      log.debug(response.toString)
      response.responseHeader.status must greaterThanOrEqualTo(0)
      response.responseHeader.qTime must greaterThanOrEqualTo(0)

      val query = new QueryRequest(Query.as("id:978-1423103349"))
      val queryResponse = client.doQuery(query)
      queryResponse.response.documents foreach {
        case doc => {
          // Act
          val book: Book = TypeBinder.bind(doc, classOf[Book])
          log.debug(book.toString)
          // Assert
          book.id must beEqual("978-1423103349")
          val lastModified = new DateTime("2010-12-31T23:59:59.345Z")
          book.lastModified must beEqual(lastModified)
        }
      }
    }
  }

}