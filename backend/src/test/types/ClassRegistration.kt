package types

import aTestWith
import org.junit.Assert.*
import net.q1cc.stefan.typedJsonDb.Database
import net.q1cc.stefan.typedJsonDb.storage.MapDbBasedStorage
import net.q1cc.stefan.typedJsonDb.typing.Type
import org.everit.json.schema.ArraySchema
import org.everit.json.schema.NumberSchema
import org.everit.json.schema.ObjectSchema
import org.everit.json.schema.StringSchema
import org.junit.After
import org.junit.AfterClass
import org.junit.Assert
import org.junit.Test
import java.io.File
import kotlin.reflect.KClass

/**
 * Created by stefan on 15.04.17.
 */


class ClassRegistration {

    val dbDir = File.createTempFile(javaClass.simpleName, "").apply { delete() }
    val db = Database(MapDbBasedStorage(dbDir))

    @After
    fun cleanup() {
        dbDir.deleteRecursively()
    }

    @Test
    fun emptyType() {
        db.aTestWith(Empty::class) {
            println(type.jsonStringPretty)
            assertEquals("types.Empty", type.schema.title)
            assertTrue(schema.requiredProperties.isEmpty())
            assertTrue(schema.propertySchemas.isEmpty())
        }
    }


    @Test
    fun justAString() {
        db.aTestWith(JustAString::class) {
            println(type.jsonStringPretty)
            assertEquals("types.JustAString", type.schema.title )
            assertEquals(listOf("content"), schema.requiredProperties)
            assertEquals(listOf("content"), schema.propertySchemas.map { it.key })
            assertEquals(schema.propertySchemas.size,1)
            schema.propertySchemas.forEach{
                assertTrue(it.value is StringSchema)
            }
        }
    }

    @Test
    fun optionalString() {
        db.aTestWith(OptionalString::class) {
            println(type.jsonStringPretty)
            assertEquals("types.OptionalString",type.schema.title)
            assertEquals(listOf<String>(), schema.requiredProperties)
            assertEquals(listOf("content"), schema.propertySchemas.map { it.key })
            assertEquals(schema.propertySchemas.size,1)
            schema.propertySchemas.forEach{
                assertTrue(it.value is StringSchema)
            }
        }
    }

    @Test
    fun listOfInts() {
        db.aTestWith(IntList::class) {
            println(type.jsonStringPretty)
            assertEquals("types.IntList", type.schema.title)
            assertEquals(listOf<String>("ints"), schema.requiredProperties)
            assertEquals(listOf("ints"), schema.propertySchemas.map { it.key })
            assertEquals(schema.propertySchemas.size, 1)
            schema.propertySchemas.forEach {
                assertTrue(it.value is ArraySchema)
                val subSchema = it.value as ArraySchema
                assertTrue(subSchema.allItemSchema is NumberSchema)
                assertTrue(subSchema.permitsAdditionalItems())
                assertTrue(subSchema.requiresArray())
            }
        }
    }

    @Test
    fun nested() {
        db.aTestWith(EmptyOptional::class) {
            println(type.jsonStringPretty)
            assertEquals("types.EmptyOptional",type.schema.title)
            assertEquals(listOf<String>(), schema.requiredProperties)
            assertEquals(listOf("empty"), schema.propertySchemas.map { it.key })
            assertEquals(schema.propertySchemas.size,1)
            schema.propertySchemas.forEach {
                assertTrue(it.value is ObjectSchema)
                val subSchema = it.value as ObjectSchema
                assertEquals("types.Empty",subSchema.title)
                assertTrue(subSchema.requiresObject())
                assertEquals(1,subSchema.propertySchemas.size)
                assertEquals(listOf("id"),subSchema.propertySchemas.map{ it.key })
                assertTrue(subSchema.propertySchemas.values.all{ it is NumberSchema })
            }
        }
    }

}

class IntList {
    var ints = listOf<Int>()
}

class EmptyOptional {
    var empty : Empty? = null
}