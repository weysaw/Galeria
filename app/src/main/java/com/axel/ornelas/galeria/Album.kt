package com.axel.ornelas.galeria

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.io.Serializable
import java.util.*

@Parcelize
class Album(var titulo: String): Parcelable, Serializable {
    val fotos: ArrayList<Foto> = arrayListOf()

    /**
     * Agrega una foto al album
     */
    fun agregarFoto(foto: Foto) {
        fotos += foto
    }

    /**
     * Remueve una foto al album
     */
    fun removerFoto(foto: Foto) {
        fotos.remove(foto)
    }

}