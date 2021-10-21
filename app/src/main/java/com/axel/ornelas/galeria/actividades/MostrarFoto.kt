package com.axel.ornelas.galeria.actividades

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.fragment.app.add
import androidx.fragment.app.commit
import com.axel.ornelas.galeria.Foto
import com.axel.ornelas.galeria.R
import com.axel.ornelas.galeria.fragmentos.MostrarFotoFragmento
import com.axel.ornelas.galeria.fragmentos.MostrarFotoFragmentoViewModel

class MostrarFoto : AppCompatActivity() {

    private val viewModel: MostrarFotoFragmentoViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mostrar_foto)
        val foto = intent.getSerializableExtra("foto") as Foto
        // Coloca el titulo de la app bar el nombre del album
        supportActionBar?.title = "Foto"
        // Indica que haya una flecha hacia atras en el app bar
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        //Agrega el fragmento que muestra la foto
        supportFragmentManager.commit {
            setReorderingAllowed(true)
            add<MostrarFotoFragmento>(R.id.mostrarFotoFragmento)
        }
        viewModel.seleccionarItem(foto)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            //Se usa para que no se ejecute el onBackPressed
            android.R.id.home -> {
                onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}