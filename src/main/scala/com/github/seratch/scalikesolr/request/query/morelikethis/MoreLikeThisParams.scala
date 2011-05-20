package com.github.seratch.scalikesolr.request.query.morelikethis

import reflect.BeanProperty
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
    this (
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

case class FieldsToUseForSimilarity(@BeanProperty val fl: String = "*") extends RequestParam {

  override def isEmpty() = fl == null || fl.isEmpty

  override def toQueryString() = "mlt.fl=" + fl

}

object FieldsToUseForSimilarity {
  def as(fl: String) = FieldsToUseForSimilarity(fl)
}

case class MinimumTermFrequency(@BeanProperty val mintf: Int = -1) extends RequestParam {

  override def isEmpty() = mintf == -1

  override def toQueryString() = "mlt.mintf=" + mintf

}

object MinimumTermFrequency {
  def as(mintf: Int) = MinimumTermFrequency(mintf)
}

case class MinimumDocumentFrequency(@BeanProperty val mindf: Int = -1) extends RequestParam {

  override def isEmpty() = mindf == -1

  override def toQueryString() = "mlt.mindf=" + mindf

}

object MinimumDocumentFrequency {
  def as(mindf: Int) = MinimumDocumentFrequency(mindf)
}

case class MinimumWordLengthBelowToBeIgnored(@BeanProperty val minwl: Int = -1) extends RequestParam {

  override def isEmpty() = minwl == -1

  override def toQueryString() = "mlt.minwl=" + minwl

}

object MinimumWordLengthBelowToBeIgnored {
  def as(minwl: Int) = MinimumWordLengthBelowToBeIgnored(minwl)
}

case class MaximumWordLengthAboveToBeIgnored(@BeanProperty val maxwl: Int = -1) extends RequestParam {

  override def isEmpty() = maxwl == -1

  override def toQueryString() = "mlt.maxwl=" + maxwl

}

object MaximumWordLengthAboveToBeIgnored {
  def as(maxwl: Int) = MaximumWordLengthAboveToBeIgnored(maxwl)
}

case class MaximumNumberOfQueryTerms(@BeanProperty val maxqt: Int = -1) extends RequestParam {

  override def isEmpty() = maxqt == -1

  override def toQueryString() = "mlt.maxqt=" + maxqt

}

object MaximumNumberOfQueryTerms {
  def as(maxqt: Int) = MaximumNumberOfQueryTerms(maxqt)
}

case class MaximumNumberOfTokensToParseInEachDocument(@BeanProperty val maxntp: Int = -1) extends RequestParam {

  override def isEmpty() = maxntp == -1

  override def toQueryString() = "mlt.maxqt=" + maxntp

}

object MaximumNumberOfTokensToParseInEachDocument {
  def as(maxntp: Int) = MaximumNumberOfTokensToParseInEachDocument(maxntp)
}

case class IsBoostedQueryEnabled(@BeanProperty val boost: Boolean = false) extends RequestParam {

  override def isEmpty() = !boost

  override def toQueryString() = "mlt.boost=" + boost

}

object IsBoostedQueryEnabled {
  def as(boost: Boolean) = IsBoostedQueryEnabled(boost)
}

case class QueryFields(@BeanProperty val qf: String = "") extends RequestParam {

  override def isEmpty() = qf == null || qf.isEmpty

  override def toQueryString() = "mlt.qf=" + qf

}

object QueryFields {
  def as(qf: String) = QueryFields(qf)
}
