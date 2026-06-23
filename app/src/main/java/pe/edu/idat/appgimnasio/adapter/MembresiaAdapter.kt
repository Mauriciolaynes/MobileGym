package pe.edu.idat.appgimnasio.adapter


import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import pe.edu.idat.appgimnasio.entity.Membresia
import pe.edu.idat.appgimnasio.R

class MembresiaAdapter (private val context : Context, private var lista: List<Membresia>)
    : RecyclerView.Adapter<MembresiaAdapter.MembresiaViewHolder>(){


    override fun onCreateViewHolder(parent: ViewGroup,viewType: Int): MembresiaViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_membresia,parent,false)
        return MembresiaViewHolder(view)
    }

    override fun onBindViewHolder(holder: MembresiaAdapter.MembresiaViewHolder,position: Int) {
        val membresia = lista[position]

        holder.tvItemTipoPlan.text = membresia.tipoMembresia
        holder.tvItemEstado.text = membresia.estado.uppercase()
        holder.tvItemFechas.text = "${membresia.fechaInicio} - ${membresia.fechaFin}"
        holder.tvItemPrecio.text = "S/ ${String.format("%.2f", membresia.precio)}"


        if (membresia.estado.lowercase() != "activo") {
            holder.tvItemEstado.setTextColor(context.getColor(android.R.color.holo_red_dark))
        } else {
            holder.tvItemEstado.setTextColor(context.getColor(android.R.color.holo_green_dark))
        }
    }

    override fun getItemCount(): Int {
        return lista.size
    }


    inner class MembresiaViewHolder(itemview: View) : RecyclerView.ViewHolder(itemview) {
        val tvItemTipoPlan: TextView = itemview.findViewById<TextView>(R.id.tvItemTipoPlan)
        val tvItemEstado: TextView = itemview.findViewById<TextView>(R.id.tvItemEstado)
        val tvItemFechas: TextView = itemview.findViewById<TextView>(R.id.tvItemFechas)
        val tvItemPrecio: TextView = itemview.findViewById<TextView>(R.id.tvItemPrecio)
    }
    fun actualizarDatos(nuevasMembresias: List<Membresia>) {
        this.lista = nuevasMembresias
        notifyDataSetChanged()
    }
}
