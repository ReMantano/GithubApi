package com.example.kotlingithubapi.adapter


import com.google.gson.Gson
import com.google.gson.TypeAdapter
import com.google.gson.TypeAdapterFactory
import com.google.gson.reflect.TypeToken
import java.lang.reflect.ParameterizedType


class ArrayAdapterFactory : TypeAdapterFactory {
    override fun <T> create(gson: Gson, type: TypeToken<T>): TypeAdapter<T>? {

        var typeAdapter: TypeAdapter<T>? = null

        try {
            val param : ParameterizedType = type.type as ParameterizedType
            val clas : Class<T> = param.getActualTypeArguments()[0] as Class<T>
            if (type.rawType == List::class.java)
                typeAdapter = ArrayAdapter<T>(clas) as TypeAdapter<T>?
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return typeAdapter


    }
}