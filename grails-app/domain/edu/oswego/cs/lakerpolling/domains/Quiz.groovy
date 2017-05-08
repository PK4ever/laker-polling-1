package edu.oswego.cs.lakerpolling.domains

class Quiz {
    Date startDate
    Date endDate
    String name

    static belongsTo = [course: Course]
    static hasMany = [questions: Question, grades: Grade]

    static constraints = {
        name nullable: true
        questions nullable: true
    }
}
