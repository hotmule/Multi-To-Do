package ru.hotmule.lastik.data.local

import kotlin.Int
import kotlin.Long
import kotlin.String

data class Attributes(
  val id: Long,
  val name: String?,
  val rank: Int?,
  val playCount: Int?,
  val lowResImage: String?,
  val highResImage: String?
) {
  override fun toString(): String = """
  |Attributes [
  |  id: $id
  |  name: $name
  |  rank: $rank
  |  playCount: $playCount
  |  lowResImage: $lowResImage
  |  highResImage: $highResImage
  |]
  """.trimMargin()
}
