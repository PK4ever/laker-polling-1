package edu.oswego.cs.lakerpolling.controllers

import edu.oswego.cs.lakerpolling.services.PreconditionService
import edu.oswego.cs.lakerpolling.services.QuizService

class QuizController {

    PreconditionService preconditionService
    QuizService quizService

    /**
     * Endpoint to create a quiz for a given course
     * @param access_token - the access token of the user
     * @param course_id - the id of the course that this particular quiz with relate to
     * @param name -(optional) the name of the quiz
     * @param start_timestamp - The date this quiz will open in UNIX time
     * @param end_timestamp - The date this quiz will close in UNIX time
     * @return
     */
    def postQuiz(String access_token, String course_id, String name, String start_timestamp, String end_timestamp) {
        def require = preconditionService.notNull(params, ["access_token", "course_id", "start_timestamp", "end_timestamp"])
        def token = preconditionService.accessToken(access_token).data

        if(require.success) {
            def result = quizService.createQuiz(token, course_id, name, start_timestamp, end_timestamp)
            if(result.success) {
                render(view: 'getQuiz', model: [quiz: result.data, courseID: course_id.toLong(), token: token])
            } else {
                render(view: '../failure', model: [errorCode: result.errorCode, message: result.message])
            }
        } else {
            render(view: '../failure', model: [errorCode: require.errorCode, message: require.message])
        }

    }

    /**
     * Endpoint to get a list of all of the question IDs for a quiz.
     * @param access_token - The access token of the requesting user
     * @param quiz_id - the id of the quiz
     */
    def getQuizzes(String access_token, String course_id, String quiz_id) {
        def require = preconditionService.notNull(params, ["access_token"])
        def token = preconditionService.accessToken(access_token).data

        if (require.success) {
            if (quiz_id) {
                def result = quizService.getQuiz(token, quiz_id)
                if (result.success) {
                    def quiz = result.data
                    render(view: 'getQuiz', model: [token: token, courseID: quiz.course.id, quiz: quiz])
                } else {
                    render(view: '../failure', model: [errorCode: result.errorCode, message: result.message])
                }
            }
            else if (course_id){
                def result = quizService.getAllQuizzes(token, course_id)
                if (result.success) {
                    render(view: 'getQuizzes', model: [token: token, courseID: course_id.toLong(), quizzes: result.data])
                } else {
                    render(view: '../failure', model: [errorCode: result.errorCode, message: result.message])
                }
            }
            else {
                render(view: '../failure', model: [errorCode: 400, message: "Missing quiz_id or question_id parameter"])
            }
        } else {
            render(view: '../failure', model: [errorCode: require.errorCode, message: require.message])
        }
    }

    /**
     * Endpoint to delete a quiz.
     * @param access_token - The access token of the requesting user
     * @param quiz_id - the id of the quiz
     */
    def deleteQuiz(String access_token, String quiz_id) {
        def require = preconditionService.notNull(params, ["access_token", "quiz_id"])
        def token = preconditionService.accessToken(access_token).data

        if (require.success) {
            def result = quizService.deleteQuiz(token, quiz_id)
            if (result.success) {
                render(view: 'deleteResult', model: [token: token])
            } else {
                render(view: '../failure', model: [errorCode: result.errorCode, message: result.message])
            }
        } else {
            render(view: '../failure', model: [errorCode: require.errorCode, message: require.message])
        }
    }

    /**
     * Endpoint to create a quiz submission for the user of the given access token and the quiz with the given ID.
     * @param access_token - the access token of the requesting user
     * @param quiz_id - the id of the quiz
     */
    def submitQuiz(String access_token, String quiz_id) {
        def require = preconditionService.notNull(params, ["access_token", "quiz_id"])
        def token = preconditionService.accessToken(access_token).data

        if (require.success) {
            def result = quizService.submitQuiz(token, quiz_id)
            if (result.success) {
                render(view: 'quizSubmission', model: [token: token, submission: result.data])
            } else {
                render(view: '../failure', model: [errorCode: result.errorCode, message: result.message])
            }
        } else {
            render(view: '../failure', model: [errorCode: require.errorCode, message: require.message])
        }
    }

    /**
     * Endpoint to get the quiz submission for the user of the given access token and the quiz with the given ID.
     * @param access_token - the access token of the requesting user
     * @param quiz_id - the id of the quiz
     */
    def getQuizSubmission(String access_token, String quiz_id) {
        def require = preconditionService.notNull(params, ["access_token", "quiz_id"])
        def token = preconditionService.accessToken(access_token).data

        if (require.success) {
            def result = quizService.getQuizSubmission(token, quiz_id)
            if (result.success) {
                render(view: 'quizSubmission', model: [token: token, submission: result.data])
            } else {
                render(view: '../failure', model: [errorCode: result.errorCode, message: result.message])
            }
        } else {
            render(view: '../failure', model: [errorCode: require.errorCode, message: require.message])
        }
    }

