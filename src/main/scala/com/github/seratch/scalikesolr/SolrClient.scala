/*
 * Copyright 2011_Kazuhiro SERA.
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
package com.github.seratch.scalikesolr

trait SolrClient {

  def doQuery(request: QueryRequest): QueryResponse

  def doDIHCommand(request: DIHCommandRequest): DIHCommandResponse

  def doUpdateDocuments(request: UpdateRequest): UpdateResponse

  def doDeleteDocuments(request: DeleteRequest): DeleteResponse

  def doCommit(request: UpdateRequest): UpdateResponse

  def doCommit(): UpdateResponse

  def doOptimize(request: UpdateRequest): UpdateResponse

  def doOptimize(): UpdateResponse

  def doRollback(request: UpdateRequest): UpdateResponse

  def doRollback(): UpdateResponse

  def doPing(request: PingRequest): PingResponse

  def doPing(): PingResponse

  @deprecated
  def doUpdateDocumentsInCSV(request: UpdateRequest): UpdateResponse

  def doUpdateInCSV(request: UpdateRequest): UpdateResponse

  def doUpdateInXML(request: UpdateRequest): UpdateResponse

  def doUpdateInJSON(request: UpdateRequest): UpdateResponse

}
