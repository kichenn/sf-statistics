package utils;


import log.LoggerFactory;
import net.jodah.expiringmap.ExpirationPolicy;
import net.jodah.expiringmap.ExpiringMap;


import java.util.UUID;
import java.util.concurrent.TimeUnit;


/**
 * SessionManager represent a singlenton session management.
 * use getManager to acquire the singlenton.
 */
public class SessionsManager {

    /**
     * Sessions map should be <userID, sessionID>
     * using ExpiringMap as the internal implementaion.
     **/

    private ExpiringMap<String, String> sessions = ExpiringMap.builder().expiration(30, TimeUnit.MINUTES).expirationPolicy(ExpirationPolicy.ACCESSED).build();



    private static SessionsManager manager = new SessionsManager();

    private SessionsManager() {}

    public static SessionsManager getManager() {
        return manager;
    }

    /**
     * Gets sessionID from global session map.
     * 
     * @param userID user unique identifier string
     * @return the value to global session map, or {@code null} if userID is expire or empty
     */
    public String get(String userID) {
        return this.sessions.get(userID);
    }

    /**
     * Save the userID & sessionID into sessions map.
     * 
     * @param userID    user unique identifier string
     * @param sessionID session unique identifier string, valid value should be in the pattern of
     *                  <code>[^a-zA-Z0-9 _-]</code>, except for null or empty string
     * @return the sessionID that have been saved into session map, be sure to check if it is the same
     *         as the param sessionID
     * @throws NullPointerException if userID is given as null.
     */
    public String put(String userID, String sessionID) {
        if (!HandlerUtilities.isValidParameter(sessionID)) {
            // if input seesionID is invalid session id, we discard it.
            sessionID = null;

            if (sessions.containsKey(userID)) {

                // if we already have session associate with this userID, use that one
                sessionID = this.get(userID);
            } else {
                // reset it to random token
                sessionID = this.generateToken();
            }
        }
        LoggerFactory.getLogger().debug("saved session id: " + sessionID);
        this.sessions.put(userID, sessionID);
        return sessionID;
    }

    /**
     * Generates a UUID format session ID.
     * 
     * @return UUID Format string
     */
    public String generateToken() {
        return UUID.randomUUID().toString();
    }
}
