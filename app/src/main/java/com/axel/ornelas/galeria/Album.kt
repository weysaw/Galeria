package com.axel.ornelas.galeria

import java.io.Serializable
import java.util.*


class Album(var titulo: String): Serializable {
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
    fun removerFoto(pos: Int) {
        fotos.removeAt(pos)
    }

}