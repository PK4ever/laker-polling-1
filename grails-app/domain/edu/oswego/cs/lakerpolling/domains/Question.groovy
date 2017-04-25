package edu.oswego.cs.lakerpolling.domains

import edu.oswego.cs.lakerpolling.util.QuestionType

class Question {
    String question
    boolean active
    List<Boolean> answers
    List<String> choices
    QuestionType type
    Date dateCreated

    static belongsTo = [course: Course]
    static hasMany = [responses: Answer]
    static constraints = {
        question nullable: true
        choices nullable: true
        responses nullable: true
    }
}
