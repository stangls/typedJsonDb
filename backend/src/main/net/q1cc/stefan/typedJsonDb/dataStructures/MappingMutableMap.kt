package net.q1cc.stefan.typedJsonDb.dataStructures

import java.io.Closeable
import java.util.concurrent.ConcurrentMap

class MappingMutableMap<out M:MutableMap<K,VA>,K,VA,VB> (
        val base: M,
        val va2vb: (VA) -> VB,
        val vb2va: (VB) -> VA
) : MutableMap<K, VB> {

    // delegation methods

    override fun containsKey(key: K) = base.containsKey(key)

    override fun clear() = base.clear()

    override fun remove(key: K) : VB? {
        return va2vb(base.remove(key)?:return null)
    }

    override fun remove(key: K, value: VB): Boolean
        = base.remove(key,vb2va(value))

    override fun get(key: K): VB? {
        return va2vb(base.get(key)?:return null)
    }

    override fun putIfAbsent(key: K, value: VB): VB? {
        return va2vb(base.putIfAbsent(key,vb2va(value))?:return null)
    }

    override val size: Int
        get() = base.size

    override fun isEmpty(): Boolean
        = base.isEmpty()

    override fun put(key: K, value: VB): VB? {
        return va2vb(base.put(key,vb2va(value))?:return null)
    }

    override fun putAll(from: Map<out K, VB>) {
        base.putAll(from.mapValues({e->vb2va(e.value)}))
    }

    override fun replace(key: K, oldValue: VB, newValue: VB): Boolean
        = base.replace(key,vb2va(oldValue),vb2va(newValue))

    override fun replace(key: K, value: VB): VB? {
        return va2vb(base.replace(key,vb2va(value))?:return null)
    }

    override fun containsValue(value: VB): Boolean
        = base.containsValue(vb2va(value))

    override val entries: MutableSet<MutableMap.MutableEntry<K, VB>>
        get() = base.entries.map { baseEntry ->
            object:MutableMap.MutableEntry<K,VB>{
                override val key: K
                    get() = baseEntry.key
                override val value: VB
                    get() = va2vb(baseEntry.value)
                override fun setValue(newValue: VB): VB
                    = va2vb(baseEntry.setValue(vb2va(newValue)))
            }
        }.toMutableSet()
    override val keys: MutableSet<K>
        get() = base.keys
    override val values: MutableCollection<VB>
        get() = base.values.map(va2vb).toMutableList()


}