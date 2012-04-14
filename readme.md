# ScalikeSolr : Apache Solr Client for Scala/Java

"ScalikeSolr" is a simple Solr client library for Scala. And it also works fine with Java.

## How to install

### sbt

```scala
libraryDependencies ++= Seq(
  "com.github.seratch" %% "scalikesolr" % "(3.6,)"
)
```

via ls:

http://ls.implicit.ly/seratch/scalikesolr

```
ls -n scalikesolr
ls-install scalikesolr
```

### Maven

Available on maven central repository.

The "2.9.2" of "scalikesolr_2.9.2"(artifactId) is the Scala version to use.

```xml
<dependency>
  <groupId>com.github.seratch</groupId>
  <artifactId>scalikesolr_2.9.2</artifactId>
  <version>[3.6,)</version>
</dependency>
```

## Usage

### Query

#### Simple Query

Using [Core Query Paramters](http://wiki.apache.org/solr/CoreQueryParameters) and [Common Query Parameters](http://wiki.apache.org/solr/CommonQueryParameters):

```scala
import com.github.seratch.scalikesolr._

val client = Solr.httpServer(new URL("http://localhost:8983/solr")).newClient
val request = new QueryRequest(query = Query("author:Rick")) 
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
```

#### Bind from SolrDocument

It requires no-argument constructor and setters for fields.
It is also possible to specify user-defined type that has one argument(String) constructor.

```scala
case class PageI(val value: String = "")
case class Book(
  var id: String = "",
  var cat: List[String] = Nil,
  var price: Double = 0.0,
  var pageI: PageI = PageI(),
  var sequenceI: Int = 0 ) {
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
```

See the [documentation](https://github.com/seratch/scalikesolr/wiki) for more detail.



## Using in Java

This library works fine with Java.

### Query

```java
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
```

#### Bind from SolrDocument

```java
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
```

See the [documentation](https://github.com/seratch/scalikesolr/wiki) for more detail.


## License

Apache License, Version 2.0 

http://www.apache.org/licenses/LICENSE-2.0.html

