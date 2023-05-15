package puzzles.common;

/**
 * An interface representing any class whose objects get notified when
 * the objects they are observing update themselves.
 *
 * @param <Subject> the type of object an implementor of this interface is observing
 * @param <ClientData> optional data the model can send to the observer (null if nothing)
 *
 * @author Justin Mishic
 */
public interface Observer<Subject, ClientData> {
    /**
     * The observed subject calls this method on each observer that has
     * previously registered with it. This version of the design pattern
     * follows the "push model" in that the subject can provide
     * ClientData to inform the observer about what exactly has happened.
     *
     * @param subject the object that wishes to inform this object
     * about something that has happened.
     * @param data optional data the server.model can send to the observer
     */
    void update(Subject subject, ClientData data);
}
