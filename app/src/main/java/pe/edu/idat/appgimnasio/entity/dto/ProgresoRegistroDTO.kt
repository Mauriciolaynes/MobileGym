package pe.edu.idat.appgimnasio.entity.dto

data class ProgresoRegistroDTO(
    val idUsuario: Int,
    val fechaRegistro: String, // Formato "yyyy-MM-dd"
    val peso: Double
)
