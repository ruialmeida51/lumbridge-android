package com.eyther.lumbridge.data.datasource.netsalary.local

import com.eyther.lumbridge.data.di.UtilModule.AndroidFileReader
import com.eyther.lumbridge.data.di.UtilModule.DefaultGson
import com.eyther.lumbridge.data.input.IFileReader
import com.eyther.lumbridge.data.mappers.netsalary.toCached
import com.eyther.lumbridge.data.model.netsalary.files.portugal.PortugalIrsTableFromFile
import com.eyther.lumbridge.data.model.netsalary.local.portugal.PortugalIrsBracketTypeCached
import com.eyther.lumbridge.data.model.netsalary.local.portugal.PortugalIrsBracketTypeCached.Handicapped
import com.eyther.lumbridge.data.model.netsalary.local.portugal.PortugalIrsBracketTypeCached.NotHandicapped
import com.eyther.lumbridge.data.model.netsalary.local.portugal.PortugalIrsTableCached
import com.eyther.lumbridge.shared.di.model.Schedulers
import com.google.gson.Gson
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * Local data source for the IRS brackets for Portugal.
 *
 * @param gson the Gson instance to parse the JSON
 * @param fileReader the file reader to read the JSON file. This is a best attempt to abstract it a little bit from the
 * platform specific implementation. With KMP in mind, we should be able to override this implementation for other platforms.
 * @param schedulers the schedulers to run the operations on
 */
class PortugalIrsBracketsLocalDataSource @Inject constructor(
    @DefaultGson private val gson: Gson,
    @AndroidFileReader private val fileReader: IFileReader,
    private val schedulers: Schedulers,
) {

    companion object {
        private const val PREFIX = "portugal_irs_brackets"
        private const val NO_HANDICAP_FIRST_TABLE = "$PREFIX/nohandicap/first_table.json"
        private const val NO_HANDICAP_SECOND_TABLE = "$PREFIX/nohandicap/second_table.json"
        private const val NO_HANDICAP_THIRD_TABLE = "$PREFIX/nohandicap/third_table.json"

        private const val HANDICAP_FOURTH_TABLE = "$PREFIX/handicapped/fourth_table.json"
        private const val HANDICAP_FIFTH_TABLE = "$PREFIX/handicapped/fifth_table.json"
        private const val HANDICAP_SIXTH_TABLE = "$PREFIX/handicapped/sixth_table.json"
        private const val HANDICAP_SEVENTH_TABLE = "$PREFIX/handicapped/seventh_table.json"
    }

    /**
     * Gets the IRS brackets for Portugal. The brackets are read from the file system.
     */
    suspend fun getIrsBracketInfo(
        bracketIdentifierCached: PortugalIrsBracketTypeCached
    ): PortugalIrsTableCached = withContext(schedulers.io) {
        return@withContext when (bracketIdentifierCached) {
            is NotHandicapped.NotMarriedWithoutDependentsOrMarried -> readPortugueseIrsTable(NO_HANDICAP_FIRST_TABLE)
            is NotHandicapped.NotMarriedWithOneOrMoreDependents -> readPortugueseIrsTable(NO_HANDICAP_SECOND_TABLE)
            is NotHandicapped.MarriedSingleHolder -> readPortugueseIrsTable(NO_HANDICAP_THIRD_TABLE)
            is Handicapped.NotMarriedOrMarriedWithoutDependents -> readPortugueseIrsTable(HANDICAP_FOURTH_TABLE)
            is Handicapped.NotMarriedWithOneOrMoreDependents -> readPortugueseIrsTable(HANDICAP_FIFTH_TABLE)
            is Handicapped.MarriedJointHolderWithOneOrMoreDependents -> readPortugueseIrsTable(HANDICAP_SIXTH_TABLE)
            is Handicapped.MarriedSingleHolder -> readPortugueseIrsTable(HANDICAP_SEVENTH_TABLE)
        }.toCached()
    }

    private fun readPortugueseIrsTable(fileName: String): PortugalIrsTableFromFile {
        return gson.fromJson(
            fileReader.read(fileName),
            PortugalIrsTableFromFile::class.java
        )
    }
}
