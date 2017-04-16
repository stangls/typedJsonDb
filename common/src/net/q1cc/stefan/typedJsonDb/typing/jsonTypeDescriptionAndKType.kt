package net.q1cc.stefan.typedJsonDb.typing

import net.q1cc.stefan.typedJsonDb.removeLineBreaks
import org.json.JSONObject
import kotlin.reflect.KClass
import kotlin.reflect.KType
import kotlin.reflect.full.createType
import kotlin.reflect.full.isSubclassOf

fun jsonPropertyTypeContent(
    type: KType,
    registerClass:(KClass<*>)->Unit
): String {
    val kClass = type.classifier as? KClass<*> ?:TODO("type not handled yet. maybe this is a non-kotlin class ??")
    val jsonTypeAndAdditions: Pair<String, String?> =
        if (kClass.isSubclassOf(Number::class)) "number".to(null) else
        if (kClass.isSubclassOf(String::class)) "string".to(null) else
        if (kClass.isSubclassOf(Boolean::class)) "boolean".to(null) else {
            // TODO: support non-embedded types like OneToMany and ManyToMany relationi
            // TODO: support more embedded types like Set (uniqeItems=true) and such
            if (kClass.isSubclassOf(Iterable::class) || kClass.isSubclassOf(Array<Any?>::class)) {
                "array" to ("""
                    "items": {
                        ${jsonPropertyTypeContent(type.arguments.first().type ?: Any::class.createType(),registerClass)}
                    }
                """)
            } else {
                registerClass(kClass)
                "object" to """
                    "title": "${kClass.simpleName}",
                    "properties": {
                        "id" : { "type": "number" }
                    }
                """
            }
        }
    val (jsonType, jsonTypeAdditions) = jsonTypeAndAdditions
    return "\"type\" : \"${jsonType}\"" + (if (jsonTypeAdditions != null) ",\n$jsonTypeAdditions" else "")
}

fun JSONObject.isFrom(
    type: KType,
    isClassRegistered:(kClass:KClass<*>)->Boolean
): Boolean {
    val kClass = type.classifier as? KClass<*> ?: TODO("type not handled yet. maybe this is a non-kotlin class ??")
    val jsonTypeDescription = this
    return when (jsonTypeDescription["type"] ?: return false) {
        "number" -> kClass.isSubclassOf(Number::class)
        "string" -> kClass.isSubclassOf(String::class)
        "boolean" -> kClass.isSubclassOf(Boolean::class)
        "array" -> (
                kClass.isSubclassOf(Iterable::class) ||
                kClass.isSubclassOf(Array<Any?>::class)
            ) && run {
                val arrayTypeDescription= jsonTypeDescription.optJSONObject("items") ?: return false
                arrayTypeDescription.isFrom( type.arguments.first().type ?: Any::class.createType(), isClassRegistered )
            }
        "object" -> run {
                val objectType = jsonTypeDescription.optString("title") ?: return false
                objectType == kClass.simpleName && isClassRegistered(kClass) &&
                jsonTypeDescription.optJSONObject("properties")?.optJSONObject("id")?.optString("type")=="number"
            }
        else -> TODO("no type parameter in ${jsonTypeDescription.toString().removeLineBreaks()}")
    }
}

