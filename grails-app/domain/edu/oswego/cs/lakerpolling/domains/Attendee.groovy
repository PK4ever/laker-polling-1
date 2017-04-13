package edu.oswego.cs.lakerpolling.domains

/**
 * Created by Josh active 3/27/17.
 */
class Attendee {

    boolean attended

    static belongsTo = [attendance: Attendance]
    static hasOne = [student:User]

    static mapping = {
        version false
    }
    static constraints = {
        attended blank: false

    }
}
