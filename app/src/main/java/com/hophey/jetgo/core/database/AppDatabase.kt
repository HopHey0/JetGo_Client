package com.hophey.jetgo.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.hophey.jetgo.feature.favourites.data.local.dao.FavouriteFlightDao
import com.hophey.jetgo.feature.favourites.data.local.entity.FavouriteFlightEntity

@Database(entities = [FavouriteFlightEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun favouriteFlightDao(): FavouriteFlightDao
}