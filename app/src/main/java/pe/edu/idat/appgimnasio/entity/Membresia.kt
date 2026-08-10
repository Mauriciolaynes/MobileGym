package pe.edu.idat.appgimnasio.entity

import com.google.gson.annotations.SerializedName

data class Membresia (

    val idMembresia: Int,
    @SerializedName("tipoPlan")
    val tipoMembresia: String,
    val fechaInicio: String,
    val fechaFin: String,
    val estado: String,
    val precio: Double

)
