package com.axel.ornelas.galeria.fragmentos

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.text.InputType
import android.view.*
import android.widget.EditText
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.axel.ornelas.galeria.Album
import com.axel.ornelas.galeria.Foto
import com.axel.ornelas.galeria.R
import com.axel.ornelas.galeria.actividades.MostrarFoto
import com.axel.ornelas.galeria.adaptadores.FotosAdaptador
import com.axel.ornelas.galeria.databinding.FragmentFotosFragmentoBinding
import java.io.ObjectOutputStream
import java.lang.Exception
import java.util.*
import kotlin.collections.ArrayList

private const val ARG_PARAM1 = "albumes"
private const val ARG_PARAM2 = "pos"
private const val nombreArchivo = "albumes"

class FotosFragmento : Fragment() {

    private lateinit var binding: FragmentFotosFragmentoBinding
    private lateinit var albumes: ArrayList<Album>
    private lateinit var album: Album
    private lateinit var adapter: FotosAdaptador
    private val viewModel: MostrarFotoFragmentoViewModel by activityViewModels()
    private val obtenerImagen =
        registerForActivityResult(ActivityResultContracts.OpenDocument()) { uri ->
            //Verifica si hay un dato repetido
            if (album.fotos.any { foto -> foto.direccionUri == uri.toString() }) {
                println("Dato repetido")
                return@registerForActivityResult
            }
            //Garantiza que se pueda abrir la imagen en otra actividad
            val takeFlags: Int = Intent.FLAG_GRANT_READ_URI_PERMISSION
            activity?.applicationContext?.contentResolver?.takePersistableUriPermission(
                uri,
                takeFlags
            )
            agregarFoto(uri.toString())
        }


    override fun onContextItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.eliminarFoto -> {
                eliminarFoto()
                true
            }
            R.id.modificarFoto -> {
                modificarFoto()
                true
            }
            else -> super.onContextItemSelected(item)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentFotosFragmentoBinding.inflate(inflater, container, false)
        registerForContextMenu(binding.fotosRecycler)
        // Obtiene los parametros
        arguments.let {
            albumes = it?.getSerializable(ARG_PARAM1) as ArrayList<Album>
            val pos = it.getInt(ARG_PARAM2)
            album = albumes[pos]
        }
        adapter = FotosAdaptador(album, activity?.menuInflater)
        // Se colocan las peliculas en el adaptador
        binding.fotosRecycler.layoutManager = GridLayoutManager(activity, 3)
        binding.fotosRecycler.adapter = adapter

        adapter.onClickListener = View.OnClickListener { v ->
            val pos: Int = binding.fotosRecycler.getChildAdapterPosition(v)
            when (resources.configuration.orientation) {
                Configuration.ORIENTATION_LANDSCAPE -> {
                    //Muestra la foto en el 2do fragmento
                    viewModel.seleccionarItem(album.fotos[pos])
                }
                else -> {
                    //Inicia la actividad donde muestra la actividad
                    val intent = Intent(activity, MostrarFoto::class.java)
                    val foto: Foto = album.fotos[pos]
                    intent.putExtra("foto", foto)
                    startActivity(intent)
                }
            }
        }
        binding.floatingActionButton.setOnClickListener {
            obtenerFoto()
        }
        return binding.root
    }


    /**
     * Agrega la foto a los albumes
     */
    private fun agregarFoto(uri: String) {
        val editDescripcion = EditText(activity)
        editDescripcion.hint = "Descripción Imagen"
        editDescripcion.inputType = InputType.TYPE_CLASS_TEXT

        obtenerDialogo("Ingrese la descripción de la imagen", "Agregar Imagen")
            .setPositiveButton("Aceptar") { _, _ ->
                val descripcion = editDescripcion.text.toString()
                //Coloca la dirección de la imagen y la agrega
                album.agregarFoto(Foto(uri, descripcion, Date().time))
                //Se notifica al recylcler view la imagen agregada
                adapter.notifyItemInserted(adapter.itemCount)
                guardarAlbumes()
            }
            .setNegativeButton("Cancelar") { dialog, _ -> dialog.dismiss() }
            .setView(editDescripcion)
            .create()
            .show()
    }

    /**
     * Modifica la descripcion de la foto
     */
    private fun modificarFoto() {
        //Obtiene la foto a modificar
        val foto = album.fotos[adapter.pos]
        val editDescripcion = EditText(activity)
        editDescripcion.hint = "Nueva Descripción Imagen"
        editDescripcion.text.insert(0, foto.descripcion)
        editDescripcion.inputType = InputType.TYPE_CLASS_TEXT

        obtenerDialogo("Ingrese la descripción nueva de la imagen", "Agregar Imagen")
            .setPositiveButton("Modificar") { _, _ ->
                val descripcion = editDescripcion.text.toString()
                //Modifica la descripcion de la foto
                foto.descripcion = descripcion
                foto.fecha = Date().time
                //Se notifica al recylcler view la imagen agregada
                adapter.notifyItemChanged(adapter.pos)
                if (viewModel.selectedItem.value?.direccionUri == foto.direccionUri)
                    viewModel.seleccionarItem(foto)
                guardarAlbumes()
            }
            .setNegativeButton("Cancelar") { dialog, _ -> dialog.dismiss() }
            .setView(editDescripcion)
            .create()
            .show()
    }

    /**
     * Obtiene la uri de la imagen
     */
    private fun obtenerFoto() {
        try {
            obtenerImagen.launch(arrayOf("image/*"))
        } catch (e: Exception) {
            Toast.makeText(activity, "Error en la dirección de la imagen", Toast.LENGTH_SHORT)
                .show()
        }
    }

    /**
     * Obtiene el dialogo para mostrar
     */
    private fun obtenerDialogo(mensaje: String, titulo: String): AlertDialog.Builder {
        //Crea el dialogo
        return AlertDialog.Builder(activity)
            .setTitle(titulo)
            .setMessage(mensaje)
    }

    /**
     * Guarda los cambios que se hayan hecho a los albumes
     */
    private fun guardarAlbumes() {
        //Guarda el archivo con los albumes
        val contexto = requireContext()
        contexto.openFileOutput(nombreArchivo, Context.MODE_PRIVATE).use { fo ->
            val os = ObjectOutputStream(fo)
            os.writeObject(albumes)
            os.close()
            fo?.close()
        }
    }

    /**
     * Muesta un dialogo en el que pregunta si quiere eliminar el album
     */
    private fun eliminarFoto() {
        val pos: Int = adapter.pos
        obtenerDialogo(
            "¿Esta seguro que quiere borrar el album de ${albumes[pos].titulo}?",
            "Eliminar Albumes"
        )
            .setPositiveButton("Eliminar") { _, _ ->
                //Se remueve el album y se notifica al recycler view
                album.removerFoto(pos)
                adapter.notifyItemRemoved(pos)
                // Se guardan los cambios
                guardarAlbumes()
            }
            .setNegativeButton("Cancelar") { dialog, _ ->
                dialog.dismiss()
            }
            .create().show()
    }
}