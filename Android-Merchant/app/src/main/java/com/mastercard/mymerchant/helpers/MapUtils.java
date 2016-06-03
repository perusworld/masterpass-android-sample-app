/*
 *    ****************************************************************************
 *    Copyright (c) 2015, MasterCard International Incorporated and/or its
 *    affiliates. All rights reserved.
 *    <p/>
 *    The contents of this file may only be used subject to the MasterCard
 *    Mobile Payment SDK for MCBP and/or MasterCard Mobile MPP UI SDK
 *    Materials License.
 *    <p/>
 *    Please refer to the file LICENSE.TXT for full details.
 *    <p/>
 *    TO THE EXTENT PERMITTED BY LAW, THE SOFTWARE IS PROVIDED "AS IS", WITHOUT
 *    WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
 *    WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 *    NON INFRINGEMENT. TO THE EXTENT PERMITTED BY LAW, IN NO EVENT SHALL
 *    MASTERCARD OR ITS AFFILIATES BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *    LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
 *    FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS
 *    IN THE SOFTWARE.
 *    *****************************************************************************
 */

package com.mastercard.mymerchant.helpers;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * Some utilities to make easy inline creation of Java Maps
 */
public final class MapUtils {

    public static class Entry<K, V> {
        private K key;
        private V value;

        public Entry(final K key, final V value) {
            this.key = key;
            this.value = value;
        }

        public K getKey() {
            return key;
        }

        public V getValue() {
            return value;
        }
    }

    public static <K, V> Entry<K, V> entry(final K key, final V value) {
        return new Entry(key, value);
    }

    public static <K, V> Map<K, V> asMap(final Entry<K, V>... entries) {
        return populate(new HashMap<K, V>(), entries);
    }

    public static <K, V> SortedMap<K, V> asSortedMap(final Entry<K, V>... entries) {
        return populate(new TreeMap<K, V>(), entries);
    }

    public static <K, V> SortedMap<K, V> asSortedMap(SortedMap<K, V> initialMap, final Entry<K, V>... entries) {
        return populate(initialMap, entries);
    }

    public static <K, V> Map<K, V> asOrderedMap(final Entry<K, V>... entries) {
        return populate(new LinkedHashMap<K, V>(), entries);
    }

    public static <K, V> Map<K, V> asUnmodifiableMap(final Entry<K, V>... entries) {
        return Collections.unmodifiableMap(populate(new HashMap<K, V>(), entries));
    }

    public static <K, V> Map<K, V> asUnmodifiableOrderedMap(final Entry<K, V>... entries) {
        return Collections.unmodifiableMap(populate(new LinkedHashMap<K, V>(), entries));
    }

    private static <K, V> Map<K, V> populate(final Map map, final Entry<K, V>... entries) {
        for (final Entry entry : entries) {
            map.put(entry.getKey(), entry.getValue());
        }
        return map;
    }

    private static <K, V> SortedMap<K, V> populate(final SortedMap map, final Entry<K, V>... entries) {
        for (final Entry entry : entries) {
            map.put(entry.getKey(), entry.getValue());
        }
        return map;
    }
}
