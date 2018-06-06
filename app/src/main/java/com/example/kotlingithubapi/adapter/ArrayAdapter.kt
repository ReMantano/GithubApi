package com.example.kotlingithubapi.adapter


import com.google.gson.GsonBuilder
import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonToken
import com.google.gson.stream.JsonWriter
import java.io.IOException



class ArrayAdapter<T>(clas: Class<T>) : TypeAdapter<List<T>>() {

    private  val  adapterclass: Class<T> = clas



    @Throws(IOException::class)
    override fun read(reader: JsonReader): List<T> {

        val list = ArrayList<T>()

        val gson = GsonBuilder()
                .registerTypeAdapterFactory(ArrayAdapterFactory())
                .create()

        if (reader.peek() === JsonToken.BEGIN_OBJECT) {
            val inning = gson.fromJson<T>(reader, adapterclass)
            list.add(inning)

        } else if (reader.peek() === JsonToken.BEGIN_ARRAY) {

            reader.beginArray()
            while (reader.hasNext()) {
                val inning = gson.fromJson<T>(reader, adapterclass)
                list.add(inning)
            }
            reader.endArray()

        }

        return list
    }

    @Throws(IOException::class)
    override fun write(writer: JsonWriter, value: List<T>) {

    }
}