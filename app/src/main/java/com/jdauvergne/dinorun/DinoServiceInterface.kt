/**
 * Defines the basic controls for a service related to the dinosaur in the game.
 * This interface allows starting, stopping, pausing, and resuming the service.
 */
interface DinoServiceInterface {

    /**
     * Starts the service.
     */
    fun start()

    /**
     * Stops the service.
     */
    fun stop()

    /**
     * Pauses the service.
     */
    fun pause()

    /**
     * Resumes the service after being paused.
     */
    fun resume()
}
