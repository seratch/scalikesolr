# scalikesolr : Apache Solr Client for Scala/Java

"scalikesolr" is a simple Solr client library for Scala. And it also works fine with Java.

Currently following Scala versions are supported:

### Scala 2.8.1.final

    <dependency>
      <groupId>com.github.seratch</groupId>
      <artifactId>scalikesolr_2.8.1</artifactId>
      <version>[3.4,)</version>
    </dependency>

### Scala 2.9.0.final

    <dependency>
      <groupId>com.github.seratch</groupId>
      <artifactId>scalikesolr_2.9.0</artifactId>
      <version>[3.4,)</version>
    </dependency>

### Scala 2.9.0.1

    <dependency>
      <groupId>com.github.seratch</groupId>
      <artifactId>scalikesolr_2.9.0-1</artifactId>
      <version>[3.4,)</version>
    </dependency>

### Scala 2.9.1.final

    <dependency>
      <groupId>com.github.seratch</groupId>
      <artifactId>scalikesolr_2.9.1</artifactId>
      <version>[3.4,)</version>
    </dependency>

## How to install

### sbt

Example of "project/build/MyProject.scala":

    val ScalikeSolrClientLibraryReleases = "Scalike Solr Client Library Releases Repository" at "https://github.com/seratch/scalikesolr/raw/master/mvn-repo/releases"
    val scalikesolr = "com.github.seratch" %% "scalikesolr" % "3.4.0" withSources ()

### Maven

"3.1","3.2","3.3","3.4" means Solr/Lucene release version that is supported by scalikesolr. The "x" of "3.4.x" will be incremented.

    <repositories>
      <repository>
        <id>ScalikeSolrClientLibraryReleases</id>
        <url>https://github.com/seratch/scalikesolr/raw/master/mvn-repo/releases</url>
      </repository>
      <repository>
        <id>ScalikeSolrClientLibrarySnapshots</id>
        <url>https://github.com/seratch/scalikesolr/raw/master/mvn-repo/snapshots</url>
      </repository>
    </repositories>

    <dependency>
      <groupId>com.github.seratch</groupId>
      <artifactId>scalikesolr_2.9.1</artifactId>
      <version>[3.4,)</version>
    </dependency>

## Usage

The following snippets use the indexed data from "example/exampledocs/books.json".

    {
      "id" : "978-0641723445",
      "cat" : ["book","hardcover"],
      "title" : "The Lightning Thief",
      "author" : "Rick Riordan",
      "series_t" : "Percy Jackson and the Olympians",
      "sequence_i" : 1,
      "genre_s" : "fantasy",
      "inStock" : true,
      "price" : 12.50,
      "pages_i" : 384
    },
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
      "pages_i" : 304
    }

### Query

#### Simple Query

