package com.github.seratch.scalikesolr.http

case class HttpResponse(val statusCode: Int, val headers: Map[String, List[String]], val content: String)