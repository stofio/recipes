package com.recipegenerator.domain.extensions

import org.json.JSONArray

fun JSONArray.toStringList(): List<String> {
    val list = mutableListOf<String>()
    for (i in 0 until this.length()) {
        list.add(this.getString(i))
    }
    return list
}