Using [Core Query Paramters](http://wiki.apache.org/solr/CoreQueryParameters) and [Common Query Parameters](http://wiki.apache.org/solr/CommonQueryParameters):

    import com.github.seratch.scalikesolr._

    val client = Solr.httpServer(new URL("http://localhost:8983/solr")).newClient
    val request = new QueryRequest(writerType = WriterType.JavaBinary, query = Query("author:Rick")) // faster when using WriterType.JavaBinary
    val response = client.doQuery(request)
    println(response.responseHeader)
    println(response.response)
    response.response.documents foreach {
      case doc => {
        println(doc.get("id").toString()) // "978-1423103349"
        println(doc.get("cat").toListOrElse(Nil).toString) // List(book, hardcover)
        println(doc.get("title").toString()) // "The Sea of Monsters"
        println(doc.get("pages_i").toIntOrElse(0).toString) // 304
        println(doc.get("price").toDoubleOrElse(0.0).toString) // 6.49
      }
    }

#### Bind from SolrDocument

It requires no-argument constructor and setters for fields.
It is also possible to specify user-defined type that has one argument(String) constructor.

    case class PageI(val value: String = "")
    case class Book(
      var id: String = "",
      var cat: List[String] = Nil,
      var price: Double = 0.0,
      var pageI: PageI = PageI(),
      var sequenceI: Int = 0
    ) {
      def this() = {
        this ("", Nil, 0.0, PageI(), 0)
      }
    }
    val book = doc.bind(classOf[Book])
    println(book.id) // "978-1423103349"
    println(book.cat.size) // 2
    println(book.price) // 6.49
    println(book.pageI.value) // 304
    println(book.sequenceI) // 2

#### With Highlightings

Using [Highlighting Parameters](http://wiki.apache.org/solr/HighlightingParameters):

    val request = new QueryRequest(
      writerType = WriterType.JSON, // but JSON format might be slow...
      query = Query("author:Rick"),
      sort = Sort("page_i desc")
    )
    request.highlighting = HighlightingParams(true)
    val response = client.doQuery(request)
    println(response.highlightings)
    response.highlightings.keys foreach {
      case key => {
        println(key + " -> " + response.highlightings.get(key).get("author").toString)
        // "978-0641723445" -> "<em>Rick</em> Riordan"
      }
    }

#### With MoreLikeThis

Using [More Like This](http://wiki.apache.org/solr/MoreLikeThis):

    val request = new QueryRequest(Query("author:Rick"))
    request.moreLikeThis = MoreLikeThisParams(
      enabled = true,
      count = 3,
      fieldsToUseForSimilarity = FieldsToUseForSimilarity("body")
    )
    val response = client.doQuery(request)
    println(response.moreLikeThis)
    response.response.documents foreach {
      doc => {
        val id = doc.get("id").toString
        response.moreLikeThis.getList(id) foreach {
          case recommendation => {
            println(recommendation) // "SolrDocument(WriterType(standard),,Map(start -> 0, numFound -> 0))"
          }
        }
      }
    }

#### With FacetQuery

Using [Simple Facet Parameters](http://wiki.apache.org/solr/SimpleFacetParameters):

    val request = new QueryRequest(Query("author:Rick"))
    request.facet = new FacetParams(
      enabled = true,
      params = List(new FacetParam(Param("facet.field"), Value("title")))
    )
    val response = client.doQuery(request)
    println(response.facet.facetFields)
    response.facet.facetFields.keys foreach {
      case key => {
        val facets = response.facet.facetFields.getOrElse(key, new SolrDocument())
        facets.keys foreach {
          case facetKey => println(facetKey + " -> " + facets.get(facetKey).toIntOrElse(0))
          // "thief" -> 1, "sea" -> 1, "monster" -> 1, "lightn" -> 1
        }
      }
    }

#### With Result Groupiong / Field Collapsing

Using [Result Groupiong / Field Collapsing](http://wiki.apache.org/solr/FieldCollapsing):

    val request = new QueryRequest(Query("genre_s:fantasy"))
    request.group = new GroupParams(
      enabled = true,
      field = Field("author_t")
    )
    val response = client.doQuery(request)
    println(response.groups.toString)
    response.groups.groups foreach {
      case group => println(group.groupValue + " -> " + group.documents.toString)
      // "r.r" -> List(SolrDocument(...
      // "glen" -> List(SolrDocument(...
    }

#### Distributed Search

Using [Distributed Search](http://wiki.apache.org/solr/DistributedSearch):

    val request = new QueryRequest(Query("genre_s:fantasy"))
    request.shards = new DistributedSearchParams(
      shards = List(
        "localhost:8984/solr",
        "localhost:8985/solr"
      )
    )
    val response = client.doQuery(request)
    println(response.groups.toString)

### DIH Command

Commands for [Data Import Handler](http://wiki.apache.org/solr/DataImportHandler):

    val request = new DIHCommandRequest(command = "delta-import")
    val response = client.doDIHCommand(request)
    println(response.initArgs)
    println(response.command)
    println(response.status)
    println(response.importResponse)
    println(response.statusMessages)

### Update

[XML Messages for Updating a Solr Index](http://wiki.apache.org/solr/UpdateXmlMessages):

#### Add documents

Add documents to Solr:

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
       {"id" : "978-1423103349",
        "cat" : ["book","paperback"],
        "title" : "The Sea of Monsters",
        "author" : "Rick Riordan",
        "series_t" : "Percy Jackson and the Olympians",
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
     client.doCommit(new UpdateRequest)

#### Delete documents

     val request = new DeleteRequest(uniqueKeysToDelete = List("978-0641723445"))
     val response = client.doDeleteDocuments(request)
     client.doCommit(new UpdateRequest)

#### Commit

     val response = client.doCommit(new UpdateRequest())

#### Rollback

     val response = client.doRollback(new UpdateRequest())

#### Optimize

     val response = client.doOptimize(new UpdateRequest())

#### Add documents in CSV format

     val request = new UpdateRequest(
       requestBody = "id,name,sequence_i\n0553573403,A Game of Thrones,1\n..."
     )
     val response = client.doAddDocumentsInCSV(request)
     client.doCommit(new UpdateRequest)

#### Update in XML format

     val request = new UpdateRequest(
       requestBody = "<optimize/>"
     )
     val response = client.doUpdateInXML(request)

#### Update in JSON format

     val request = new UpdateRequest(
       writerType = WriterType.JSON,
       requestBody = "{ \"optimize\": { \"waitFlush\":false, \"waitSearcher\":false } }"
     )
     val response = client.doUpdateInJSON(request)

### Load documents from Update format

JSON format is not supported.

#### [XML format](http://wiki.apache.org/solr/UpdateXmlMessages)

     val xmlString = "<add><doc><field name=\"employeeId\">05991</field><field name=\"office\">Bridgewater</field>..."
     val docs = UpdateFormatLoader.fromXMLString(xmlString)
     docs foreach {
       case doc => {
         println("employeeId:" + doc.get("employeeId").toString()) // "05991"
         println("office:" + doc.get("office").toString()) // "Bridgewater"
       }
     }

#### [CSV format](http://wiki.apache.org/solr/UpdateCSV)

     val csvString = "id,name,sequence_i\n0553573403,A Game of Thrones,1\n..."
     val docs = UpdateFormatLoader.fromCSVString(csvString)
     docs foreach {
       case doc => {
         println(doc.get("id")) // "0553573403"
         println(doc.get("name")) // "A Game of Thrones"
         println(doc.get("sequence_i").toIntOrElse(0)) // 1
       }
     }

### Ping

     val response = client.doPing(new PingRequest())
     println(response.status) // "OK"

### How to know which type is the param mapped?

Name of constructor arg is same as the Solr parameter key.

For example:

    case class Query(@BeanProperty val q: String) extends RequestParam

## Using in Java

This library works fine with Java.

### Query

    SolrClient client = Solr.httpServer(new URL("http://localhost:8983/solr")).getNewClient();
    QueryRequest request = new QueryRequest(Query.as("author:Rick")); // or new Query("author:Rick")
    request.setWriterType(WriterType.JSON());
    request.setSort(Sort.as("author desc"));
    request.setMoreLikeThis(MoreLikeThisParams.as(true, 3, FieldsToUseForSimilarity.as("title")));
    QueryResponse response = client.doQuery(request);
    List<SolrDocument> documents = response.getResponse().getDocumentsInJava();
    for (SolrDocument document : documents) {
        log.debug(document.get("id").toString()); // "978-1423103349"
        log.debug(document.get("cat").toListInJavaOrElse(null).toString()); // ["book", "paperback"]
        log.debug(document.get("pages_i").toNullableIntOrElse(null).toString()); // 304
        log.debug(document.get("price").toNullableDoubleOrElse(null).toString()); // 6.49
    }

#### Bind from SolrDocument

    public class PageI {
        private String value;
        public PageI(String value) { this.value = value; }
        public String getValue() { return value; }
    }

    public class Book {
        private String id;
        private List<String> cat;
        private Double price;
        private PageI pageI;
        private Integer sequenceI;
        public String getId() { return id; }
        public void setId(String id) { this.id = id; }
        // ... getter/setter
    }

    SolrDocument document = ...;
    Book book = document.bindInJava(Book.class);
    log.debug(book.getId()); // "978-1423103349"
    log.debug(book.getCat().toString()); // [book]
    log.debug(book.getPrice().toString()); // "6.49"
    log.debug(book.getPageI().getValue()); // "304"
    log.debug(book.getSequenceI().toString()); // "2"

### DIH Command

    DIHCommandRequest request = new DIHCommandRequest("delta-import");
    DIHCommandResponse response = client.doDIHCommand(request);

### Update

#### Add documements

    AddRequest request = new AddRequest();
    String jsonString = "{\"id\" : \"978-0641723445\", ... }";
    SolrDocument doc = new SolrDocument(WriterType.JSON(), jsonString);
    List<SolrDocument> docs = new ArrayList<SolrDocument>();
    docs.add(doc);
    request.setDocumentsInJava(docs);
    AddResponse response = client.doAddDocuments(request);
    client.doCommit(new UpdateRequest());

#### Delete documents

    DeleteRequest request = new DeleteRequest();
    List<String> uniqueKeys = new ArrayList<String>();
    uniqueKeys.add("978-0641723445");
    request.setUniqueKeysToDetelteInJava(uniqueKeys);
    DeleteResponse response = client.doDeleteDocuments(request);
    client.doCommit(new UpdateRequest());

#### Commit

    UpdateResponse response = client.doCommit(new UpdateRequest());

#### Rollback

    UpdateResponse response = client.doRollback(new UpdateRequest());

#### Optimize

    UpdateResponse response = client.doOptimize(new UpdateRequest());

#### Add documents in CSV format

    UpdateRequest request = new UpdateRequest();
    request.setRequestBody("id,name,sequence_i,...");
    UpdateResponse response = client.doAddDocumentsInCSV(request);
    client.doCommit(new UpdateRequest());

#### Update in XML format

    UpdateRequest request = new UpdateRequest();
    request.setRequestBody("<optimize/>");
    UpdateResponse response = client.doUpdateInXML(request);

#### Update in JSON format

    UpdateRequest request = new UpdateRequest();
    request.setRequestBody("{ \"optimize\": { \"waitFlush\":false, \"waitSearcher\":false } }");
    request.setWriterType(WriterType.JSON());
    request.setAdditionalQueryString("&indent=on");
    UpdateResponse response = client.doUpdateInJSON(request);

### Load documents from Update format

JSON format is not supported.

#### [XML format](http://wiki.apache.org/solr/UpdateXmlMessages)

     String xmlString = "<add><doc><field name=\"employeeId\">05991</field><field name=\"office\">Bridgewater</field>..."
     List<SolrDocument> docs = UpdateFormatLoader.fromXMLStringInJava(xmlString);
     for (SolrDocument doc : docs) {
       log.debug(doc.toString());
     }

#### [CSV format](http://wiki.apache.org/solr/UpdateCSV)

     String csvString = "id,name,sequence_i\n0553573403,A Game of Thrones,1\n..."
     List<SolrDocument> docs = UpdateFormatLoader.fromCSVStringInJava(csvString);
     for (SolrDocument doc : docs) {
       log.debug(doc.toString());
     }

### Ping

    PingResponse response = client.doPing(new PingRequest());

