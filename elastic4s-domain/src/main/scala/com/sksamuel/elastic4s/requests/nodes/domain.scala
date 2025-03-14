package com.sksamuel.elastic4s.requests.nodes

import com.fasterxml.jackson.annotation.JsonProperty
import com.sksamuel.elastic4s.ext.Maps
import com.typesafe.config.{Config, ConfigFactory}

import scala.concurrent.duration._

case class SwapStats(
    @JsonProperty("total_in_bytes") totalInBytes: Long,
    @JsonProperty("free_in_bytes") freeInBytes: Long,
    @JsonProperty("used_in_bytes") usedInBytes: Long
)

case class MemoryStats(
    @JsonProperty("total_in_bytes") totalInBytes: Long,
    @JsonProperty("free_in_bytes") freeInBytes: Long,
    @JsonProperty("used_in_bytes") usedInBytes: Long,
    @JsonProperty("free_percent") freePercent: Int,
    @JsonProperty("used_percent") usedPercent: Int
)

case class OsStats(
    @JsonProperty("cpu_percent") cpuPercent: Int,
    @JsonProperty("load_average") loadAverage: Double,
    mem: MemoryStats,
    swap: SwapStats
)

case class Docs(count: Long)

case class Indices(docs: Docs)

case class Jvm(@JsonProperty("uptime_in_millis") uptimeInMillis: Long) {
  val uptime: java.time.Duration = java.time.Duration.ofMillis(uptimeInMillis)
}

case class NodeStats(
    name: String,
    @JsonProperty("transport_address") transportAddress: String,
    host: String,
    ip: Seq[String],
    os: Option[OsStats],
    roles: Seq[String],
    indices: Indices,
    jvm: Jvm
)

case class NodesStatsResponse(@JsonProperty("cluster_name") clusterName: String, nodes: Map[String, NodeStats])

case class NodeInfoResponse(@JsonProperty("cluster_name") clusterName: String, nodes: Map[String, NodeInfo])

case class NodeInfo(
    name: String,
    @JsonProperty("transport_address") transportAddress: String,
    host: String,
    ip: String,
    version: String,
    @JsonProperty("build_hash") buildHash: String,
    @JsonProperty("total_indexing_buffer") totalIndexingBuffer: Long,
    roles: Seq[String],
    @JsonProperty("settings") settingsAsMap: Map[String, AnyRef],
    os: OsInfo,
    process: Process,
    transport: Transport,
    http: Http,
    @JsonProperty("thread_pool") threadPools: Map[String, ThreadPool]
) {

  def settings: Config = ConfigFactory.parseMap(Maps.deepAsJava(settingsAsMap))
}

case class Transport(
    @JsonProperty("bound_address") boundAddress: Seq[String],
    @JsonProperty("publish_address") publishAddress: String
)

case class Http(
    @JsonProperty("bound_address") boundAddress: Seq[String],
    @JsonProperty("publish_address") publishAddress: String
)

case class ThreadPool(
    `type`: String,
    @JsonProperty("keep_alive") keepAlive: Option[String],
    min: Long,
    max: Long,
    queue_size: Long
)

case class Process(
    @JsonProperty("refresh_interval_in_millis") refreshIntervalInMillis: Long,
    @JsonProperty("id") id: String,
    @JsonProperty("mlockall") mlockall: Boolean
) {
  def refreshInterval: Duration = refreshIntervalInMillis.millis
}

case class OsInfo(
    @JsonProperty("refresh_interval_in_millis") refreshIntervalInMillis: Long,
    name: String,
    arch: String,
    version: String,
    @JsonProperty("available_processors") availableProcessors: Int,
    @JsonProperty("allocated_processors") allocatedProcessors: Int
) {
  def refreshInterval: Duration = refreshIntervalInMillis.millis
}
