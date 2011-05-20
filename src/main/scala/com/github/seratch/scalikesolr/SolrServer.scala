package com.github.seratch.scalikesolr

import java.net.URL

trait SolrServer {

  def newClient(): SolrClient

  def getNewClient(): SolrClient = newClient()

}

class HttpSolrServer(val url: URL) extends SolrServer {

  override def newClient(): SolrClient = new HttpSolrClient(url)

}
