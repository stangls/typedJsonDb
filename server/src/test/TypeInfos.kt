import net.q1cc.stefan.typedJsonDb.Database
import net.q1cc.stefan.typedJsonDb.typing.Type
import org.everit.json.schema.ObjectSchema
import org.junit.Assert.assertTrue
import kotlin.reflect.KClass

class TypeInfos(val type: net.q1cc.stefan.typedJsonDb.typing.Type) {
    val schema = type.schema as ObjectSchema
}

fun Database.aTestWith(kClass: kotlin.reflect.KClass<*>, block: TypeInfos.() -> Unit) {
    val type = this.registerType(kClass)
    TypeInfos(type).block()
    assertTrue(isFrom(type,kClass))
}