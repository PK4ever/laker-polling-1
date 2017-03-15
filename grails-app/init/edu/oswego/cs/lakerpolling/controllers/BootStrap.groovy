package edu.oswego.cs.lakerpolling.controllers

import edu.oswego.cs.lakerpolling.domains.AuthToken
import edu.oswego.cs.lakerpolling.domains.Course
import edu.oswego.cs.lakerpolling.domains.Role
import edu.oswego.cs.lakerpolling.domains.User
import edu.oswego.cs.lakerpolling.util.RoleType

class BootStrap {

    def init = { servletContext ->

        /* Define students */
        User a = new User(firstName: "Jason", lastName: "Parker", email: "jpark@gmail.com", imageUrl: "Some image")
        a.setRole(new Role(type: RoleType.STUDENT))
        a.setAuthToken(new AuthToken(subject: 'sub-a', accessToken: 'aa'))
        a.save(flush: true, failOnError: true)

        User b = new User(firstName: "Peter", lastName: "Swanson", email: "pswan@coolpeople.com", imageUrl: "coolest")
        b.setRole(new Role(type: RoleType.STUDENT))
        b.setAuthToken(new AuthToken(subject: 'sub-b', accessToken: 'bb'))
        b.save(flush: true, failOnError: true)

        User stu = new User(firstName: "Zack", lastName: "Brown", email: "zb@gmail.com", imageUrl: "The greatest image.jpg")
        stu.setRole(new Role(type: RoleType.STUDENT))
        stu.setAuthToken(new AuthToken(subject: 'sub-stu-1', accessToken: 'cc'))
        stu.save(flush: true, failOnError: true)

        User stu2 = new User(firstName: "Stephen", lastName: "Forgot", email: "steph@gmail.com", imageUrl: "The greatest image.jpg")
        stu2.setRole(new Role(type: RoleType.STUDENT))
        stu2.setAuthToken(new AuthToken(subject: 'sub-stu-2', accessToken: 'dd'))
        stu2.save(flush: true, failOnError: true)

        User michael = new User(firstName: "Michael", lastName: "Cavataio", email: "mcavatai@oswego.edu", imageUrl: "http://media.salon.com/2015/01/chrissy_teigen.jpg")
        michael.setRole(new Role(type: RoleType.STUDENT))
//        michael.setAuthToken(new AuthToken(subject: "michael-stu", accessToken: "ee"))
        michael.save(flush: true, failOnError: true)

        /* End students*/

        /* instructors */
        User inst1 = new User(firstName: "Bastian", lastName: "Temb", email: "bt@oswego.edu", imageUrl: "deutschland.png")
        inst1.setRole(new Role(type: RoleType.INSTRUCTOR))
        inst1.setAuthToken(new AuthToken(subject: "sub-inst-1", accessToken: "ins1"))
        inst1.save(flush: true, failOnError: true)

        User inst2 = new User(firstName: "Chris", lastName: "Harris", email: "charris@oswego", imageUrl: "chris.png")
        inst2.setRole(new Role(type: RoleType.INSTRUCTOR))
        inst2.setAuthToken(new AuthToken(subject: "sub-inst-2", accessToken: "ins2"))
        inst2.save(flush: true, failOnError: true)

        User admin = new User(firstName: "admin", lastName: "admin", email: "cooladmin@gmail.com", imageUrl: "cool")
        admin.setRole(new Role(type: RoleType.ADMIN))
        admin.setAuthToken(new AuthToken(subject: "sub-ad-1", accessToken: "ad1"))
        admin.save(flush: true, failOnError: true)

        /*End instructors*/


        /*Courses*/
        Course csc480 = new Course(name: "CSC 480", crn: 11111, instructor: inst1)
        csc480.addToStudents(a)
        csc480.addToStudents(b)
        csc480.addToStudents(michael);
        csc480.save(flush: true, failOnError: true)


        Course hci521 = new Course(name:  "HCI 521", crn: 22222, instructor: inst2)
        hci521.addToStudents(stu)
        hci521.addToStudents(stu2)
        hci521.addToStudents(michael)
        hci521.save(flush: true, failOnError: true)
        /*End courses*/

    }

    def destroy = {
    }
}
