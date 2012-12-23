package testutil

import com.github.seratch.scalikesolr.request.common.WriterType
import com.github.seratch.scalikesolr.request.UpdateRequest
import com.github.seratch.scalikesolr.{ SolrDocumentValue, SolrDocument, Solr }
import java.net.URL
import com.github.seratch.scalikesolr.util.Log
import org.slf4j.LoggerFactory

trait PreparingDocuments {

  private val logger4PreparingDocuments = new Log(LoggerFactory.getLogger(classOf[PreparingDocuments]))

  def updateDocuments() {
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
    client.doUpdateDocuments(request)
    client.doCommit(new UpdateRequest())
  }

  logger4PreparingDocuments.info("updating docuemnts...")
  updateDocuments()
  Thread.sleep(300L)
  logger4PreparingDocuments.info("updating docuemnts... Done.")

}
