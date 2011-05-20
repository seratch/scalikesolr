package com.github.seratch.scalikesolr.response.dih

import reflect.BeanProperty
import com.github.seratch.scalikesolr.SolrDocument

case class InitArgs(@BeanProperty val defaults: SolrDocument)