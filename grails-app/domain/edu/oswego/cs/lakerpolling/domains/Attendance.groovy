package edu.oswego.cs.lakerpolling.domains


class Attendance implements Serializable{
    Date date

    static belongsTo = [course: Course]
    static hasMany = [attendees: Attendee]

    static mapping = {
        version false
        id composite: ['date', 'course']
    }

    static constraints = {
        date blank: false
    }
}
