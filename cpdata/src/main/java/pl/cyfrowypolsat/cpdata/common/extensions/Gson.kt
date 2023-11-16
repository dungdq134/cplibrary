package pl.cyfrowypolsat.cpdata.common.extensions

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

inline fun <reified T> Gson.fromJson(json: String?) = if (json != null) {
    fromJson<T>(json, object : TypeToken<T>() {}.type)
} else {
    null
}
