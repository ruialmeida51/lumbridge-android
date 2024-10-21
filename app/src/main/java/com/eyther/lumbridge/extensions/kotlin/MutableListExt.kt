package com.eyther.lumbridge.extensions.kotlin

/**
 * Swaps two elements in a mutable list.
 *
 * @param firstIndex The index of the first element to swap.
 * @param secondIndex The index of the second element to swap.
 *
 * @return The mutable list with the elements swapped.
 */
fun <T> MutableList<T>.swapElements(firstIndex: Int, secondIndex: Int): MutableList<T> = apply {
    if (firstIndex == secondIndex) return@apply
    val element = this.removeAt(firstIndex)
    this.add(secondIndex, element)
}
