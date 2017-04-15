package net.q1cc.stefan.typedJsonDb.test

import org.junit.Assert.*
import net.q1cc.stefan.typedJsonDb.Database
import net.q1cc.stefan.typedJsonDb.storage.MapDbBasedStorage
import net.q1cc.stefan.typedJsonDb.typing.Type
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


class Types {

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
            Assert.assertEquals("Empty", type.schema.title)
            assertTrue(schema.requiredProperties.isEmpty())
            assertTrue(schema.propertySchemas.isEmpty())
        }
    }


    @Test
    fun justAString() {
        db.aTestWith(JustAString::class) {
            println(type.jsonStringPretty)
            Assert.assertEquals("JustAString", type.schema.title )
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
            Assert.assertEquals("OptionalString",type.schema.title)
            assertEquals(listOf<String>(), schema.requiredProperties)
            assertEquals(listOf("content"), schema.propertySchemas.map { it.key })
            assertEquals(schema.propertySchemas.size,1)
            schema.propertySchemas.forEach{
                assertTrue(it.value is StringSchema)
            }
        }
    }
}

class Empty

class JustAString {
    var content : String = ""
}
class OptionalString {
    var content : String? = ""
}