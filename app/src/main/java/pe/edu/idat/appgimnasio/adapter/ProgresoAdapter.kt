package pe.edu.idat.appgimnasio.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import pe.edu.idat.appgimnasio.R
import pe.edu.idat.appgimnasio.entity.Progreso

class ProgresoAdapter(
    private var listaProgreso: List<Progreso>,
    private val onItemClick: (Progreso) -> Unit
) : RecyclerView.Adapter<ProgresoAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvFecha: TextView = itemView.findViewById(R.id.tvFechaProgreso)
        val tvPeso: TextView = itemView.findViewById(R.id.tvPesoProgreso)

        init {
            itemView.setOnClickListener {
                onItemClick(listaProgreso[adapterPosition])
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_progreso, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val progreso = listaProgreso[position]
        holder.tvFecha.text = progreso.fechaRegistro
        holder.tvPeso.text = "${progreso.peso} Kg"
    }

    override fun getItemCount(): Int = listaProgreso.size

    fun actualizarLista(nuevaLista: List<Progreso>) {
        listaProgreso = nuevaLista
        notifyDataSetChanged()
    }
}
