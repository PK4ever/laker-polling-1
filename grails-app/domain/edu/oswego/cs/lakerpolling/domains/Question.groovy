package edu.oswego.cs.lakerpolling.domains

class Question {
    String question
    boolean active
    List<Boolean> answers
    List<String> choices

    static belongsTo = [course: Course]
    static hasMany = [responses: Answer]
    static constraints = {
        question nullable: true
        choices nullable: true
        responses nullable: true
    }
}
