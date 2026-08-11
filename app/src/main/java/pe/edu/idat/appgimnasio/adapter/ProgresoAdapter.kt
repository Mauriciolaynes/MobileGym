package pe.edu.idat.appgimnasio.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
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
        val ivTendencia: ImageView = itemView.findViewById(R.id.ivTendenciaPeso)

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

        // Lógica de tendencia (comparando con el registro anterior/más antiguo en la lista)
        // Asumiendo que la lista está ordenada de más reciente a más antiguo
        if (position < listaProgreso.size - 1) {
            val progresoAnterior = listaProgreso[position + 1]
            holder.ivTendencia.visibility = View.VISIBLE
            
            if (progreso.peso > progresoAnterior.peso) {
                // Subió de peso: Flecha Roja Arriba (Alerta)
                holder.ivTendencia.setImageResource(R.drawable.ic_avanzar)
                holder.ivTendencia.rotation = 270f
                holder.ivTendencia.setColorFilter(ContextCompat.getColor(holder.itemView.context, R.color.red))
            } else if (progreso.peso < progresoAnterior.peso) {
                // Bajó de peso: Flecha Verde Abajo (Mejora)
                holder.ivTendencia.setImageResource(R.drawable.ic_avanzar)
                holder.ivTendencia.rotation = 90f
                holder.ivTendencia.setColorFilter(ContextCompat.getColor(holder.itemView.context, R.color.green))
            } else {
                // Mismo peso: No mostrar flecha
                holder.ivTendencia.visibility = View.GONE
            }
        } else {
            // Es el registro más antiguo, no hay con qué comparar
            holder.ivTendencia.visibility = View.GONE
        }
    }

    override fun getItemCount(): Int = listaProgreso.size

    fun actualizarLista(nuevaLista: List<Progreso>) {
        listaProgreso = nuevaLista
        notifyDataSetChanged()
    }
}
