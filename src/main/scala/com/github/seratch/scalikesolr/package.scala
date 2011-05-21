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
