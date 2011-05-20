# scalikesolr : Apache Solr Client for Scala/Java

## What's this?

"scalikesolr" is a Solr client library for Scala/Java.

## Scala version

This library supports Scala 2.9.0.final.

## How to install

### Maven

"3.1.0" is Solr/Lucene version that is supported by scalikesolr. The "x" of "3.1.0.x" will be incremented.

    <repositories>
      ...
      <repository>
        <id>scalikesolr-releases</id>
        <url>https://github.com/seratch/scalikesolr/raw/master/mvn-repo/releases</url>
      </repository>
      <repository>
        <id>scalikesolr-snapshots</id>
        <url>https://github.com/seratch/scalikesolr/raw/master/mvn-repo/snapshots</url>
      </repository>
      ...
    </repositories>

    <dependency>
      <groupId>com.github.seratch</groupId>
      <artifactId>scalikesolr</artifactId>
      <version>[3.1.0.0,)</version>
    </dependency>

## Usage

### Query

Simple Query:

    import com.github.seratch.scalikesolr._

    val client = Solr.httpServer(new URL("http://localhost:8983/solr")).newClient

    val request = new QueryRequest(Query("author:Rick"))
    val response = client.doQuery(request)
    println(response.responseHeader)
    println(response.response)

With Highlightings:

    val request = new QueryRequest(
      writerType = WriterType.JSON,
      query = Query("author:Rick"),
      sort = Sort("page_i desc"),
      highlighting = HighlightingParams(true)
    )
    val response = client.doQuery(request)
    println(response.highlightings)

With MoreLikeThis:

    val request = new QueryRequest(
      query = Query("author:Rick"),
      moreLikeThis = MoreLikeThisParams(
        enabled = true,
        count = 3,
        fieldsToUseForSimilarity = FieldsToUseForSimilarity("body")
      )
    )
    val response = client.doQuery(request)
    println(response.moreLikeThis)

With FacetQuery:

    val request = new QueryRequest(
      query = Query("author:Rick"),
      facet = new FacetParams(
        enabled = true,
        params = List(new FacetParam(Param("facet.field"), Value("title")))
      )
    )
    val response = client.doQuery(request)
    println(response.facet.facetFields)

### DIH Command

    val request = new DIHCommandRequest(command = "delta-import")
    val response = client.doDIHCommand(request)
    println(response.initArgs)
    println(response.command)
    println(response.status)
    println(response.importResponse)
    println(response.statusMessages)

### Update

Add documents:

     val request = new UpdateRequest()
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
     val response = client.doAddDocumentsAndCommit(request)

Commit:

     val response = client.doCommit(new UpdateRequest())

Rollback:

     val response = client.doRollback(new UpdateRequest())

### Ping

     val response = client.doPing(new PingRequest())
     println(response.status) // "OK"

### How to know which type is the param mapped?

Name of constructor arg is same as the Solr parameter key.

For example:

    case class Query(@BeanProperty val q: String) extends RequestParam

## Using in Java

This library works for Java applications well.

### Query

    SolrClient client = Solr.httpServer(new URL("http://localhost:8983/solr")).getNewClient();
    QueryRequest request = new QueryRequest(new Query("author:Rick"));
    QueryResponse response = client.doQuery(request);
    assertThat(response.getResponseHeader(), is(notNullValue()));
    assertThat(response.getResponse(), is(notNullValue()));
    assertThat(response.getHighlightings(), is(notNullValue()));
    assertThat(response.getMoreLikeThis(), is(notNullValue()));
    assertThat(response.getFacet(), is(notNullValue()));

### DIH Command

    DIHCommandRequest request = new DIHCommandRequest("delta-import");
    DIHCommandResponse response = client.doDIHCommand(request);
    assertThat(response.getCommand(), is(notNullValue()));
    assertThat(response.getImportResponse(), is(notNullValue()));
    assertThat(response.getRawBody(), is(notNullValue()));
    assertThat(response.getStatus(), is(notNullValue()));
    assertThat(response.getStatusMessages().getDefaults(), is(notNullValue()));
    assertThat(response.getInitArgs().getDefaults(), is(notNullValue()));

### Update

Add documements:

    UpdateRequest request = new UpdateRequest();
    String jsonString = "{\"id\" : \"978-0641723445\", ... }";
    SolrDocument doc = new SolrDocument(WriterType.JSON(), jsonString);
    List<SolrDocument> docs = new ArrayList<SolrDocument>();
    docs.add(doc);
    request.setDocumentsInJava(docs);
    UpdateResponse response = client.doAddDocuments(request);

Commit:

    UpdateRequest request = new UpdateRequest();
    UpdateResponse response = client.doCommit(request);

Rollback:

    UpdateRequest request = new UpdateRequest();
    UpdateResponse response = client.doRollback(request);

### Ping

    PingRequest request = new PingRequest();
    PingResponse response = client.doPing(request);
