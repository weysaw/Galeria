package com.axel.ornelas.galeria.actividades

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.os.bundleOf
import androidx.fragment.app.add
import androidx.fragment.app.commit
import com.axel.ornelas.galeria.Album
import com.axel.ornelas.galeria.R
import com.axel.ornelas.galeria.databinding.ActivityAlbumFotosBinding
import com.axel.ornelas.galeria.fragmentos.FotosFragmento

class AlbumFotos : AppCompatActivity() {

    private lateinit var binding: ActivityAlbumFotosBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAlbumFotosBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val album = intent.getSerializableExtra("album") as Album
        supportActionBar?.title = album.titulo
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        val bundle = bundleOf("album" to album)
        supportFragmentManager.commit {
            setReorderingAllowed(true)
            add<FotosFragmento>(R.id.fragmentContainerView, args = bundle)
        }
    }
}