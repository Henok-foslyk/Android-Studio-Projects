package com.example.httpdemo.network

import retrofit2.http.GET
import retrofit2.http.Query
import com.example.httpdemo.data.nasa.RoverPhotos

// HOST: https://api.nasa.gov/
// PATH: mars-photos/api/v1/rovers/curiosity/photos
// QUERY params: ?earth_date=2024-1-4&api_key=DEMO_KEY

interface NasaAPI {
    @GET("mars-photos/api/v1/rovers/curiosity/photos")
    suspend fun getRoverPhotos(@Query("earth_date") earthDate: String,
                               @Query("api_key") apikey: String): RoverPhotos
}