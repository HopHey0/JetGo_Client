package com.hophey.jetgo.feature.favourites.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.hophey.jetgo.feature.favourites.data.local.entity.FavouriteFlightEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FavouriteFlightDao {
    @Query("SELECT * FROM favourite_flights")
    fun getAll(): Flow<List<FavouriteFlightEntity>>

    @Query("SELECT flightId FROM favourite_flights")
    fun getAllIds(): Flow<List<Long>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: FavouriteFlightEntity)

    @Query("DELETE FROM favourite_flights WHERE flightId = :flightId")
    suspend fun delete(flightId: Long)
}