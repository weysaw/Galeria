package com.axel.ornelas.galeria.adaptadores


import android.content.Context
import android.net.Uri
import android.view.*
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.axel.ornelas.galeria.Album
import com.axel.ornelas.galeria.R
import android.view.ContextMenu.ContextMenuInfo
import com.bumptech.glide.Glide

/**
 * https://newbedev.com/how-to-create-context-menu-for-recyclerview
 * https://bumptech.github.io/glide/doc/options.html
 */
class AlbumesAdaptador(var localDataSet: ArrayList<Album>, private val menuInflater: MenuInflater) :
    RecyclerView.Adapter<AlbumesAdaptador.ViewHolder>() {

    private lateinit var contexto: Context
    //Tal vez se deba poner protected
    lateinit var onClickListener: View.OnClickListener

    var pos = 0
        private set

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        contexto = parent.context
        //Indica el estilo que debe tener el recycler
        val view = LayoutInflater.from(contexto).inflate(R.layout.album, parent, false)
        view.setOnClickListener(onClickListener)
        //view.setOnLongClickListener(onLongClickListener)
        //Devuelve la vista creada
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        //Coloca la información de las peliculas a los campos de texto
        with(localDataSet[position].fotos) {
            if (this.isEmpty()) {
                Glide.with(contexto)
                    .load(R.drawable.folder)
                    .centerCrop()
                    .into(holder.imagen)
            } else {
                val direccion: Uri = Uri.parse(localDataSet[position].fotos[0].direccionUri)
                Glide.with(contexto)
                    .load(direccion)
                    .centerCrop()
                    .into(holder.imagen)
            }
        }
        holder.titulo.text = localDataSet[position].titulo
        holder.itemView.setOnLongClickListener {
            pos = holder.adapterPosition
            false
        }
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
            menu?.setHeaderTitle("Opciones Album")
        }


    }
}