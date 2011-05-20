package com.github.seratch.scalikesolr

import java.net.URL

object Solr {

  def httpServer(url: URL): SolrServer = new HttpSolrServer(url)

  def getHttpServer(url: URL): SolrServer = httpServer(url)

}