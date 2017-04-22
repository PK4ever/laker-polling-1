package edu.oswego.cs.lakerpolling.domains

class Grade {

    double grade
    static hasOne = [student: User]
    static belongsTo = [quiz: Quiz]

    static constraints = {
    }
}
