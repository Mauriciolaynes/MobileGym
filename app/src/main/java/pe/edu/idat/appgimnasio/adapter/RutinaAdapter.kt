package pe.edu.idat.appgimnasio.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import pe.edu.idat.appgimnasio.R
import pe.edu.idat.appgimnasio.entity.Rutina

class RutinaAdapter(
    private val context: Context,
    private var lista: List<Rutina>,
    private val onItemClick: (Rutina) -> Unit
) : RecyclerView.Adapter<RutinaAdapter.RutinaViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RutinaViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_mis_rutinas, parent, false)
        return RutinaViewHolder(view)
    }

    override fun onBindViewHolder(holder: RutinaViewHolder, position: Int) {
        val rutina = lista[position]

        holder.tvNombreRutina.text = rutina.idNombreRutina


        holder.itemView.setOnClickListener {
            onItemClick(rutina)
        }

        Glide.with(context)
            .load(rutina.urlGif)
            .into(holder.ivItemImagenRutina)
    }

    override fun getItemCount(): Int {
        return lista.size
    }

    inner class RutinaViewHolder(itemview: View) : RecyclerView.ViewHolder(itemview) {
        val tvNombreRutina: TextView = itemview.findViewById(R.id.tvNombreRutina)

        val ivItemImagenRutina: ImageView = itemview.findViewById(R.id.ivItemImagenRutina)

    }

    fun actualizarDatos(nuevasRutinas: List<Rutina>) {
        this.lista = nuevasRutinas
        notifyDataSetChanged()
    }
}