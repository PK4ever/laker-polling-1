package edu.oswego.cs.lakerpolling.domains


class Attendance {
    Date date

    static belongsTo = [course: Course]
    static hasMany = [attendees: Attendee]

    static mapping = {
        version false
    }

    static constraints = {
        date blank: false
    }
}
