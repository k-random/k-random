/*
 * The MIT License
 *
 *   Copyright (c) 2023, Mahmoud Ben Hassine (mahmoud.benhassine@icloud.com)
 *
 *   Permission is hereby granted, free of charge, to any person obtaining a copy
 *   of this software and associated documentation files (the "Software"), to deal
 *   in the Software without restriction, including without limitation the rights
 *   to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *   copies of the Software, and to permit persons to whom the Software is
 *   furnished to do so, subject to the following conditions:
 *
 *   The above copyright notice and this permission notice shall be included in
 *   all copies or substantial portions of the Software.
 *
 *   THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *   IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *   FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *   AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *   LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *   OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 *   THE SOFTWARE.
 */
package io.github.krandom.beans

import java.util.Hashtable
import java.util.IdentityHashMap
import java.util.NavigableMap
import java.util.SortedMap
import java.util.TreeMap
import java.util.WeakHashMap
import java.util.concurrent.ConcurrentMap
import java.util.concurrent.ConcurrentNavigableMap
import java.util.concurrent.ConcurrentSkipListMap

class WildCardMapBean {
  /*
   * Interfaces
   */
  var unboundedWildCardTypedMap: MutableMap<*, *>? = null
  var boundedWildCardTypedMap: MutableMap<out Number, out Runnable>? = null

  var unboundedWildCardTypedSortedMap: SortedMap<*, *>? = null
  var boundedWildCardTypedSortedMap: SortedMap<out Number, out Runnable>? = null

  var unboundedWildCardTypedNavigableMap: NavigableMap<*, *>? = null
  var boundedWildCardTypedNavigableMap: NavigableMap<out Number, out Runnable>? = null

  var unboundedWildCardTypedConcurrentMap: ConcurrentMap<*, *>? = null
  var boundedWildCardTypedConcurrentMap: ConcurrentMap<out Number, out Runnable>? = null

  var unboundedWildCardTypedConcurrentNavigableMap: ConcurrentNavigableMap<*, *>? = null
  var boundedWildCardTypedConcurrentNavigableMap:
    ConcurrentNavigableMap<out Number, out Runnable>? =
    null

  /*
   * Classes
   */
  var unboundedWildCardTypedHashMap: HashMap<*, *>? = null
  var boundedWildCardTypedHashMap: HashMap<out Number, out Runnable>? = null

  var unboundedWildCardTypedHashtable: Hashtable<*, *>? = null
  var boundedWildCardTypedHashtable: Hashtable<out Number, out Runnable>? = null

  var unboundedWildCardTypedHinkedHashMap: LinkedHashMap<*, *>? = null
  var boundedWildCardTypedLinkedHashMap: LinkedHashMap<out Number, out Runnable>? = null

  var unboundedWildCardTypedWeakHashMap: WeakHashMap<*, *>? = null
  var boundedWildCardTypedWeakHashMap: WeakHashMap<out Number, out Runnable>? = null

  var unboundedWildCardTypedIdentityHashMap: IdentityHashMap<*, *>? = null
  var boundedWildCardTypedIdentityHashMap: IdentityHashMap<out Number, out Runnable>? = null

  var unboundedWildCardTypedTreeMap: TreeMap<*, *>? = null
  var boundedWildCardTypedTreeMap: TreeMap<out Number, out Runnable>? = null

  var unboundedWildCardTypedConcurrentSkipListMap: ConcurrentSkipListMap<*, *>? = null
  var boundedWildCardTypedConcurrentSkipListMap: ConcurrentSkipListMap<out Number, out Runnable>? =
    null
}
