package com.axel.ornelas.galeria.fragmentos

import android.content.res.Configuration
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.activityViewModels
import com.axel.ornelas.galeria.databinding.MostrarFotoFragmentoBinding
import com.bumptech.glide.Glide
import java.util.*

/**
 * Muestra la foto del fragmento
 */
class MostrarFotoFragmento : Fragment() {

    private val viewModel: MostrarFotoFragmentoViewModel by activityViewModels()
    private lateinit var binding: MostrarFotoFragmentoBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = MostrarFotoFragmentoBinding.inflate(inflater, container, false)
        binding.fotoMostrado.layoutParams = when (resources.configuration.orientation) {
            Configuration.ORIENTATION_LANDSCAPE -> LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                700
            )
            //Cuando esta en portrait
            else -> LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 1400)
        }
        // Cada vez que se seleccione la foto en el otro fragmento aqui se refleja
        viewModel.selectedItem.observe(viewLifecycleOwner, { foto ->
            val direccion = Uri.parse(foto.direccionUri)
            // Coloca la nueva imagen selecciona
            Glide.with(this)
                .load(direccion)
                .into(binding.fotoMostrado)
            binding.descripcionFoto.text = foto.descripcion
            binding.fechaFoto.text = Date(foto.fecha).toString()
        })
        return binding.root
    }
}