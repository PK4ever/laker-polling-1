package edu.oswego.cs.lakerpolling.services

import edu.oswego.cs.lakerpolling.domains.AuthToken
import edu.oswego.cs.lakerpolling.domains.Role
import edu.oswego.cs.lakerpolling.domains.User
import edu.oswego.cs.lakerpolling.util.RoleType
import grails.test.mixin.Mock
import grails.test.mixin.TestFor
import grails.test.mixin.integration.Integration
import org.springframework.test.annotation.Rollback
import spock.lang.Specification

@Integration
@Rollback
@TestFor(UserService)
@Mock([User, AuthToken])
class UserServiceSpec extends Specification {

    User notInstructor
    User validInstructor

    def setup() {
        println "Starting UserService Tests"
    }

    def cleanup() {
        println "Stopping UserService Tests"
    }

    /* Necessary for populating the in-memory database.  Mocks do not work, since they will null all values, and
    self-destruct itself upon trying to set values (no clue why).  This means that we are going to have to use the
    Integration test annotation with the rollback to ensure that it reverts changes to the db (seemed to bug out without
    this annotation).  This method allows you to just call it at the start of every test under the when: tag, and
    populate the db on each trial. */

    def prepareData() {
        /* TEST INSTRUCTOR */
        validInstructor = new User(email: "test.email@oswego.edu")
        validInstructor.setRole(new Role(type: RoleType.INSTRUCTOR, master: RoleType.INSTRUCTOR))
        validInstructor.save()

        AuthToken token1 = new AuthToken(accessToken: "inst-1000", subject: "inst-1000-subj", user: validInstructor)
        token1.save(flush: true)

        /* TEST STUDENTS - IN A COURSE */
        notInstructor = new User(firstName: "Jason", lastName: "Parker", email: "a@oswego.edu", imageUrl: "Some image")
        notInstructor.setRole(new Role(type: RoleType.STUDENT, master: RoleType.STUDENT))
        notInstructor.save()

        AuthToken token2 = new AuthToken(accessToken: "inst-199199", subject: "inst-1000333233-subj", user: notInstructor)
        token2.save(flush: true)
    }

    void "test checkIfInstructor(): Not Instructor"() {
        when:
        prepareData()
        then:
        def instructor = service.checkIfInstructor(notInstructor)

        if (instructor) {
            print("User is not an instructor so this shouldn't return true")
            assert false
        }
    }

    void "test checkIfInstructor(): Valid Instructor"() {
        when:
        prepareData()
        then:
        def instructor = service.checkIfInstructor(validInstructor)

        if (!instructor) {
            print("User is a valid instructor so this should return true")
            assert false
        }
    }

    void "test getUser(): Invalid Token"() {
        when:
        prepareData()
        then:
        def user = service.getUser("")

        if (user.message != UserService.UserErrors.INVALID_ACCESS_TOKEN) {
            print("User should show it has a invalid access token")
            assert false
        }
    }

    void "test getUser(): Valid Token"() {
        when:
        prepareData()
        then:
        def user = service.getUser("aa-1000")

        if (user == null) {
            print("User should be found with a valid token")
            assert false
        }
    }

    void "test getOrMakeByEmail(): Invalid email"() {
        when:
        prepareData()
        then:
        def user = service.getOrMakeByEmail("joe@harvard.edu")

        if (user != null) {
            print("User should not be created or retrieved with a invalid email")
            assert false
        }
    }

    void "test getOrMakeByEmail(): Valid email"() {
        when:
        prepareData()
        then:
        def user = service.getOrMakeByEmail("kmartin5@oswego.edu")

        if (user == null) {
            print("User should be created or retrieved with valid email")
            assert false
        }
    }

    void "test getMakeOrUpdate(): All Invalid EQ Classes"() {
        when:
        prepareData()
        then:
        def invalidUserAuthPair = service.getMakeOrUpdate("", "", "", "", "")

        if (invalidUserAuthPair != null) {
            print("User and Auth token pair should not be created with invalid user data passed in")
            assert false
        }
    }

    void "test getMakeOrUpdate(): All Valid EQ Classes"() {
        when:
        prepareData()
        then:

        def validUserAuthPair = service.getMakeOrUpdate("csc212", "Keith", "Martin", "Some image", "kmartin5@oswego.edu")

        if (validUserAuthPair == null) {
            print("User and Auth token pair should be created with valid user data passed in")
            assert false
        }
    }
}

