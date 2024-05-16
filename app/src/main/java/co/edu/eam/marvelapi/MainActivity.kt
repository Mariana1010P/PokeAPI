package co.edu.eam.marvelapi

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import co.edu.eam.marvelapi.adapter.ListaPokemonAdapter
import co.edu.eam.marvelapi.model.PokemonRespuesta
import co.edu.eam.marvelapi.pokeApi.PokeApiService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "POKEDEX"
    }

    private lateinit var retrofit: Retrofit
    private lateinit var recyclerView: RecyclerView
    private lateinit var listaPokemonAdapter: ListaPokemonAdapter

    private var offset: Int = 0
    private var aptoParaCargar: Boolean = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        recyclerView = findViewById(R.id.recyclerView)
        listaPokemonAdapter = ListaPokemonAdapter(this)
        recyclerView.adapter = listaPokemonAdapter
        recyclerView.setHasFixedSize(true)
        val layoutManager = GridLayoutManager(this, 3)
        recyclerView.layoutManager = layoutManager
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                if (dy > 0) {
                    val visibleItemCount = layoutManager.childCount
                    val totalItemCount = layoutManager.itemCount
                    val pastVisibleItems = layoutManager.findFirstVisibleItemPosition()

                    if (aptoParaCargar) {
                        if ((visibleItemCount + pastVisibleItems) >= totalItemCount) {
                            Log.i(TAG, " Llegamos al final.")

                            aptoParaCargar = false
                            offset += 20
                            obtenerDatos(offset)
                        }
                    }
                }
            }
        })

        retrofit = Retrofit.Builder()
            .baseUrl("https://pokeapi.co/api/v2/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        aptoParaCargar = true
        offset = 0
        obtenerDatos(offset)
    }

    private fun obtenerDatos(offset: Int) {
        val service = retrofit.create(PokeApiService::class.java)
        val pokemonRespuestaCall = service.obtenerListaPokemon(20, this.offset)

        pokemonRespuestaCall.enqueue(object : Callback<PokemonRespuesta> {
            override fun onResponse(call: Call<PokemonRespuesta>, response: Response<PokemonRespuesta>) {
                aptoParaCargar = true
                if (response.isSuccessful) {
                    val pokemonRespuesta = response.body()
                    val listaPokemon = pokemonRespuesta?.results

                    if (listaPokemon != null) {
                        listaPokemonAdapter.adicionarListaPokemon(listaPokemon)
                    }
                } else {
                    Log.e(TAG, "onResponse: ${response.errorBody()}")
                }
            }

            override fun onFailure(call: Call<PokemonRespuesta>, t: Throwable) {
                aptoParaCargar = true
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
    }

}