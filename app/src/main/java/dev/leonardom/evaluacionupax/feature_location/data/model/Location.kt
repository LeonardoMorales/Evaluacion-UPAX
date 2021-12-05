package dev.leonardom.evaluacionupax.feature_location.data.model

import com.google.firebase.Timestamp

/*
    Modelo de Información de Localización para interactuar con Firebase
 */
data class Location(
    val latitude: Double,
    val longitude: Double,
    val timestamp: Timestamp?
){

    // Requisito de Firebase para la creación de Objeto a partir de la información recibida desede Firebase
    constructor() : this(0.0, 0.0, null)

}
