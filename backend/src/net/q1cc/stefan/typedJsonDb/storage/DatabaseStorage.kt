package net.q1cc.stefan.typedJsonDb.storage

import net.q1cc.stefan.typedJsonDb.dataStructures.MappingMutableMap
import net.q1cc.stefan.typedJsonDb.typing.Type
import java.io.Closeable
import java.util.concurrent.ConcurrentMap

abstract class DatabaseStorage : Closeable {
    abstract val serializedTypes: ConcurrentMap<String, String>
    val types get() = MappingMutableMap(serializedTypes, ::Type, Type::jsonString )
}