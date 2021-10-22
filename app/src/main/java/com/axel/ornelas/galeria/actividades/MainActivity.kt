package com.axel.ornelas.galeria.actividades

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
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
import com.axel.ornelas.galeria.adaptadores.AlbumesAdaptador
import com.axel.ornelas.galeria.databinding.ActivityMainBinding
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import java.lang.Exception

private const val nombreArchivo = "albumes"

/**
 *
 * https://www.youtube.com/watch?v=HMjI7cLsyfw
 * https://proandroiddev.com/serializable-or-parcelable-why-and-which-one-17b274f3d3bb
 */
class MainActivity : AppCompatActivity() {

    private var albumes: ArrayList<Album> = arrayListOf()
    private lateinit var adapter: AlbumesAdaptador
    private lateinit var binding: ActivityMainBinding
    private var actualizarAlbum = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // Se crea un adaptador con el arreglo
        adapter = AlbumesAdaptador(albumes, menuInflater)
        // Se le indica el layout al reclycler
        binding.albumes.layoutManager =
            if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE)
                GridLayoutManager(this, 3)
            else
                GridLayoutManager(this, 2)
        // Se colocan las peliculas en el adaptador
        binding.albumes.adapter = adapter
        adapter.onClickListener = View.OnClickListener { v ->
            actualizarAlbum = binding.albumes.getChildAdapterPosition(v)
            val intent = Intent(this, AlbumFotos::class.java)
            intent.putExtra("albumes", albumes)
            intent.putExtra("pos", actualizarAlbum)
            startActivity(intent)
        }
        registerForContextMenu(binding.albumes)
    }

    /**
     * Cuando se resume la actividad actualiza las portadas de los albumes
     */
    override fun onResume() {
        leerArchivoAlbumes()
        adapter.notifyItemChanged(actualizarAlbum)
        super.onResume()
    }

    /**
     * Opciones del album cuando se deja presionado
     */
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
     * Agrega un album presionando el boton inferior
     */
    fun agregarAlbum(v: View) {
        val albumTitulo = EditText(this)
        albumTitulo.hint = "Nombre Álbum"
        albumTitulo.inputType = InputType.TYPE_CLASS_TEXT
        //Crea el dialogo
        obtenerDialogo("Ingrese el titulo del nuevo álbum", "Nuevo Älbum")
            .setPositiveButton("Crear") { _, _ ->
                // Se obtiene el nombre del album
                val nombreAlbum: String = albumTitulo.text.toString()
                if (!verificarNombreAlbum(nombreAlbum)) return@setPositiveButton
                //Agrega el nuevo album
                albumes += Album(nombreAlbum)
                adapter.notifyItemInserted(albumes.size - 1)
                guardarAlbumes()
            }
            .setNegativeButton("Cancelar") { dialog, _ ->
                dialog.dismiss()
            }
            .setView(albumTitulo)
            .create()
            .show()
    }


    /**
     * Modifica el nombre del album
     */
    private fun modificarAlbum() {
        val album = albumes[adapter.pos]
        val albumTitulo = EditText(this)
        albumTitulo.text.insert(0, album.titulo)
        albumTitulo.hint = "Nuevo Nombre Album"
        albumTitulo.inputType = InputType.TYPE_CLASS_TEXT
        //Crea el dialogo
        obtenerDialogo("Ingrese el titulo nuevo del álbum", "Modificar Álbum")
            .setPositiveButton("Crear") { _, _ ->
                // Se obtiene el nombre del album
                val nombreAlbum: String = albumTitulo.text.toString()
                if (!verificarNombreAlbum(nombreAlbum)) return@setPositiveButton
                album.titulo = nombreAlbum
                adapter.notifyItemChanged(adapter.pos)
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
        val toast = Toast.makeText(
            applicationContext,
            "El nombre no debe estar vacio",
            Toast.LENGTH_SHORT
        )
        return when {
            nombreAlbum.isBlank() || nombreAlbum.isEmpty() -> {
                toast.show()
                false
            }
            albumes.any { album -> album.titulo == nombreAlbum } -> {
                toast.setText("El nombre de este album ya existe")
                toast.show()
                false
            }
            else -> true
        }

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
            adapter.localDataSet = albumes
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
        obtenerDialogo(
            "¿Esta seguro que quiere borrar el álbum \"${albumes[adapter.pos].titulo}\"?",
            "Eliminar Albumes"
        )
            .setPositiveButton("Eliminar") { _, _ ->
                //Se remueve el album y se notifica al recycler view
                albumes.removeAt(adapter.pos)
                adapter.notifyItemRemoved(adapter.pos)
                // Se guardan los cambios
                guardarAlbumes()
            }
            .setNegativeButton("Cancelar") { dialog, _ ->
                dialog.dismiss()
            }
            .create()
            .show()
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