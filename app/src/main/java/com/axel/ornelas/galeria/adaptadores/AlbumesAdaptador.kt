package com.axel.ornelas.galeria.adaptadores


import android.net.Uri
import android.view.*
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.axel.ornelas.galeria.Album
import com.axel.ornelas.galeria.R
import android.view.ContextMenu.ContextMenuInfo

/**
 * https://newbedev.com/how-to-create-context-menu-for-recyclerview
 */
class AlbumesAdaptador(private val localDataSet: ArrayList<Album>, private val menuInflater: MenuInflater) :
    RecyclerView.Adapter<AlbumesAdaptador.ViewHolder>() {

    //Tal vez se deba poner protected
    lateinit var onClickListener: View.OnClickListener
    var pos: Int = 0
        private set
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        //Indica el estilo que debe tener el recycler
        val view = LayoutInflater.from(parent.context).inflate(R.layout.album, parent, false)
        view.setOnClickListener(onClickListener)
        //Devuelve la vista creada
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        //Coloca la información de las peliculas a los campos de texto
        with(localDataSet[position].fotos) {
            if (this.isEmpty())
                holder.imagen.setImageResource(R.drawable.folder)
            else {
                val direccion: Uri = localDataSet[position].fotos[0].direccionUri
                holder.imagen.setImageURI(direccion)
            }
        }
        holder.titulo.text = localDataSet[position].titulo
        holder.itemView.setOnLongClickListener {
            pos = position
            false
        }
    }

    override fun onViewRecycled(holder: ViewHolder) {
        holder.itemView.setOnLongClickListener(null)
        super.onViewRecycled(holder)
    }
    /**
     * Tamaño de los datos
     */
    override fun getItemCount(): Int {
        return localDataSet.size
    }

    /**
     * Clase interna para localizar los datos que se necesitan para colocar la info
     */
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnCreateContextMenuListener {
        val imagen: ImageView = itemView.findViewById(R.id.fotoAlbum)
        val titulo: TextView = itemView.findViewById(R.id.tituloAlbum)

        init {
            itemView.setOnCreateContextMenuListener(this)
        }
        override fun onCreateContextMenu(menu: ContextMenu?, v: View?, menuInfo: ContextMenuInfo?) {
            menuInflater.inflate(R.menu.opciones_album, menu)
            menu?.setHeaderTitle("Opciones Menu")
        }


    }
}