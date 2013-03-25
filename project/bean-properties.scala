import sbt._
import sbt.Keys._
import scalag._

object ReplaceBeanPropertiesForScala29Command extends Plugin {

  ScalagPlugin.addCommand(
    namespace = "scala2.9",
    args = Nil,
    description = "Replace BeanProperty for Scala 2.9",
    operation = { case ScalagInput(Nil, settings) => 
      Seq(
        "src/main/scala/com/github/seratch/scalikesolr/HttpSolrClient.scala",
        "src/main/scala/com/github/seratch/scalikesolr/SolrCore.scala",
        "src/main/scala/com/github/seratch/scalikesolr/SolrDocument.scala",
        "src/main/scala/com/github/seratch/scalikesolr/SolrDocumentValue.scala",
        "src/main/scala/com/github/seratch/scalikesolr/http/HttpClient.scala",
        "src/main/scala/com/github/seratch/scalikesolr/request/DIHCommandRequest.scala",
        "src/main/scala/com/github/seratch/scalikesolr/request/DeleteRequest.scala",
        "src/main/scala/com/github/seratch/scalikesolr/request/PingRequest.scala",
        "src/main/scala/com/github/seratch/scalikesolr/request/UpdateRequest.scala",
        "src/main/scala/com/github/seratch/scalikesolr/request/common/WriterType.scala",
        "src/main/scala/com/github/seratch/scalikesolr/request/query/EchoParams.scala",
        "src/main/scala/com/github/seratch/scalikesolr/request/query/ExplainOther.scala",
        "src/main/scala/com/github/seratch/scalikesolr/request/query/FieldsToReturn.scala",
        "src/main/scala/com/github/seratch/scalikesolr/request/query/FilterQuery.scala",
        "src/main/scala/com/github/seratch/scalikesolr/request/query/IsDebugQueryEnabled.scala",
        "src/main/scala/com/github/seratch/scalikesolr/request/query/IsEchoHandlerEnabled.scala",
        "src/main/scala/com/github/seratch/scalikesolr/request/query/IsIndentEnabled.scala",
        "src/main/scala/com/github/seratch/scalikesolr/request/query/IsOmitHeaderEnabled.scala",
        "src/main/scala/com/github/seratch/scalikesolr/request/query/MaximumRowsReturned.scala",
        "src/main/scala/com/github/seratch/scalikesolr/request/query/Query.scala",
        "src/main/scala/com/github/seratch/scalikesolr/request/query/QueryParserType.scala",
        "src/main/scala/com/github/seratch/scalikesolr/request/query/QueryType.scala",
        "src/main/scala/com/github/seratch/scalikesolr/request/query/Sort.scala",
        "src/main/scala/com/github/seratch/scalikesolr/request/query/StartRow.scala",
        "src/main/scala/com/github/seratch/scalikesolr/request/query/AllowedMilliseconds.scala",
        "src/main/scala/com/github/seratch/scalikesolr/request/query/Version.scala",
        "src/main/scala/com/github/seratch/scalikesolr/request/query/facet/FacetParams.scala",
        "src/main/scala/com/github/seratch/scalikesolr/request/query/group/GroupParams.scala",
        "src/main/scala/com/github/seratch/scalikesolr/request/query/highlighting/HighlightingParams.scala",
        "src/main/scala/com/github/seratch/scalikesolr/request/query/morelikethis/MoreLikeThisParams.scala",
        "src/main/scala/com/github/seratch/scalikesolr/response/DIHCommandResponse.scala",
        "src/main/scala/com/github/seratch/scalikesolr/response/DeleteResponse.scala",
        "src/main/scala/com/github/seratch/scalikesolr/response/PingResponse.scala",
        "src/main/scala/com/github/seratch/scalikesolr/response/QueryResponse.scala",
        "src/main/scala/com/github/seratch/scalikesolr/response/UpdateResponse.scala",
        "src/main/scala/com/github/seratch/scalikesolr/response/common/ResponseHeader.scala",
        "src/main/scala/com/github/seratch/scalikesolr/response/dih/InitArgs.scala",
        "src/main/scala/com/github/seratch/scalikesolr/response/dih/StatusMessages.scala",
        "src/main/scala/com/github/seratch/scalikesolr/response/query/Facet.scala",
        "src/main/scala/com/github/seratch/scalikesolr/response/query/Groups.scala",
        "src/main/scala/com/github/seratch/scalikesolr/response/query/Highlightings.scala",
        "src/main/scala/com/github/seratch/scalikesolr/response/query/MoreLikeThis.scala",
        "src/main/scala/com/github/seratch/scalikesolr/response/query/Response.scala"
      ).foreach { path =>
        val filePath = FilePath(path)
        filePath.forceWrite(filePath.readAsString().replaceFirst("beans.BeanProperty", "reflect.BeanProperty"))
      }
    }
  )

}


// vim: set ts=4 sw=4 et:
