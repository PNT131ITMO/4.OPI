package weblab.webproject3;

import jakarta.persistence.*;
import java.io.Serializable;

/**
 * Represents an entry in the system, storing coordinates (x, y), radius (r), 
 * and the result of a hit check within specified geometric areas.
 * This class is a JPA entity, mapped to a database table.
 */
@Entity
public class Entry implements Serializable {

    /** The unique identifier for this entry, auto-generated using a sequence. */
    @Id
    @SequenceGenerator(name = "jpaSequence", sequenceName = "JPA_SEQUENCE", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "jpaSequence")
    private int id;

    /** The x-coordinate value of the entry. */
    private Double xVal;

    /** The y-coordinate value of the entry. */
    private Double yVal;

    /** The radius value used for hit checking. */
    private Integer rVal;

    /** The result of the hit check ("hit" or "miss"). */
    private String hitResult;

    /**
     * Default constructor for JPA and general use.
     */
    public Entry() {
    }

    /**
     * Checks if the entry lies within the triangle area defined by x <= 0, y <= 0, 
     * and y >= -x - r.
     *
     * @return true if the entry is in the triangle, false otherwise
     */
    private boolean checkTriangle() {
        return xVal <= 0 && yVal <= 0 && yVal >= (-xVal - (double) rVal);
    }

    /**
     * Checks if the entry lies within the rectangle area defined by 0 <= x <= r/2, 
     * -r <= y <= 0.
     *
     * @return true if the entry is in the rectangle, false otherwise
     */
    private boolean checkRectangle() {
        return xVal >= 0 && xVal <= rVal  && yVal >= (double) -rVal && yVal <= 0;
    }

    /**
     * Checks if the entry lies within the circle area defined by x <= 0, y >= 0, 
     * and x^2 + y^2 <= (r^2)/4.
     *
     * @return true if the entry is in the circle, false otherwise
     */
    private boolean checkCircle() {
        return xVal <= 0 && yVal >= 0 && xVal * xVal + yVal * yVal <= (double) rVal * rVal / 4;
    }

    /**
     * Determines if the entry is a hit by checking if it lies within the triangle, 
     * rectangle, or circle areas. Sets the hitResult field accordingly.
     */
    public void checkHit() {
        hitResult = checkCircle() || checkRectangle() || checkTriangle() ? "hit" : "miss";
    }

    /**
     * Gets the ID of this entry.
     *
     * @return the entry's ID
     */
    public int getId() {
        return id;
    }

    /**
     * Sets the ID of this entry.
     *
     * @param id the ID to set
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Gets the x-coordinate value.
     *
     * @return the x-coordinate value
     */
    public Double getxVal() {
        return xVal;
    }

    /**
     * Sets the x-coordinate value.
     *
     * @param xVal the x-coordinate value to set
     */
    public void setxVal(Double xVal) {
        this.xVal = xVal;
    }

    /**
     * Gets the y-coordinate value.
     *
     * @return the y-coordinate value
     */
    public Double getyVal() {
        return yVal;
    }

    /**
     * Sets the y-coordinate value.
     *
     * @param yVal the y-coordinate value to set
     */
    public void setyVal(Double yVal) {
        this.yVal = yVal;
    }

    /**
     * Gets the radius value.
     *
     * @return the radius value
     */
    public Integer getrVal() {
        return rVal;
    }

    /**
     * Sets the radius value.
     *
     * @param rVal the radius value to set
     */
    public void setrVal(Integer rVal) {
        this.rVal = rVal;
    }

    /**
     * Gets the hit result.
     *
     * @return the hit result ("hit" or "miss")
     */
    public String getHitResult() {
        return hitResult;
    }

    /**
     * Sets the hit result.
     *
     * @param hitResult the hit result to set
     */
    public void setHitResult(String hitResult) {
        this.hitResult = hitResult;
    }

    /**
     * Returns a string representation of this entry.
     *
     * @return a string containing x, y, r values and hit result
     */
    @Override
    public String toString() {
        return "Entry{" +
                "xValue=" + xVal +
                ", yValue=" + yVal +
                ", rValue=" + rVal +
                ", hitResult=" + hitResult +
                '}';
    }

    /**
     * Computes the hash code for this entry based on x, y, and r values.
     *
     * @return the hash code value
     */
    @Override
    public int hashCode() {
        return xVal.hashCode() + yVal.hashCode() + rVal.hashCode();
    }

    /**
     * Compares this entry with another object for equality based on x, y, and r values.
     *
     * @param obj the object to compare with
     * @return true if the objects are equal, false otherwise
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj instanceof Entry) {
            Entry entryObj = (Entry) obj;
            return xVal.equals(entryObj.getxVal()) &&
                    yVal.equals(entryObj.getyVal()) &&
                    rVal.equals(entryObj.getrVal());
        }
        return false;
    }
}