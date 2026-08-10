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

        holder.tvNombreRutina.text = rutina.nombreRutina

        holder.itemView.setOnClickListener {
            onItemClick(rutina)
        }

        if (!rutina.imagenUrl.isNullOrEmpty()) {
            val urlCompleta = "http://192.168.18.6:8080/rutinas/${rutina.imagenUrl}"
            Glide.with(context)
                .load(urlCompleta)
                .into(holder.ivItemImagenRutina)
        } else {
            holder.ivItemImagenRutina.setImageResource(R.drawable.ic_launcher_background)
        }
    }

    override fun getItemCount(): Int {
        return lista.size
    }

    inner class RutinaViewHolder(itemview: View) : RecyclerView.ViewHolder(itemview) {
        val tvNombreRutina: TextView = itemview.findViewById(R.id.tvItemNombreRutina)
        val ivItemImagenRutina: ImageView = itemview.findViewById(R.id.ivItemImagenRutina)
    }

    fun actualizarDatos(nuevasRutinas: List<Rutina>) {
        this.lista = nuevasRutinas
        notifyDataSetChanged()
    }
}