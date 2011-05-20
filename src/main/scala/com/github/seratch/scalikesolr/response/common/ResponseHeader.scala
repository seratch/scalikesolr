package com.github.seratch.scalikesolr.response.common

import reflect.BeanProperty
import com.github.seratch.scalikesolr.SolrDocument

case class ResponseHeader(@BeanProperty val status: Int,
                          @BeanProperty val qTime: Int,
                          @BeanProperty val params: SolrDocument = SolrDocument())
