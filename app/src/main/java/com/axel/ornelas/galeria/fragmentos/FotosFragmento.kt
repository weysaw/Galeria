package com.axel.ornelas.galeria.fragmentos

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.axel.ornelas.galeria.Album
import com.axel.ornelas.galeria.Foto
import com.axel.ornelas.galeria.R
import com.axel.ornelas.galeria.adaptadores.FotosAdaptador
import com.axel.ornelas.galeria.databinding.FragmentFotosFragmentoBinding
import java.lang.Exception
import java.util.*

private const val ARG_PARAM1 = "album"

class FotosFragmento : Fragment() {
    private lateinit var binding: FragmentFotosFragmentoBinding
    private lateinit var album: Album
    private lateinit var adapter: FotosAdaptador
    private val obtenerImagen = registerForActivityResult(ActivityResultContracts.OpenDocument()) {
        //Garantiza que se pueda abrir la imagen en otra actividad
        val takeFlags: Int = Intent.FLAG_GRANT_READ_URI_PERMISSION
        activity?.applicationContext?.contentResolver?.takePersistableUriPermission(it, takeFlags)
        //Coloca la dirección de la imagen y la agrega
        album.agregarFoto(Foto(it, "Esta chido", Date().time))
        adapter.notifyDataSetChanged()

    }


    override fun onContextItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.eliminarFoto -> {

                true
            }
            R.id.modificarFoto -> {

                true
            }
            else -> super.onContextItemSelected(item)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentFotosFragmentoBinding.inflate(inflater, container, false)
        registerForContextMenu(binding.fotosRecycler)
        arguments.let {
            album = it?.getParcelable<Album>(ARG_PARAM1) as Album
        }
        adapter = FotosAdaptador(album, activity?.menuInflater)
        // Se colocan las peliculas en el adaptador
        binding.fotosRecycler.layoutManager = GridLayoutManager(activity, 3)
        binding.fotosRecycler.adapter = adapter
        adapter.onClickListener = View.OnClickListener { v ->

        }
        binding.floatingActionButton.setOnClickListener {
            agregarFoto()
        }
        return binding.root
    }

    /**
     *
     */
    private fun agregarFoto() {
        try {
            obtenerImagen.launch(arrayOf("image/*"))
        } catch (e: Exception) {
            Toast.makeText(activity, "Error en la dirección de la imagen", Toast.LENGTH_SHORT)
                .show()
        }
    }

    /**
     *
     */
    private fun obtenerDialogo(mensaje: String, titulo: String): AlertDialog.Builder {
        //Crea el dialogo
        return AlertDialog.Builder(activity)
            .setTitle(titulo)
            .setMessage(mensaje)
    }
}