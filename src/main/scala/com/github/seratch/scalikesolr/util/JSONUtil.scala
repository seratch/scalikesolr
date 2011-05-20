package com.github.seratch.scalikesolr.util

object JSONUtil {

  def normalizeNum(num: String) = num.replaceFirst("\\.0+$", "")

}