package pe.edu.idat.appgimnasio.entity

data class Usuario(
    val idUsuario : Int,
    val dni: String,
    val nombres: String,
    val apellidos: String,
    val email: String,
    val telefono: String,
    val password: String
)