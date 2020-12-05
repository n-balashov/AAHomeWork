package com.aacademy.homework.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import com.aacademy.homework.data.local.model.MovieDetailWithActors
import io.reactivex.rxjava3.core.Single

@Dao
interface MovieDetailDao {

    @Query("SELECT * FROM moviedetail WHERE id = :id")
    fun getMovieDetail(id: Int): Single<MovieDetailWithActors>
}