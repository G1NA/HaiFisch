package commons;

import com.haifisch.server.utils.Point;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * Request of a checkin
 */

public class CheckInRequest implements Serializable {

    private static final long serialVersionUID = 2547822657122L;

    private Point leftCorner;
    private Point rightCorner;
    private Timestamp fromTime;
    private Timestamp toTime;
    private int topK;
    private String requestId;
    private int mapperCount;

    public CheckInRequest() {
    }

    /**
     * New CheckInRequest object
     *
     * @param id    The id assigned to the check in
     * @param left  The bottom left point
     * @param right The top right point
     * @param from  From when in time
     * @param to    To when in time
     */
    public CheckInRequest(String id, int mapperCount, Point left, Point right, Timestamp from, Timestamp to) {
        this.mapperCount = mapperCount;
        leftCorner = left;
        rightCorner = right;
        fromTime = from;
        toTime = to;
        requestId = id;
    }

    /**
     * Simple constructor
     *
     * @param id The id assigned to the check in
     */
    public CheckInRequest(String id) {
        this.requestId = id;
    }

    /**
     * Return the bottom left point
     *
     * @return Point type, having the coordinates of the bottom left point
     */
    public Point getLeftCorner() {
        return leftCorner;
    }

    /**
     * Set the bottom left point
     *
     * @param left_corner The Point variable to be set on left_corner
     */
    public void setLeftCorner(Point left_corner) {
        this.leftCorner = left_corner;
    }

    /**
     * Return the unique id of the request
     *
     * @return The id string
     */
    public String getRequestId() {
        return requestId;
    }

    /**
     * Get the top right corner
     *
     * @return The Point variable
     */
    public Point getRightCorner() {
        return rightCorner;
    }

    public void setRightCorner(Point right_corner) {
        this.rightCorner = right_corner;
    }

    public Timestamp getFromTime() {
        return fromTime;
    }

    public void setFromTime(Timestamp from_time) {
        this.fromTime = from_time;
    }

    public Timestamp getToTime() {
        return toTime;
    }

    public void setToTime(Timestamp to_time) {
        this.toTime = to_time;
    }

    public int getTopK() {
        return topK;
    }

    public void setTopK(int topK) {
        this.topK = topK;
    }

    public int getMapperCount() {
        return mapperCount;
    }
}
