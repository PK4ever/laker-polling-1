package edu.oswego.cs.lakerpolling.services

import edu.oswego.cs.lakerpolling.domains.AuthToken
import edu.oswego.cs.lakerpolling.domains.Course
import edu.oswego.cs.lakerpolling.domains.Role
import edu.oswego.cs.lakerpolling.domains.User
import edu.oswego.cs.lakerpolling.util.QueryResult
import edu.oswego.cs.lakerpolling.util.RoleType
import grails.test.mixin.TestFor
import grails.test.mixin.integration.Integration
import grails.transaction.Rollback
import grails.web.servlet.mvc.GrailsParameterMap
import spock.lang.Specification

import javax.servlet.http.HttpServletRequest

@Rollback
@Integration
@TestFor(PreconditionService)
class PreconditionServiceSpec extends Specification {

    AuthToken bad_auth
    User admn, badmn, inst1, inst2, binst, a, b, c, d, bad
    Course course1, course2

    def setup() {
        println "Starting PreconditionServices testing"

    }

    def cleanup() {
        println "Finishing PreconditionServices testing"
    }


    def prepareData() {
        /* TEST AUTHTOKEN - Unassigned AuthToken */
        bad_auth = new AuthToken(accessToken: "fail", subject: "fail-subj")

        /* TEST ADMIN - Valid Email */
        admn = new User(email: "test.admin@oswego.edu")
        admn.setRole(new Role(type: RoleType.ADMIN, master: RoleType.ADMIN))
        admn.setAuthToken(new AuthToken(accessToken: "admn-1000", subject: "admn-1000-subj"))
        admn.save(flush:true, failOnError: true)

        /* TEST ADMIN - Invalid Email */
        badmn = new User(email: "bad.admin@bad.com")
        badmn.setRole(new Role(type: RoleType.ADMIN, master: RoleType.ADMIN))
        badmn.setAuthToken(new AuthToken(accessToken: "badmn-1000", subject: "badmn-1000-subj"))
        badmn.save(flush:true, failOnError: true)

        /* TEST INSTRUCTORS - Valid Emails */
        inst1 = new User(email: "test.email@oswego.edu")
        inst1.setRole(new Role(type: RoleType.INSTRUCTOR, master: RoleType.INSTRUCTOR))
        inst1.setAuthToken(new AuthToken(accessToken: "inst-1000", subject: "inst-1000-subj"))
        inst1.save(flush: true, failOnError: true)

        inst2 = new User(email: "test.email2@oswego.edu")
        inst2.setRole(new Role(type: RoleType.INSTRUCTOR, master: RoleType.INSTRUCTOR))
        inst2.setAuthToken(new AuthToken(accessToken: "inst2-1000", subject: "inst2-1000-subj"))
        inst2.save(flush: true, failOnError: true)

        /* TEST INSTRUCTOR - Invalid Email */
        binst = new User(email: "bad.inst@bad.com")
        binst.setRole(new Role(type: RoleType.INSTRUCTOR, master: RoleType.INSTRUCTOR))
        binst.setAuthToken(new AuthToken(accessToken: "binst-1000", subject: "binst-1000-subj"))
        binst.save(flush: true, failOnError: true)

        /* TEST STUDENTS - In A Course */
        a = new User(firstName: "Jason", lastName: "Parker", email: "a@oswego.edu", imageUrl: "Some image")
        a.setRole(new Role(type: RoleType.STUDENT, master: RoleType.STUDENT))
        a.setAuthToken(new AuthToken(subject: 'sub-a-1000', accessToken: 'aa-1000'))
        a.save(flush: true, failOnError: true)

        b = new User(firstName: "Peter", lastName: "Swanson", email: "b@oswego.edu", imageUrl: "coolest")
        b.setRole(new Role(type: RoleType.STUDENT, master: RoleType.STUDENT))
        b.setAuthToken(new AuthToken(subject: 'sub-b-1000', accessToken: 'bb-1000'))
        b.save(flush: true, failOnError: true)

        /* TEST STUDENTS - Not In A Course */
        c = new User(firstName: "John", lastName: "Johnson", email: "c@oswego.edu", imageUrl: "Other image")
        c.setRole(new Role(type: RoleType.STUDENT, master: RoleType.STUDENT))
        c.setAuthToken(new AuthToken(subject: 'sub-c-1000', accessToken: 'cc-1000'))
        c.save(flush: true, failOnError: true)

        d = new User(firstName: "Jack", lastName: "Jackson", email: "d@oswego.edu", imageUrl: "Other coolest")
        d.setRole(new Role(type: RoleType.STUDENT, master: RoleType.STUDENT))
        d.setAuthToken(new AuthToken(subject: 'sub-d-1000', accessToken: 'dd-1000'))
        d.save(flush: true, failOnError: true)

        /* TEST STUDENT - Invalid Email */
        bad = new User(firstName: "Bad", lastName: "Guy", email: "bad.guy@bad.com", imageUrl: "Some Other Coolest")
        bad.setRole(new Role(type: RoleType.STUDENT, master: RoleType.STUDENT))
        bad.setAuthToken(new AuthToken(subject: 'sub-bad-1000', accessToken: 'bad-1000'))
        bad.save(flush: true, failOnError: true)

        /* TEST COURSE - Contains Students a And b */
        course1 = new Course(name: "CSC 480", crn: 11111, instructor: inst1)
        course1.addToStudents(a)
        course1.addToStudents(b)
        course1.save(flush: true, failOnError: true)

        /* TEST COURSE - Contains No Students */
        course2 = new Course(name: "CSC 212", crn: 22222, instructor: inst2)
        course2.save(flush: true, failOnError: true)
    }


    void "test notNull(): null objects"(){

        given:
        GrailsParameterMap nullMap = null

        List<String> nullParameters = null

        when:
        def  result = service.notNull(nullMap,nullParameters)

        then:
        result.message.equals("Null precondition parameters")



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
        QueryResult result = service.notNull(invalidParamMap,invalidParameters)

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
        QueryResult result = service.notNull(validParamMap,validParameters)

        then:
        result!=null


    }

    void "test accessToken() : null objects"(){
        given:
        String accessToeknString = null

        when:
        QueryResult result = service.accessToken(accessToeknString)

        then:
        result.message.equals("Null precondition parameters")

    }

    void "test accessToken() : invalid objects"(){
        given:
        prepareData()
        String accessToeknString = "jklol"

        when:
        QueryResult result = service.accessToken(accessToeknString)

        then:
        result !=null

    }

    void "test accessToken() : valid objects"(){
        given:
        prepareData()
        String accessTokenString = "aa-1000"

        when:
        QueryResult result = service.accessToken(accessTokenString)

        then:
        result !=null
    }

}
