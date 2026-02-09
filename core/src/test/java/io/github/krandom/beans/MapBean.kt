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

class MapBean {
  /*
   * Interfaces
   */
  var map: MutableMap<*, *>? = null
  var typedMap: MutableMap<Int, Person>? = null

  var sortedMap: SortedMap<*, *>? = null
  var typedSortedMap: SortedMap<Int, Person>? = null

  var navigableMap: NavigableMap<*, *>? = null
  var typedNavigableMap: NavigableMap<Int, Person>? = null

  var concurrentMap: ConcurrentMap<*, *>? = null
  var typedConcurrentMap: ConcurrentMap<Int, Person>? = null

  var concurrentNavigableMap: ConcurrentNavigableMap<*, *>? = null
  var typedConcurrentNavigableMap: ConcurrentNavigableMap<Int, Person>? = null

  /*
   * Classes
   */
  var hashMap: HashMap<*, *>? = null
  var typedHashMap: HashMap<Int, Person>? = null

  var hashtable: Hashtable<*, *>? = null
  var typedHashtable: Hashtable<Int, Person>? = null

  var linkedHashMap: LinkedHashMap<*, *>? = null
  var typedLinkedHashMap: LinkedHashMap<Int, Person>? = null

  var weakHashMap: WeakHashMap<*, *>? = null
  var typedWeakHashMap: WeakHashMap<Int, Person>? = null

  var identityHashMap: IdentityHashMap<*, *>? = null
  var typedIdentityHashMap: IdentityHashMap<Int, Person>? = null

  var treeMap: TreeMap<*, *>? = null
  var typedTreeMap: TreeMap<Int, Person>? = null

  var concurrentSkipListMap: ConcurrentSkipListMap<*, *>? = null
  var typedConcurrentSkipListMap: ConcurrentSkipListMap<Int, Person>? = null
}
