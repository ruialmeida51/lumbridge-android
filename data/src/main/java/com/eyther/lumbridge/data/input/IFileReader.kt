package com.eyther.lumbridge.data.input

/**
 * Declares the contract for reading files. The idea here is to abstract the file reading
 * so that we can easily switch between platforms if necessary.
 */
interface IFileReader {

    /**
     * Given a file name, reads the file and returns its RAW content.
     *
     * @param fileName the name of the file to read
     */
    fun read(fileName: String): String
}
