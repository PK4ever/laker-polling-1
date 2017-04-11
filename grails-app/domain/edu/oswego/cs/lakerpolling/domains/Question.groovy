package edu.oswego.cs.lakerpolling.domains

class Question {
    String question
    boolean active
    List<Boolean> answers
    List<Integer> studentAnswers

    static belongsTo = [course: Course]
    static hasMany = [responses: Answer]
    static constraints = {
        question nullable: true
        responses nullable: true
    }
}
