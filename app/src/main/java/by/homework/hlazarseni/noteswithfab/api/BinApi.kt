package by.homework.hlazarseni.noteswithfab.api

import by.homework.hlazarseni.noteswithfab.model.NoteDTO
import retrofit2.http.GET
import retrofit2.http.Path


interface BinApi {

    @GET("/{numberCard}")
    fun getFakeData(@Path("numberCard") numberCard: String ): List<NoteDTO>

}