package com.github.seratch.scalikesolr.request.util

import com.github.seratch.scalikesolr.request.common.RequestParam

private[request] object QueryStringUtil {

  def appendIfExists[RP <: RequestParam](buf: StringBuilder, param: RP): Unit = {
    if (param != null && !param.isEmpty) {
      if (buf.length > 0) buf.append("&")
      buf.append(param.toQueryString)
    }
  }

}