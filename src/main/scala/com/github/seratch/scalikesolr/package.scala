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

package com.github.seratch

package object scalikesolr {

  type WriterType = request.common.WriterType
  val WriterType = request.common.WriterType

  type QueryRequest = request.QueryRequest
  val QueryRequest = request.QueryRequest
  type QueryResponse = response.QueryResponse
  val QueryResponse = response.QueryResponse

  type AddRequest = request.AddRequest
  val AddRequest = request.AddRequest
  type AddResponse = response.AddResponse
  val AddResponse = response.AddResponse

  type DeleteRequest = request.DeleteRequest
  val DeleteRequest = request.DeleteRequest
  type DeleteResponse = response.DeleteResponse
  val DeleteResponse = response.DeleteResponse

  type UpdateRequest = request.UpdateRequest
  val UpdateRequest = request.UpdateRequest
  type UpdateResponse = response.UpdateResponse
  val UpdateResponse = response.UpdateResponse

  type DIHCommandRequest = request.DIHCommandRequest
  val DIHCommandRequest = request.DIHCommandRequest
  type DIHCommandResponse = response.DIHCommandResponse
  val DIHCommandResponse = response.DIHCommandResponse

  type PingRequest = request.PingRequest
  val PingRequest = request.PingRequest
  type PingResponse = response.PingResponse
  val PingResponse = response.PingResponse

}
