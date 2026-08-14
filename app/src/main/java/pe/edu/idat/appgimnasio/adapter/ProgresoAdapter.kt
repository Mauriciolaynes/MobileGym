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
        val tvDiferencia: TextView = itemView.findViewById(R.id.tvDiferenciaPeso)

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

        if (position < listaProgreso.size - 1) {
            val progresoAnterior = listaProgreso[position + 1]

            val diferencia = Math.abs(progreso.peso - progresoAnterior.peso)
            val difFormateada = String.format(java.util.Locale.getDefault(), "%.1f", diferencia)

            holder.tvDiferencia.visibility = View.VISIBLE

            if (progreso.peso > progresoAnterior.peso) {
                holder.ivTendencia.visibility = View.VISIBLE
                holder.ivTendencia.setImageResource(R.drawable.ic_avanzar)
                holder.ivTendencia.rotation = 270f

                val colorRojo = ContextCompat.getColor(holder.itemView.context, R.color.red)
                holder.ivTendencia.setColorFilter(colorRojo)

                holder.tvDiferencia.text = "Subiste $difFormateada Kg"
                holder.tvDiferencia.setTextColor(colorRojo)

            } else if (progreso.peso < progresoAnterior.peso) {
                holder.ivTendencia.visibility = View.VISIBLE
                holder.ivTendencia.setImageResource(R.drawable.ic_avanzar)
                holder.ivTendencia.rotation = 90f

                val colorVerde = ContextCompat.getColor(holder.itemView.context, R.color.green)
                holder.ivTendencia.setColorFilter(colorVerde)

                holder.tvDiferencia.text = "Bajaste $difFormateada Kg"
                holder.tvDiferencia.setTextColor(colorVerde)

            } else {
                holder.ivTendencia.visibility = View.GONE
                holder.tvDiferencia.text = "Te mantuviste"
                holder.tvDiferencia.setTextColor(ContextCompat.getColor(holder.itemView.context, android.R.color.darker_gray))
            }
        } else {
            holder.ivTendencia.visibility = View.GONE
            holder.tvDiferencia.visibility = View.VISIBLE
            holder.tvDiferencia.text = "Peso inicial"
            holder.tvDiferencia.setTextColor(ContextCompat.getColor(holder.itemView.context, android.R.color.darker_gray))
        }
    }

    override fun getItemCount(): Int = listaProgreso.size

    fun actualizarLista(nuevaLista: List<Progreso>) {
        listaProgreso = nuevaLista
        notifyDataSetChanged()
    }
}