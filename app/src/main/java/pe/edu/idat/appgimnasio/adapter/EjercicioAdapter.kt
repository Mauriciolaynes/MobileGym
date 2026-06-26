package pe.edu.idat.appgimnasio.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import pe.edu.idat.appgimnasio.R
import pe.edu.idat.appgimnasio.entity.EjercicioRutina

class EjercicioAdapter(private val context: Context, private var lista: List<EjercicioRutina>) :
    RecyclerView.Adapter<EjercicioAdapter.EjercicioViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EjercicioViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_ejercicio, parent, false)
        return EjercicioViewHolder(view)
    }

    override fun onBindViewHolder(holder: EjercicioViewHolder, position: Int) {
        val ejercicio = lista[position]

        holder.tvItemNombreEjercicio.text = ejercicio.nombreEjercicio
        holder.tvItemSeriesRepeticiones.text = "${ejercicio.series} series x ${ejercicio.repeticiones} repeticiones"
    }

    override fun getItemCount(): Int {
        return lista.size
    }

    inner class EjercicioViewHolder(itemview: View) : RecyclerView.ViewHolder(itemview) {
        val tvItemNombreEjercicio: TextView = itemview.findViewById<TextView>(R.id.tvItemNombreEjercicio)
        val tvItemSeriesRepeticiones: TextView = itemview.findViewById<TextView>(R.id.tvItemSeriesRepeticiones)
    }

    fun actualizarDatos(nuevosEjercicios: List<EjercicioRutina>) {
        this.lista = nuevosEjercicios
        notifyDataSetChanged()
    }
}