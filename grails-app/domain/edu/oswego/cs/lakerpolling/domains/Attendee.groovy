package edu.oswego.cs.lakerpolling.domains


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
