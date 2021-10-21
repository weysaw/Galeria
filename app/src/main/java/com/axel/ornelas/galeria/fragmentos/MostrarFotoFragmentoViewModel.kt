package com.axel.ornelas.galeria.fragmentos

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.axel.ornelas.galeria.Foto

class MostrarFotoFragmentoViewModel : ViewModel() {
    val listaDeFotosMutable = MutableLiveData<Foto>()
    val selectedItem: LiveData<Foto> get() = listaDeFotosMutable

    fun seleccionarItem(item: Foto) {
        listaDeFotosMutable.value = item
    }
}