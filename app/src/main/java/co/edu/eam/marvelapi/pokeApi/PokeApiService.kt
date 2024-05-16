package co.edu.eam.marvelapi.pokeApi

import co.edu.eam.marvelapi.model.PokemonRespuesta
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface PokeApiService {
    @GET("pokemon")
    fun obtenerListaPokemon(@Query("limit") limit: Int, @Query("offset") offset: Int): Call<PokemonRespuesta>
}