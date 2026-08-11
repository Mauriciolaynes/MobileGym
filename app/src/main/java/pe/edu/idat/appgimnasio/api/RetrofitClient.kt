package pe.edu.idat.appgimnasio.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {


//    private const val BASE_URL = "http://192.168.18.6:8080/api/v1/"

// Si usas EMULADOR, cambia 192.168.18.6 por 10.0.2.2
private const val BASE_URL = "http://10.0.2.2:8080/api/v1/"
// Si usas un CELULAR FÍSICO, asegúrate de que el celular y la PC
// estén en la misma red Wi-Fi y usa la IP actual de tu PC (ej. 192.168.1.15)

    val instance: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}