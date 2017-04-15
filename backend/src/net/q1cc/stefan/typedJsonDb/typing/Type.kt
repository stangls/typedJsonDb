package net.q1cc.stefan.typedJsonDb.typing

import net.q1cc.stefan.typedJsonDb.typing.jsonPropertyTypeContent
import org.everit.json.schema.loader.SchemaLoader
import org.json.JSONException
import org.json.JSONObject
import org.json.JSONTokener
import org.mapdb.DB.Keys.type
import kotlin.reflect.KClass
import kotlin.reflect.KMutableProperty1
import kotlin.reflect.KType
import kotlin.reflect.full.createType
import kotlin.reflect.full.isSubclassOf
import kotlin.reflect.full.memberProperties

class Type(
    val schemaObject : JSONObject
){
    constructor(jsonDefinition : String) : this (
        try {
            JSONObject(JSONTokener(jsonDefinition))
        }catch(e:JSONException){
            println(jsonDefinition)
            throw e
        }
    )

    override fun toString() = name
    val jsonString get() = schemaObject.toString()
    val jsonStringPretty get() = schemaObject.toString(2)

    val schema by lazy { SchemaLoader.load(schemaObject) }

    val name by lazy { schema.title }

    fun isFrom(c: KClass<*>, isClassRegistered: (kClass: KClass<*>) -> Boolean) : Boolean =
        schemaObject.getString("type")=="object" &&
        schemaObject.getString("title")==c.simpleName &&
        schemaObject.optJSONObject("properties")?.let {
            val missingClassProperties = c.memberProperties.map { it.name }.toMutableSet()
            it.keySet().all { fieldName ->
                val prop = c.memberProperties.find { it.name==fieldName } ?: return@all false
                missingClassProperties -= prop.name
                val jsonTypeDescription = it[fieldName] as? JSONObject ?: return@all false
                val type = prop.returnType
                jsonTypeDescription.isFrom(type,isClassRegistered)
            } && missingClassProperties.isEmpty()
        } ?: c.memberProperties.isEmpty()

    companion object{
        fun from( c : KClass<*>) : Type {

            val requiredFields : List<String> =
                c.memberProperties.filter{ it is KMutableProperty1 && !it.returnType.isMarkedNullable }.map{ it.name }
            val properties : List<String> =
                c.memberProperties.mapNotNull {
                    it as? KMutableProperty1<*, *>
                }.map { prop -> "\"${prop.name}\": {"+ jsonPropertyTypeContent(prop.returnType) +"}" }
            return Type("""{
                "${"$"}schema": "http://json-schema.org/schema#",
                "title": "${c.simpleName}",
                "type": "object",
                "required": [${requiredFields.map { "\"$it\"" }.joinToString()}],
                "properties": {
                    ${properties.joinToString()}
                }
            }""")

        }
    }


}

