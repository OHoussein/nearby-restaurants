package dev.ohoussein.restos.domain.model

class Icon(
        val prefix: String,
        val suffix: String,
) {

    fun getUrl(size: Int): String = prefix + size + suffix
}
