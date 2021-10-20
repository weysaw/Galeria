package com.axel.ornelas.galeria

import android.net.Uri
import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.io.Serializable
import java.util.*

@Parcelize
data class Foto(
    val direccionUri: Uri,
    val descripcion: String,
    val fecha: Long = Date().time
) : Parcelable, Serializable
