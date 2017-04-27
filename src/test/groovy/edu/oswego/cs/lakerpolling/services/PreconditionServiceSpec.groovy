package edu.oswego.cs.lakerpolling.services

import com.sun.javaws.exceptions.InvalidArgumentException
import edu.oswego.cs.lakerpolling.domains.AuthToken
import edu.oswego.cs.lakerpolling.domains.Course
import edu.oswego.cs.lakerpolling.domains.Role
import edu.oswego.cs.lakerpolling.domains.User
import edu.oswego.cs.lakerpolling.util.QueryResult
import edu.oswego.cs.lakerpolling.util.RoleType
import grails.test.mixin.Mock
import grails.test.mixin.TestFor
import grails.test.mixin.integration.Integration
import grails.transaction.Rollback
import grails.web.servlet.mvc.GrailsParameterMap
import spock.lang.Specification

import javax.servlet.http.HttpServletRequest

@Integration
@Rollback
@TestFor(PreconditionService)
class PreconditionServiceSpec extends Specification {
    PreconditionService preconditionService = new PreconditionService()
    User inst1
    Course course1

    def setup() {
        println "Starting PreconditionServices testing"
    }

    def cleanup() {
        println "Finishing PreconditionServices testing"
    }


    def prepareData() {
        /* TEST INSTRUCTOR */
        inst1 = new User(email: "test.email@oswego.edu")
        inst1.setRole(new Role(type: RoleType.INSTRUCTOR))
        inst1.setAuthToken(new AuthToken(accessToken: "inst-1000", subject: "inst-1000-subj"))
        inst1.save(flush: true, failOnError: true)

        /* TEST STUDENTS - IN A COURSE */
        def a = new User(firstName: "Jason", lastName: "Parker", email: "a@oswego.edu", imageUrl: "Some image")
        a.setRole(new Role(type: RoleType.STUDENT))
        a.setAuthToken(new AuthToken(subject: 'sub-a-1000', accessToken: 'aa-1000'))
        a.save(flush: true, failOnError: true)

        def b = new User(firstName: "Peter", lastName: "Swanson", email: "b@oswego.edu", imageUrl: "coolest")
        b.setRole(new Role(type: RoleType.STUDENT))
        b.setAuthToken(new AuthToken(subject: 'sub-b-1000', accessToken: 'bb-1000'))
        b.save(flush: true, failOnError: true)

        /* TEST STUDENTS - NOT IN A COURSE */
        def c = new User(firstName: "John", lastName: "Johnson", email: "c@oswego.edu", imageUrl: "Other image")
        c.setRole(new Role(type: RoleType.STUDENT))
        c.setAuthToken(new AuthToken(subject: 'sub-c-1000', accessToken: 'cc-1000'))
        c.save(flush: true, failOnError: true)

        def d = new User(firstName: "Jack", lastName: "Jackson", email: "d@oswego.edu", imageUrl: "Other coolest")
        d.setRole(new Role(type: RoleType.STUDENT))
        d.setAuthToken(new AuthToken(subject: 'sub-d-1000', accessToken: 'dd-1000'))
        d.save(flush: true, failOnError: true)

        /* TEST COURSE */
        def course1 = new Course(name: "CSC 480", crn: 11111, instructor: inst1)
        course1.addToStudents(a)
        course1.addToStudents(b)
        course1.save(flush: true, failOnError: true)
    }

    void "test notNull(): null objects"(){

        given:

        GrailsParameterMap nullMap = null

        List<String> nullParameters = null

        when:
        QueryResult result = preconditionService.notNull(nullMap,nullParameters)

        then:
        thrown InvalidArgumentException




    }

    void "test notNull(): invalid objects"(){
        given:
        Map<String, String> invalidMap = new HashMap<String,String>()
        invalidMap.put("a","stuff")
        invalidMap.put("b","more stuff")
        invalidMap.put("c","dumb stuff")
        HttpServletRequest request = Mock()
        GrailsParameterMap invalidParamMap = new GrailsParameterMap(invalidMap, request)



        List<String> invalidParameters = new ArrayList<>()
        invalidParameters.add("butts")
        invalidParameters.add("jerks")
        invalidParameters.add("potato")

        when:
        QueryResult result = preconditionService.notNull(invalidParamMap,invalidParameters)

        then:
        result != null


    }
    void "test notNull(): valid objects"(){
        given:
        Map<String, String> validMap = new HashMap<String,String>()
        validMap.put("adult","adult user")
        validMap.put("instructor","instructor user")
        validMap.put("student","student user")
        validMap.put("admin","admin user")
        validMap.put("super","super user")
        HttpServletRequest request = Mock()
        GrailsParameterMap validParamMap = new GrailsParameterMap(validMap, request)
        List<String> validParameters = validParamMap.keySet().toList()

        when:
        QueryResult result = preconditionService.notNull(validParamMap,validParameters)

        then:
        result!=null



    }

    void "test accessToken() : null objects"(){
        given:
        String accessToeknString = null

        when:
        QueryResult result = preconditionService.accessToken(accessToeknString)

        then:
        thrown InvalidArgumentException

    }

    void "test accessToken() : invalid objects"(){
        given:
        prepareData()
        String accessToeknString = "jklol"

        when:
        QueryResult result = preconditionService.accessToken(accessToeknString)

        then:
        result !=null

    }

    void "test accessToken() : valid objects"(){
        given:
        prepareData()
        String accessTokenString = "aa-1000"

        when:
        QueryResult result = preconditionService.accessToken(accessTokenString)

        then:
        result !=null
    }

}
