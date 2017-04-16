package net.q1cc.stefan.typedJsonDb

import net.q1cc.stefan.typedJsonDb.storage.DatabaseStorage
import net.q1cc.stefan.typedJsonDb.typing.Type
import kotlin.reflect.KClass

class Database( val storage : DatabaseStorage) {

    protected val registeredClasses = mutableSetOf<KClass<*>>()

    fun registerType( t: Type) : Boolean =
        storage.types.getOrPut(t.name,{t}) == t

    fun registerType( c: KClass<*> ) : Type {
        val type = Type.from(c, { registerType(it) } )
        if (registerType(type)){
            registeredClasses += c
        }else{
            throw Exception("Could not register type ${type.name}. Does it exist already with a different definition? Maybe you want to migrate the type.")
        }
        return type
    }

    fun isFrom(type: Type, kClass: KClass<*>): Boolean =
        type.isFrom(kClass,this::isClassRegistered)

    fun isClassRegistered(kClass:KClass<*>) = registeredClasses.contains(kClass)
}