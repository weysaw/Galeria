package com.axel.ornelas.galeria.actividades

import android.app.AlertDialog
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputType
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import com.axel.ornelas.galeria.Album
import com.axel.ornelas.galeria.R
import com.axel.ornelas.galeria.adaptadores.AlbumAdaptador
import com.axel.ornelas.galeria.databinding.ActivityMainBinding
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import java.lang.Exception

class MainActivity : AppCompatActivity() {

    private var albumes: ArrayList<Album> = arrayListOf()
    private lateinit var binding: ActivityMainBinding
    private val nombreArchivo = "albumes"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // Se le indica el layout al reclycler
        binding.albumes.layoutManager = GridLayoutManager(this, 2)
        leerArchivoAlbumes()
        // Se crea un adaptador con el arreglo
        val adapter = AlbumAdaptador(albumes, menuInflater)
        // Se colocan las peliculas en el adaptador
        binding.albumes.adapter = adapter
        adapter.onClickListener = View.OnClickListener { v ->
            val pos: Int = binding.albumes.getChildAdapterPosition(v)
            Toast.makeText(this, pos.toString(), Toast.LENGTH_SHORT).show()
            /*val intent = Intent(this, InformacionPelicula::class.java)
            intent.putExtra("pelicula", pelis[pos])
            startActivity(intent)*/
        }
        registerForContextMenu(binding.albumes)
    }

    /**
     * Agrega un album presionando el boton inferior
     */
    fun agregarAlbum(view: View) {
        val albumTitulo = EditText(this)
        albumTitulo.hint = "Nombre Album"
        albumTitulo.inputType = InputType.TYPE_CLASS_TEXT
        //Crea el dialogo
        obtenerDialogo("Ingrese el titulo del nuevo album", "Nuevo Album")
            .setPositiveButton("Crear") { _, _ ->
                // Se obtiene el nombre del album
                val nombreAlbum: String = albumTitulo.text.toString()
                if (!verificarNombreAlbum(nombreAlbum)) return@setPositiveButton
                //Agrega el nuevo album
                albumes += Album(nombreAlbum)
                guardarAlbumes()
            }
            .setNegativeButton("Cancelar") { dialog, _ ->
                dialog.dismiss()
            }
            .setView(albumTitulo)
            .create().show()
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.eliminarAlbum -> {
                confirmarEliminacion()
                true
            }
            R.id.modificarAlbum -> {
                modificarAlbum()
                true
            }
            else -> super.onContextItemSelected(item)
        }
    }

    /**
     * Modifica el nombre del album
     */
    private fun modificarAlbum() {
        val albumTitulo = EditText(this)
        albumTitulo.hint = "Nuevo Nombre Album"
        albumTitulo.inputType = InputType.TYPE_CLASS_TEXT
        //Crea el dialogo
        obtenerDialogo("Ingrese el titulo nuevo del album", "Modificar Album")
            .setPositiveButton("Crear") { _, _ ->
                // Se obtiene el nombre del album
                val nombreAlbum: String = albumTitulo.text.toString()
                if (!verificarNombreAlbum(nombreAlbum)) return@setPositiveButton
                //Agrega el nuevo album
                val adaptador = (binding.albumes.adapter as AlbumAdaptador)
                val pos: Int = adaptador.pos
                albumes[pos].titulo = nombreAlbum
                adaptador.notifyItemChanged(pos)
                guardarAlbumes()
            }
            .setNegativeButton("Cancelar") { dialog, _ ->
                dialog.dismiss()
            }
            .setView(albumTitulo)
            .create().show()
    }

    /**
     * Verifica si el nombre es valido
     */
    private fun verificarNombreAlbum(nombreAlbum: String): Boolean {
        if (nombreAlbum.contains(",")) {
            Toast.makeText(
                applicationContext,
                "El nombre no debe contener \",\"",
                Toast.LENGTH_SHORT
            ).show()
            return false
        }
        return true
    }

    /**
     * Lee el archivo que contiene los albumes
     */
    private fun leerArchivoAlbumes() {
        try {
            applicationContext.openFileInput(nombreArchivo).use {
                val obInputS = ObjectInputStream(it)
                albumes = obInputS.readObject() as ArrayList<Album>
                obInputS.close()
                it.close()
            }
        } catch (ex: Exception) {
            println("\n\nArchivo no encontrado\n\n")
        }

    }


    /**
     * Guarda los cambios que se hayan hecho a los albumes
     */
    private fun guardarAlbumes() {
        //Guarda el archivo con los albumes
        applicationContext.openFileOutput(nombreArchivo, Context.MODE_PRIVATE).use {
            val os = ObjectOutputStream(it)
            os.writeObject(albumes)
            os.close()
            it.close()
        }
    }


    /**
     * Muesta un dialogo en el que pregunta si quiere eliminar el album
     */
    private fun confirmarEliminacion() {
        val adaptador = (binding.albumes.adapter as AlbumAdaptador)
        val pos: Int = adaptador.pos
        obtenerDialogo(
            "¿Esta seguro que quiere borrar el album de ${albumes[pos].titulo}?",
            "Eliminar Albumes"
        )
            .setPositiveButton("Eliminar") { _, _ ->
                //Se remueve el album y se notifica al recycler view
                albumes.removeAt(pos)
                adaptador.notifyItemRemoved(pos)
                // Se guardan los cambios
                guardarAlbumes()
            }
            .setNegativeButton("Cancelar") { dialog, _ ->
                dialog.dismiss()
            }
            .create().show()
    }

    /**
     * Devuelve el dialogo del que se puede configurar
     */
    private fun obtenerDialogo(mensaje: String, titulo: String): AlertDialog.Builder {
        //Crea el dialogo
        return AlertDialog.Builder(this)
            .setTitle(titulo)
            .setMessage(mensaje)
    }


}