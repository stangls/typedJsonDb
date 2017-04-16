[![Build Status](https://travis-ci.org/stangls/typedJsonDb.svg?branch=master)](https://travis-ci.org/stangls/typedJsonDb)

# typedJsonDb
An easy to use, scalable and schemaful noSQL-database with easy to write class-to-schema mappings written in Kotlin.

The main idea of this database: Most programmers just want to persist simple data classes which are already typesafe and interconnected.

What this database is **not** doing:
* the SQL approach: require writing table structures, N:M-tables and O/R mappings.
* most of the NoSQL approach: store indexable text-blobs without a type, where structural changes are done by the client on all the data.

What this database should be/do (or wants to be/do):
* easy to use.
* based on standards.
* so far based on other established databases (interchangably) used as simple data-stores for meta- and client-data
* persist existing (kotlin, but also other) datastructures with as few lines of code as possible
* retrive data using select-queries (no joins required)
* speed queries up by user-defined index structures
* fetch results
** eagerly
** lazily
** row-by-row (iterable like JDBC's ResultSet)
** in-background (i.e. eagerly fetch lazy links if load is low, for faster results)
* allow multiple clients with (slightly) different datamodels / needs
* client-server architecture and monolithic architecture with one interface
* scalability
* allow easy as well as powerfull structural migrations. if the datastructure changes:
** either nothing has to be done (e.g. removal of a field can be done automatically)
** define what is the default (e.g. new field has default value or is based on other fields' values)
** supply code to do perform the structural change on data

What this database might become in the long run:
* compatible to SQL
* used by other programming languages (java, python, c++, typescript, ...)
