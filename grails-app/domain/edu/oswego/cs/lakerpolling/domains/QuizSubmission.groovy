package edu.oswego.cs.lakerpolling.domains

class QuizSubmission {
    Date timestamp

    static belongsTo = [student: User, quiz: Quiz]
}
