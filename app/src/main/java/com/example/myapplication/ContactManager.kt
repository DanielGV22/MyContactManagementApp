package com.example.myapplication.data

import android.content.Context
import org.json.JSONArray
import org.json.JSONObject

class ContactManager(ctx: Context) {
    private val prefs = ctx.getSharedPreferences("contacts", Context.MODE_PRIVATE)
    private val KEY = "contact_list"

    fun getAll(): List<Contact> {
        val json = prefs.getString(KEY, "[]")!!
        val arr = JSONArray(json)
        return (0 until arr.length()).map { i ->
            val obj = arr.getJSONObject(i)
            Contact(
                obj.getString("name"),
                obj.getString("bio"),
                obj.getString("email"),
                obj.getString("phone"),
                obj.getString("website")
            )
        }
    }

    fun saveAll(list: List<Contact>) {
        val arr = JSONArray()
        list.forEach {
            arr.put(JSONObject().apply {
                put("name", it.name)
                put("bio", it.bio)
                put("email", it.email)
                put("phone", it.phone)
                put("website", it.website)
            })
        }
        prefs.edit().putString(KEY, arr.toString()).apply()
    }

    fun add(contact: Contact) {
        val list = getAll().toMutableList()
        list.add(contact)
        saveAll(list)
    }

    fun remove(index: Int) {
        val list = getAll().toMutableList()
        if (index in list.indices) {
            list.removeAt(index)
            saveAll(list)
        }
    }
}