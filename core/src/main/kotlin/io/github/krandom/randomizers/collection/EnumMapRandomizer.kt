package io.github.krandom.randomizers.collection

import io.github.krandom.api.Randomizer
import io.github.krandom.randomizers.misc.EnumRandomizer
import io.github.krandom.randomizers.number.ByteRandomizer
import java.util.EnumMap
import kotlin.math.abs

class EnumMapRandomizer<E : Enum<E>, V>
@JvmOverloads
constructor(
  private val keyRandomizer: EnumRandomizer<E>,
  private val valueRandomizer: Randomizer<V>,
  private val nbElements: Int = abs(ByteRandomizer().getRandomValue().toInt()),
) : Randomizer<EnumMap<E, V>> {
  init {
    require(nbElements >= 0) { "The number of elements to generate must be >= 0" }
  }

  @Suppress("UNCHECKED_CAST")
  override fun getRandomValue(): EnumMap<E, V> {
    val sampleKey = keyRandomizer.getRandomValue()!!
    val enumClass: Class<E> = sampleKey.javaClass
    val maxDistinctKeys = enumClass.enumConstants.size
    val targetSize = minOf(nbElements, maxDistinctKeys)
    if (targetSize == 0) {
      return EnumMap(enumClass)
    }

    val tmp = LinkedHashMap<E, V>(targetSize)

    tmp[sampleKey] = valueRandomizer.getRandomValue()
    while (tmp.size < targetSize) {
      val k = keyRandomizer.getRandomValue()!!
      if (!tmp.containsKey(k)) {
        tmp[k] = valueRandomizer.getRandomValue()!!
      }
    }
    return EnumMap(tmp)
  }
}
