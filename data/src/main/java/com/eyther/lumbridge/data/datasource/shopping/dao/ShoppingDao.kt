package com.eyther.lumbridge.data.datasource.shopping.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.eyther.lumbridge.data.model.shopping.entity.SHOPPING_LIST_TABLE_NAME
import com.eyther.lumbridge.data.model.shopping.entity.ShoppingListEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ShoppingDao {
    @Transaction
    @Query("SELECT * FROM $SHOPPING_LIST_TABLE_NAME")
    fun getAllShoppingLists(): Flow<List<ShoppingListEntity>?>

    @Transaction
    @Query("SELECT * FROM $SHOPPING_LIST_TABLE_NAME WHERE shoppingListId = :shoppingListId")
    suspend fun getShoppingListById(shoppingListId: Long): ShoppingListEntity?

    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertShoppingList(shoppingList: ShoppingListEntity): Long

    @Transaction
    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateShoppingList(shoppingList: ShoppingListEntity)

    @Transaction
    @Query("DELETE FROM $SHOPPING_LIST_TABLE_NAME WHERE shoppingListId = :shoppingListId")
    suspend fun deleteShoppingListById(shoppingListId: Long)
}
