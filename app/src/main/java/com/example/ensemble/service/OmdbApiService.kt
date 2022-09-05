package com.example.ensemble.service

import com.google.gson.annotations.SerializedName
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface OmdbApiService {
    @Headers("Content-Type: application/json")
    @GET("/?apikey=a2849051&page=1&type=movie&plot=full")
    fun getOmdbMoviesByTitle(
        @Query("s") title: String
    ): Observable<OmdbRootData>
}

data class OmdbRootData(
    @SerializedName("Search")
    val result: List<OmdbData>
)

data class OmdbData(
    @SerializedName("Title")
    val title: String,
    @SerializedName("Year")
    val year: String,
    @SerializedName("Poster")
    val poster: String
)