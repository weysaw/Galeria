package com.axel.ornelas.galeria.adaptadores


import android.annotation.SuppressLint
import android.view.*
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.axel.ornelas.galeria.Album
import com.axel.ornelas.galeria.R
import android.view.ContextMenu.ContextMenuInfo

/**
 * https://newbedev.com/how-to-create-context-menu-for-recyclerview
 */
class FotosAdaptador(private val localDataSet: Album, private val menuInflater: MenuInflater?) :
    RecyclerView.Adapter<FotosAdaptador.ViewHolder>() {

    //Tal vez se deba poner protected
    lateinit var onClickListener: View.OnClickListener
    var pos: Int = 0
        private set
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        //Indica el estilo que debe tener el recycler
        val view = LayoutInflater.from(parent.context).inflate(R.layout.foto, parent, false)
        view.setOnClickListener(onClickListener)
        //Devuelve la vista creada
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, @SuppressLint("RecyclerView") position: Int) {
        val foto = localDataSet.fotos[position]
        holder.imagen.setImageURI(foto.direccionUri)
        holder.itemView.setOnLongClickListener {
            pos = position
            false
        }
    }

    /**
     *
     */
    override fun onViewRecycled(holder: ViewHolder) {
        holder.itemView.setOnLongClickListener(null)
        super.onViewRecycled(holder)
    }
    /**
     * Tamaño de las fotos
     */
    override fun getItemCount(): Int {
        return localDataSet.fotos.size
    }

    /**
     * Clase interna para localizar los datos que se necesitan para colocar la info
     */
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnCreateContextMenuListener {
        val imagen: ImageView = itemView.findViewById(R.id.imagenFoto)
        init {
            itemView.setOnCreateContextMenuListener(this)
        }
        override fun onCreateContextMenu(menu: ContextMenu?, v: View?, menuInfo: ContextMenuInfo?) {
            menuInflater?.inflate(R.menu.opciones_foto, menu)
            menu?.setHeaderTitle("Opciones Menu")
        }


    }
}