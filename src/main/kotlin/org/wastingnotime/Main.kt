package org.wastingnotime

import com.google.gson.Gson
import spark.Spark.*
import java.util.*

val contacts = arrayListOf(
    Contact(UUID.randomUUID().toString(), "Albert", "Einstein", "1111-1111"),
    Contact(UUID.randomUUID().toString(), "Marie", "Curie", "2222-1111")
)

fun main(args: Array<String>) {
    port(8010)
    val gson = Gson()

    post("/contacts") { req, res ->
        val contact = gson.fromJson(req.body(), Contact::class.java)
        contact.id = UUID.randomUUID().toString()

        contacts.add(contact)

        res.status(201)
        res.header("Location", "/" + contact.id)
        ""
    }

    get("/contacts", { _, res ->
        res.status(200)
        res.type("application/json")
        contacts
    }) { src: Any? -> gson.toJson(src) }

    get("/contacts/:id", { req, res ->
        val id = req.params(":id")

        val contact = contacts.find { it.id == id }
        if (contact == null)
            halt(404)

        res.status(200)
        res.type("application/json")
        contact
    }) { src: Any? -> gson.toJson(src) }

    put("/contacts/:id") { req, res ->
        val id = req.params(":id")
        val contact = gson.fromJson(req.body(), Contact::class.java)
        if (contact == null)
            halt(400)

        val currentContact = contacts.find { it.id == id }
        if (currentContact == null)
            halt(404)

        currentContact!!.firstName = contact.firstName
        currentContact.lastName = contact.lastName
        currentContact.phoneNumber = contact.phoneNumber

        res.status(204)
        ""
    }

    delete("/contacts/:id") { req, res ->
        val id = req.params(":id")

        val contact = contacts.find { it.id == id }
        if (contact == null)
            halt(404)

        contacts.remove(contact)

        res.status(204)
        ""
    }
}
