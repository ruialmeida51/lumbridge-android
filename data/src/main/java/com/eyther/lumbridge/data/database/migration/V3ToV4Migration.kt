package com.eyther.lumbridge.data.database.migration

import androidx.room.RenameColumn
import androidx.room.RenameTable
import androidx.room.migration.AutoMigrationSpec
import com.eyther.lumbridge.data.model.shopping.entity.SHOPPING_LIST_TABLE_NAME

@RenameColumn(tableName = "groceries_list", fromColumnName = "groceriesListId", toColumnName = "shoppingListId")
@RenameColumn(tableName = "groceries_list", fromColumnName = "groceriesListItems", toColumnName = "shoppingListItems")
@RenameTable(fromTableName = "groceries_list", toTableName = SHOPPING_LIST_TABLE_NAME)
class V3ToV4Migration : AutoMigrationSpec
