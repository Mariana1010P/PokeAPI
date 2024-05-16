package co.edu.eam.marvelapi.model

data class Pokemon(
    var number: Int = 0,
    var name: String = "",
    var url: String = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/"
) {
    fun getPokemonNumber(): Int {
        val urlParts = url.split("/")
        return urlParts[urlParts.size - 2].toInt()
    }
}
