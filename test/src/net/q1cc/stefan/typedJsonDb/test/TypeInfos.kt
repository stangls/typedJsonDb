package net.q1cc.stefan.typedJsonDb.test

import net.q1cc.stefan.typedJsonDb.Database
import net.q1cc.stefan.typedJsonDb.typing.Type
import org.everit.json.schema.ObjectSchema
import kotlin.reflect.KClass

class TypeInfos(val type: Type) {
    val schema = type.schema as ObjectSchema
}

fun Database.aTestWith(kClass: KClass<*>, block: TypeInfos.() -> Unit) {
    TypeInfos( this.registerType(kClass) ).block()
}