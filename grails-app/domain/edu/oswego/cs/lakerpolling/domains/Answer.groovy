package edu.oswego.cs.lakerpolling.domains

class Answer {
    boolean correct
    int answer

    static belongsTo = [question: Question]
    static hasOne = [student: User]

    static constraints = {
        answer nullable: true
    }
}
