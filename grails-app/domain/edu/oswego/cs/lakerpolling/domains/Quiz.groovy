package edu.oswego.cs.lakerpolling.domains

class Quiz {
    Date startDate
    Date endDate
    String name

    static belongsTo = [course: Course]
    static hasMany = [questions: Question]

    static constraints = {
        name nullable: true
        questions nullable: true
    }
}
