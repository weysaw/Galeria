package com.axel.ornelas.galeria.adaptadores


import android.content.Context
import android.net.Uri
import android.view.*
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.axel.ornelas.galeria.Album
import com.axel.ornelas.galeria.R
import android.view.ContextMenu.ContextMenuInfo
import com.bumptech.glide.Glide

/**
 * https://newbedev.com/how-to-create-context-menu-for-recyclerview
 * https://bumptech.github.io/glide/doc/options.html
 */
class FotosAdaptador(private val localDataSet: Album, private val menuInflater: MenuInflater?) :
    RecyclerView.Adapter<FotosAdaptador.ViewHolder>() {

    private lateinit var contexto: Context
    lateinit var onClickListener: View.OnClickListener
    var pos: Int = 0
        private set
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        //Indica el estilo que debe tener el recycler
        contexto = parent.context
        val view = LayoutInflater.from(contexto).inflate(R.layout.foto, parent, false)
        view.setOnClickListener(onClickListener)
        //Devuelve la vista creada
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val foto = localDataSet.fotos[position]
        val direccion = Uri.parse(foto.direccionUri)
        Glide.with(contexto)
            .load(direccion)
            .fitCenter()
            .into(holder.imagen)
        holder.itemView.setOnLongClickListener {
            pos = holder.adapterPosition
            false
        }
    }

    /**
     * Accion por defecto cuando se presiona mucho tiempo
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