package com.airline.android.model

import com.google.gson.Gson
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

/**
 * Type tokens needed for Gson deserialization of generic types
 */
val listRoutesType: Type = object : TypeToken<List<Route>>() {}.type
val listFLightsType: Type = object : TypeToken<List<Flight>>() {}.type
val listTicketsType: Type = object : TypeToken<List<Ticket>>() {}.type

/**
 * Helper class, represents API response
 */
data class JsendResponse(
    var status: String,
    var data: Data?,
    var listData: List<Data>?,
    var message: String?
)

/**
 * Helper class, represents login response
 */
data class LoginResponse(
    val sessionKey: String,
    val userId: Int
) : Data

data class UserIdResponse(
    val userId: Int
) : Data

/**
 * Helper class, represents fail data
 */
data class JsendFail(var message: String) : Data

/**
 * Interface-marker for classes, that can be deserialized
 */
interface Data

/**
 * Custom deserializer for Gson
 */
class JsendDeserializer : JsonDeserializer<JsendResponse> {
    override fun deserialize(
        json: JsonElement,
        typeOfT: Type,
        context: JsonDeserializationContext
    ): JsendResponse {
        val jsendObject = json.asJsonObject

        val gson = Gson()

        val status = jsendObject.get("status").asString

        val result = JsendResponse(status, null, null, null)

        when (status) {
            "success" -> {
                val data = jsendObject.get("data")
                if (data.asJsonObject.has("routes")) {
                    val e = data.asJsonObject.get("routes").asJsonArray
                    result.listData = gson.fromJson(e, listRoutesType)
                } else if (data.asJsonObject.has("flights")) {
                    val e = data.asJsonObject.get("flights").asJsonArray
                    result.listData = gson.fromJson(e, listFLightsType)
                } else if (data.asJsonObject.has("tickets")) {
                    val e = data.asJsonObject.get("tickets").asJsonArray
                    result.listData = gson.fromJson(e, listTicketsType)
                } else {
                    when {
                        data.asJsonObject.has("route") -> result.data =
                                gson.fromJson(data.asJsonObject.get("route"), Route::class.java)
                        data.asJsonObject.has("flight") -> result.data =
                                gson.fromJson(data.asJsonObject.get("flight"), Flight::class.java)
                        data.asJsonObject.has("sessionKey") -> result.data =
                                gson.fromJson(data.asJsonObject, LoginResponse::class.java)
                        data.asJsonObject.has("userId") -> result.data =
                                gson.fromJson(data.asJsonObject, UserIdResponse::class.java)
                    }
                }
            }
            "fail" -> {
                val data = jsendObject.get("data")
                result.data = gson.fromJson(data, JsendFail::class.java)
            }
            "error" -> {
                result.message = jsendObject.get("message").asString
            }
        }
        return result
    }
}
