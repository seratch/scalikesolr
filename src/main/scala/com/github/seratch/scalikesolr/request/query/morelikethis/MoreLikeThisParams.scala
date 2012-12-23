/*
 * Copyright 2011 Kazuhiro Sera
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */

package com.github.seratch.scalikesolr.request.query.morelikethis

import scala.beans.BeanProperty
import com.github.seratch.scalikesolr.request.common.RequestParam

case class MoreLikeThisParams(@BeanProperty var enabled: Boolean = false,
    @BeanProperty var count: Int = 0,
    @BeanProperty var fieldsToUseForSimilarity: FieldsToUseForSimilarity = FieldsToUseForSimilarity(),
    @BeanProperty var isBoostedQueryEnabled: IsBoostedQueryEnabled = IsBoostedQueryEnabled(),
    @BeanProperty var maximumNumberOfQueryTerms: MaximumNumberOfQueryTerms = MaximumNumberOfQueryTerms(),
    @BeanProperty var maximumNumberOfTokensToParseInEachDocument: MaximumNumberOfTokensToParseInEachDocument = MaximumNumberOfTokensToParseInEachDocument(),
    @BeanProperty var maximumWordLengthAboveToBeIgnored: MaximumWordLengthAboveToBeIgnored = MaximumWordLengthAboveToBeIgnored(),
    @BeanProperty var minimumDocumentFrequency: MinimumDocumentFrequency = MinimumDocumentFrequency(),
    @BeanProperty var minimumTermFrequency: MinimumTermFrequency = MinimumTermFrequency(),
    @BeanProperty var minimumWordLengthBelowToBeIgnored: MinimumWordLengthBelowToBeIgnored = MinimumWordLengthBelowToBeIgnored(),
    @BeanProperty var queryFields: QueryFields = QueryFields()) {

  def this(enabled: Boolean, count: Int, fieldsToUseForSimilarity: FieldsToUseForSimilarity) {
    this(
      enabled = enabled,
      count = count,
      isBoostedQueryEnabled = IsBoostedQueryEnabled(),
      fieldsToUseForSimilarity = fieldsToUseForSimilarity,
      maximumNumberOfQueryTerms = MaximumNumberOfQueryTerms(),
      maximumNumberOfTokensToParseInEachDocument = MaximumNumberOfTokensToParseInEachDocument(),
      maximumWordLengthAboveToBeIgnored = MaximumWordLengthAboveToBeIgnored(),
      minimumDocumentFrequency = MinimumDocumentFrequency(),
      minimumTermFrequency = MinimumTermFrequency(),
      minimumWordLengthBelowToBeIgnored = MinimumWordLengthBelowToBeIgnored(),
      queryFields = QueryFields())
  }
}

object MoreLikeThisParams {
  def as(enabled: Boolean, count: Int, fieldsToUseForSimilarity: FieldsToUseForSimilarity) = {
    new MoreLikeThisParams(enabled, count, fieldsToUseForSimilarity)
  }
}

case class FieldsToUseForSimilarity(@BeanProperty val fl: String = "*") extends RequestParam {

  override def isEmpty() = fl == null || fl.isEmpty

  override def getKey() = "mlt.fl"

  override def getValue() = fl.toString

}

object FieldsToUseForSimilarity {
  def as(fl: String) = FieldsToUseForSimilarity(fl)
}

case class MinimumTermFrequency(@BeanProperty val mintf: Int = -1) extends RequestParam {

  override def isEmpty() = mintf == -1

  override def getKey() = "mlt.mintf"

  override def getValue() = mintf.toString

}

object MinimumTermFrequency {
  def as(mintf: Int) = MinimumTermFrequency(mintf)
}

case class MinimumDocumentFrequency(@BeanProperty val mindf: Int = -1) extends RequestParam {

  override def isEmpty() = mindf == -1

  override def getKey() = "mlt.mindf"

  override def getValue() = mindf.toString

}

object MinimumDocumentFrequency {
  def as(mindf: Int) = MinimumDocumentFrequency(mindf)
}

case class MinimumWordLengthBelowToBeIgnored(@BeanProperty val minwl: Int = -1) extends RequestParam {

  override def isEmpty() = minwl == -1

  override def getKey() = "mlt.minwl"

  override def getValue() = minwl.toString

}

object MinimumWordLengthBelowToBeIgnored {
  def as(minwl: Int) = MinimumWordLengthBelowToBeIgnored(minwl)
}

case class MaximumWordLengthAboveToBeIgnored(@BeanProperty val maxwl: Int = -1) extends RequestParam {

  override def isEmpty() = maxwl == -1

  override def getKey() = "mlt.maxwl"

  override def getValue() = maxwl.toString

}

object MaximumWordLengthAboveToBeIgnored {
  def as(maxwl: Int) = MaximumWordLengthAboveToBeIgnored(maxwl)
}

case class MaximumNumberOfQueryTerms(@BeanProperty val maxqt: Int = -1) extends RequestParam {

  override def isEmpty() = maxqt == -1

  override def getKey() = "mlt.maxqt"

  override def getValue() = maxqt.toString

}

object MaximumNumberOfQueryTerms {
  def as(maxqt: Int) = MaximumNumberOfQueryTerms(maxqt)
}

case class MaximumNumberOfTokensToParseInEachDocument(@BeanProperty val maxntp: Int = -1) extends RequestParam {

  override def isEmpty() = maxntp == -1

  override def getKey() = "mlt.maxntp"

  override def getValue() = maxntp.toString

}

object MaximumNumberOfTokensToParseInEachDocument {
  def as(maxntp: Int) = MaximumNumberOfTokensToParseInEachDocument(maxntp)
}

case class IsBoostedQueryEnabled(@BeanProperty val boost: Boolean = false) extends RequestParam {

  override def isEmpty() = !boost

  override def getKey() = "mlt.boost"

  override def getValue() = boost.toString

}

object IsBoostedQueryEnabled {
  def as(boost: Boolean) = IsBoostedQueryEnabled(boost)
}

case class QueryFields(@BeanProperty val qf: String = "") extends RequestParam {

  override def isEmpty() = qf == null || qf.isEmpty

  override def getKey() = "mlt.qf"

  override def getValue() = qf.toString

}

object QueryFields {
  def as(qf: String) = QueryFields(qf)
}
