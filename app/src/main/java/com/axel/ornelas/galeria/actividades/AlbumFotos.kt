package com.axel.ornelas.galeria.actividades

import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.os.bundleOf
import androidx.fragment.app.add
import androidx.fragment.app.commit
import com.axel.ornelas.galeria.Album
import com.axel.ornelas.galeria.R
import com.axel.ornelas.galeria.databinding.ActivityAlbumFotosBinding
import com.axel.ornelas.galeria.fragmentos.FotosFragmento
import com.axel.ornelas.galeria.fragmentos.MostrarFotoFragmento


private const val PARAM1 = "albumes"
private const val PARAM2 = "pos"

/**
 * Actividad que muestra las fotos de un album
 */
class AlbumFotos : AppCompatActivity() {

    private lateinit var binding: ActivityAlbumFotosBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAlbumFotosBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // Obtiene los albumes (se usa para guardar luego)
        val albumes = intent.getSerializableExtra(PARAM1) as ArrayList<Album>
        // Obtiene la posicion del album
        val pos = intent.getIntExtra(PARAM2, 0)
        // Coloca el titulo de la app bar el nombre del album
        supportActionBar?.title = albumes[pos].titulo
        // Indica que haya una flecha hacia atras en el app bar
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        // Realiza el paquete con los parametros para el fragmento
        val bundle = bundleOf(PARAM1 to albumes, PARAM2 to pos)

        when (resources.configuration.orientation) {
            Configuration.ORIENTATION_LANDSCAPE -> {
                //Agrega los 2 fragmentos con las imagenes, otro que muestra cual esta viendo
                supportFragmentManager.commit {
                    add<FotosFragmento>(R.id.albumFotoFragmento, args = bundle)
                    add<MostrarFotoFragmento>(R.id.mostrarFotoLand)
                }
            }//Cuando esta en portrait
            else -> {
                supportFragmentManager.commit {
                    setReorderingAllowed(true)
                    add<FotosFragmento>(R.id.albumFotoFragmento, args = bundle)
                }
            }
        }
    }
}