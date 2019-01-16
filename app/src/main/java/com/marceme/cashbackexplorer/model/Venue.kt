package com.marceme.cashbackexplorer.model

data class Venue(val id: Int,
    val uuid: String,
    val name: String,
    val city: String,
    val cashback: Double,
    val lat: Double,
    val long: Double,
    val user_id: Int, val created_at: String, val updated_at: String)