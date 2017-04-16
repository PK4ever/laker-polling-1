package edu.oswego.cs.lakerpolling.controllers

import edu.oswego.cs.lakerpolling.domains.Attendance
import edu.oswego.cs.lakerpolling.domains.Attendee
import edu.oswego.cs.lakerpolling.domains.AuthToken
import edu.oswego.cs.lakerpolling.domains.Course
import edu.oswego.cs.lakerpolling.domains.Role
import edu.oswego.cs.lakerpolling.domains.User
import edu.oswego.cs.lakerpolling.util.RoleType

class BootStrap {

    def init = { servletContext ->

        /* Define students */
        User a = new User(firstName: "Jason", lastName: "Parker", email: "jpark@gmail.com", imageUrl: "Some image")
        a.setRole(new Role(type: RoleType.STUDENT, master: RoleType.STUDENT))
        a.setAuthToken(new AuthToken(subject: 'sub-a', accessToken: 'aa'))
        a.save(flush: true)

        User b = new User(firstName: "Peter", lastName: "Swanson", email: "pswan@coolpeople.com", imageUrl: "coolest")
        b.setRole(new Role(type: RoleType.STUDENT, master: RoleType.STUDENT))
        b.setAuthToken(new AuthToken(subject: 'sub-b', accessToken: 'bb'))
        b.save(flush: true)

        User stu = new User(firstName: "Zack", lastName: "Brown", email: "zb@gmail.com", imageUrl: "The greatest image.jpg")
        stu.setRole(new Role(type: RoleType.STUDENT, master: RoleType.STUDENT))
        stu.setAuthToken(new AuthToken(subject: 'sub-stu-1', accessToken: 'cc'))
        stu.save(flush: true)

        User stu2 = new User(firstName: "Stephen", lastName: "Forgot", email: "steph@gmail.com", imageUrl: "The greatest image.jpg")
        stu2.setRole(new Role(type: RoleType.STUDENT, master: RoleType.STUDENT))
        stu2.setAuthToken(new AuthToken(subject: 'sub-stu-2', accessToken: 'dd'))
        stu2.save(flush: true)

        User michael = new User(firstName: "Michael", lastName: "Cavataio", email: "mcavatai@oswego.edu", imageUrl: "http://media.salon.com/2015/01/chrissy_teigen.jpg")
        michael.setRole(new Role(type: RoleType.STUDENT, master: RoleType.STUDENT))
//        michael.setAuthToken(new AuthToken(subject: "michael-stu", accessToken: "ee"))
        michael.save(flush: true)

        User max = new User(firstName: "Max", lastName: "Sokolovsky", email: "msokolov@oswego.edu", imageUrl: "http://media.salon.com/2015/01/chrissy_teigen.jpg")
        max.setRole(new Role(type: RoleType.STUDENT, master: RoleType.STUDENT))
        max.save(flush: true)

        User mike = new User(firstName: "Mike", lastName: "Mekker", email: "mmekker@oswego.edu", imageUrl: "http://media.salon.com/2015/01/chrissy_teigen.jpg")
        mike.setRole(new Role(type: RoleType.STUDENT, master: RoleType.STUDENT))
        mike.save(flush: true)

        User mike2 = new User(firstName: "Mike", lastName: "Other", email: "someemail222@oswego.edu", imageUrl: "http://media.salon.com/2015/01/chrissy_teigen.jpg")
        mike2.setRole(new Role(type: RoleType.STUDENT, master: RoleType.STUDENT))
        mike2.save(flush: true)

        User paul = new User(firstName: "Paul", lastName: "Kwoyelo", email: "pkwoyelo@oswego.edu", imageUrl: "http://media.salon.com/2015/01/chrissy_teigen.jpg")
        paul.setRole(new Role(type: RoleType.INSTRUCTOR, master: RoleType.STUDENT))
        paul.save(flush: true)

        User brad = new User(firstName: "Brandon", lastName: "Lanthrip", email: "blanthri@oswego.edu", imageUrl: "http://media.salon.com/2015/01/chrissy_teigen.jpg")
        brad.setRole(new Role(type: RoleType.INSTRUCTOR, master: RoleType.STUDENT))
        brad.save(flush: true)

        User jeff = new User(email: "jregistr@oswego.edu")
        jeff.setRole(new Role(type: RoleType.STUDENT, master: RoleType.STUDENT))
        jeff.save(flush: true)

        /* End students*/

        /* instructors */
        User inst1 = new User(email: "bastian.tenbergen@oswego.edu")
        inst1.setRole(new Role(type: RoleType.INSTRUCTOR, master: RoleType.INSTRUCTOR))
        inst1.setAuthToken(new AuthToken(accessToken: "inst-1", subject: "inst-1-subj"))
        inst1.save(flush: true)

        User inst2 = new User(email: "christopher.harris@oswego.edu")
        inst2.setRole(new Role(type: RoleType.INSTRUCTOR, master: RoleType.INSTRUCTOR))
        inst2.save(flush: true)

        User tyler = new User(email: "tmoson@oswego.edu")
        tyler.setRole(new Role(type: RoleType.INSTRUCTOR, master: RoleType.INSTRUCTOR))
        tyler.save(flush: true)

        User linc = new User(email: "ldaniel@oswego.edu")
        linc.setRole(new Role(type: RoleType.INSTRUCTOR, master: RoleType.INSTRUCTOR))
        linc.save(flush: true)

        User admin = new User(firstName: "admin", lastName: "admin", email: "cooladmin@gmail.com", imageUrl: "cool")
        admin.setRole(new Role(type: RoleType.ADMIN, master: RoleType.ADMIN))
        admin.setAuthToken(new AuthToken(subject: "sub-ad-1", accessToken: "ad1"))
        admin.save(flush: true)

        /*End instructors*/


        /*Courses*/
        Course csc480 = new Course(name: "CSC 480", crn: 11111, instructor: inst1)
        csc480.addToStudents(a)
        csc480.addToStudents(b)
        csc480.addToStudents(michael)
        csc480.addToStudents(max)
        csc480.save(flush: true)

        Course csc212 = new Course(name: "CSC 480", crn: 11111, instructor: tyler)
        csc212.addToStudents(a)
        csc212.addToStudents(b)
        csc212.addToStudents(michael);
        csc212.save(flush: true)


        Course hci521 = new Course(name:  "HCI 521", crn: 22222, instructor: linc)
        hci521.addToStudents(stu)
        hci521.addToStudents(stu2)
        hci521.addToStudents(michael)
        hci521.save(flush: true)
        /*End courses*/

        /*Attendance*/
//        Attendee brandon = new Attendee(attended: true, student: stu)
//        Date someDate = new Date("1/22/91")
//        Attendance something = new Attendance(date: someDate, course: csc480)
//        something.addToAttendees(brandon)
//        something.save(flush: true)
//        brandon.save(flusth: true)

        Attendance at1 = new Attendance(date: new Date("2017/04/12"), course: csc480)
        at1.addToAttendees(new Attendee(attended: true, student: a))
        at1.addToAttendees(new Attendee(attended: true, student: b))
        at1.addToAttendees(new Attendee(attended: false, student: michael))
        at1.addToAttendees(new Attendee(attended: true, student: max))
        at1.save(flush:true)

        Attendance at2 = new Attendance(date: new Date("2017/04/13"), course: csc480)
        at2.addToAttendees(new Attendee(attended: false, student: a))
        at2.addToAttendees(new Attendee(attended: true, student: b))
        at2.addToAttendees(new Attendee(attended: true, student: michael))
        at2.addToAttendees(new Attendee(attended: true, student: max))
        at2.save(flush:true)

        Attendance at3 = new Attendance(date: new Date("2017/04/14"), course: csc480)
        at3.addToAttendees(new Attendee(attended: false, student: a))
        at3.addToAttendees(new Attendee(attended: true, student: b))
        at3.addToAttendees(new Attendee(attended: true, student: michael))
        at3.addToAttendees(new Attendee(attended: true, student: max))
        at3.save(flush:true)

    }

    def destroy = {
    }
}
