package edu.oswego.cs.lakerpolling.domains

class Choice {
    int number
    String text

    static belongsTo = [question: Question]
}
