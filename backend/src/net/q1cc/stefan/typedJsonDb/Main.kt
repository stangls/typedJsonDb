package net.q1cc.stefan.typedJsonDb

import net.q1cc.stefan.typedJsonDb.dataStructures.MappingMutableMap
import net.q1cc.stefan.typedJsonDb.typing.Type
import org.mapdb.DBMaker
import org.mapdb.DB
import org.mapdb.HTreeMap
import org.mapdb.Serializer
import java.io.Closeable
import java.io.File
import java.util.concurrent.ConcurrentMap
import javax.print.DocFlavor
import kotlin.reflect.KClass
import kotlin.reflect.KMutableProperty
import kotlin.reflect.KMutableProperty1
import kotlin.reflect.KType
import kotlin.reflect.full.createType
import kotlin.reflect.full.isSubclassOf
import kotlin.reflect.full.isSubtypeOf
import kotlin.reflect.full.memberProperties


/**
 * Created by stefan on 14.04.17.
 */
object Main {

    @JvmStatic
    fun main(args: Array<String>) {
    }
}
