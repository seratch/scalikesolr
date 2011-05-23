package com.github.seratch.scalikesolr.util

import org.specs.Specification
import org.slf4j.LoggerFactory
import com.github.seratch.scalikesolr.SolrDocument
import org.joda.time.DateTime

case class PageI(val value: String = "")

case class Book(
                 var id: String = "",
                 var cat: List[String] = Nil,
                 var name: String = "",
                 var price: Double = 0.0,
                 var pageI: PageI = PageI(),
                 var sequenceI: Int = 0) {

  private var secret: String = ""

  def this() = {
    this ("", Nil, "", 0.0, PageI(), 0)
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

object TypeBinderSpec extends Specification {

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
}