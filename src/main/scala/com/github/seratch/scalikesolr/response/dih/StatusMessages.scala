package com.github.seratch.scalikesolr.response.dih

import reflect.BeanProperty
import com.github.seratch.scalikesolr.SolrDocument

case class StatusMessages(@BeanProperty val defaults: SolrDocument)