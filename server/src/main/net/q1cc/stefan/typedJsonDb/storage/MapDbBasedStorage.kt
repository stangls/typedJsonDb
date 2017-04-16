package net.q1cc.stefan.typedJsonDb.storage

import org.mapdb.DBMaker
import org.mapdb.Serializer
import java.io.File
import java.util.concurrent.ConcurrentMap

class MapDbBasedStorage(
    path: File
) : DatabaseStorage() {

    init{
        if (!path.exists()){
            path.mkdirs()
        }
        if (!path.isDirectory){
            throw Exception("$path must be a directory")
        }
    }

    val metaDb = DBMaker
            .fileDB(path.absolutePath+"/meta.db")
            .fileMmapEnable()
            .make()

    override fun close() {
        metaDb.close()
    }

    override val serializedTypes: ConcurrentMap<String, String>
        = metaDb.hashMap("types", Serializer.STRING, Serializer.STRING).createOrOpen()

}