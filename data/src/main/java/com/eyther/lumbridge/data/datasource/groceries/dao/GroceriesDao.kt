package com.eyther.lumbridge.data.datasource.groceries.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.eyther.lumbridge.data.model.groceries.entity.GROCERIES_LIST_TABLE_NAME
import com.eyther.lumbridge.data.model.groceries.entity.GroceriesListEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface GroceriesDao {
    @Transaction
    @Query("SELECT * FROM $GROCERIES_LIST_TABLE_NAME")
    fun getAllGroceriesList(): Flow<List<GroceriesListEntity>?>

    @Transaction
    @Query("SELECT * FROM $GROCERIES_LIST_TABLE_NAME WHERE groceriesListId = :groceriesListId")
    suspend fun getGroceriesListById(groceriesListId: Long): GroceriesListEntity?

    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGroceriesList(groceriesList: GroceriesListEntity): Long

    @Transaction
    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateGroceriesList(groceriesList: GroceriesListEntity)

    @Transaction
    @Query("DELETE FROM $GROCERIES_LIST_TABLE_NAME WHERE groceriesListId = :groceriesListId")
    suspend fun deleteGroceriesListById(groceriesListId: Long)
}