    /**
     * Endpoint to get a list of all of the question IDs for a quiz.
     * @param access_token - The access token of the requesting user
     * @param quiz_id - the id of the quiz
     */
    def getQuizQuestions(String access_token, String quiz_id, String question_id) {
        def require = preconditionService.notNull(params, ["access_token", "quiz_id"])
        def token = preconditionService.accessToken(access_token).data

        if (require.success) {
            if (question_id) {
                def result = quizService.getQuestion(token, quiz_id, question_id)
                if (result.success) {
                    render(view: 'quizQuestion', model: [token: token, question: result.data])
                } else {
                    render(view: '../failure', model: [errorCode: result.errorCode, message: result.message])
                }
            }
            else {
                def result = quizService.getAllQuestionIds(token, quiz_id)
                if (result.success) {
                    render(view: 'questionIdList', model: [token: token, quizId: quiz_id.toLong(), questionIds: result.data])
                } else {
                    render(view: '../failure', model: [errorCode: result.errorCode, message: result.message])
                }
            }
        } else {
            render(view: '../failure', model: [errorCode: require.errorCode, message: require.message])
        }
    }

    /**
     * Endpoint to add a question to an existing quiz.
     * @param access_token - The access token of the requesting user
     * @param quiz_id - the id of the quiz
     * @param text - the text of the question
     * @param choices - a comma-separated String containing the text of the answer options
     * @param answers - a comma-separated String of Boolean values representing whether each option in the question is a correct choice or not.
     */
    def postQuestionToQuiz(String access_token, String quiz_id, String text, String choices, String answers) {
        def require = preconditionService.notNull(params, ["access_token", "quiz_id", "text", "choices", "answers"])
        def token = preconditionService.accessToken(access_token).data

        if (require.success) {
            def result = quizService.postQuestionToQuiz(token, quiz_id, text, choices, answers)
            if (result.success) {
                render(view: 'quizQuestion', model: [token: token, question: result.data])
            } else {
                render(view: '../failure', model: [errorCode: result.errorCode, message: result.message])
            }
        } else {
            render(view: '../failure', model: [errorCode: require.errorCode, message: require.message])
        }
    }

    /**
     * Removes the given question.
     * @param access_token - the access_token of the user
     * @param quiz_id - the id of the quiz
     * @param question_id - the id of the question to remove
     * @return - returns a json view
     */
    def deleteQuestion(String access_token, String quiz_id, String question_id) {
        def require = preconditionService.notNull(params, ["access_token", "question_id"])
        def token = preconditionService.accessToken(access_token).data

        if(require.success) {
            def result = quizService.deleteQuestion(token, quiz_id, question_id)
            if(result.success) {
                render(view: 'deleteResult', model: [token: token])
            } else render(view: '../failure', model: [errorCode: result.errorCode, message: result.message])
        } else render(view: '../failure', model: [errorCode: require.errorCode, message: require.message])
    }

    /**
     * Endpoint to answer a selected question
     * @param access_token - the access_token of the user
     * @param quiz_id - the id of the quiz
     * @param question_id - the id of the question
     * @param answer - the collection of answers represented as a list of booleans
     * @return - returns a json view
     */
    def answerQuestion(String access_token, String quiz_id, String question_id, String answer) {
        def require = preconditionService.notNull(params, ["access_token", "quiz_id", "question_id", "answer"])
        def token = preconditionService.accessToken(access_token).data

        if(require.success) {
            def result = quizService.answerQuestion(token, quiz_id, question_id, answer)
            if(result.success) {
                render(view: 'answerQuestion', model: [token: token])
            } else {
                render(view: '../failure', model: [errorCode: result.errorCode, message: result.message])
            }
        } else {
            render(view: '../failure', model: [errorCode: require.errorCode, message: require.message])
        }
    }

    def getGrades(String access_token, String quiz_id, String user_id) {
        def require = preconditionService.notNull(params, ["access_token", "quiz_id"])
        def token = preconditionService.accessToken(access_token).data
        def result
        if(require.success) {
            if(user_id != null) result = quizService.getUserGrades(token, quiz_id, user_id)
            else result = quizService.getQuizGrades(token, quiz_id)

            if(result.success) {
                render(view: 'getGrades', model: [token: token, grades: result.data, quiz: quiz_id])
            } else {
                render(view: '../failure', model: [errorCode: result.errorCode, message: result.message])
            }
        } else {
            render(view: '../failure', model: [errorCode: require.errorCode, message: require.message])
        }
    }
}
