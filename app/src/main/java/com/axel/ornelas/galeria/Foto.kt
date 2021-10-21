package com.axel.ornelas.galeria

import java.io.Serializable
import java.util.*

data class Foto(
    val direccionUri: String,
    var descripcion: String,
    var fecha: Long = Date().time
) : Serializable
