package com.stehno.mazes.repository

/**
 * Repository used to store maze image data.
 */
interface ImageRepository {

    void store(byte[] imageData)

    byte[] retrieve()
}
