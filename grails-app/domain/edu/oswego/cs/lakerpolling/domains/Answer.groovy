package edu.oswego.cs.lakerpolling.domains

class Answer {
    boolean correct
    List<Boolean> answers

    static belongsTo = [question: Question]
    static hasOne = [student: User]

    static constraints = {
        answers nullable: true
    }
}
