package com.stehno.mazes.repository

import groovy.util.logging.Slf4j
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Repository

import javax.servlet.http.HttpSession

/**
 * Session-based storage for maze image data.
 *
 * Generally, storing image data in session is a bad idea, but since this is a single small image
 * in a simple fun app, it's better than having to write to the filesystem or setup a database.
 */
@Repository @Slf4j
class SessionImageRepository implements ImageRepository {

    private static final String IMAGE_DATA = 'IMAGE_DATA'

    @Autowired private HttpSession session

    void store(byte[] imageData) {
        log.info 'Storing {} bytes of image data', imageData.size()

        session.setAttribute(IMAGE_DATA, imageData)
    }

    byte[] retrieve() {
        session.getAttribute(IMAGE_DATA) as byte[]
    }
}
