package edu.oswego.cs.lakerpolling.controllers

import edu.oswego.cs.lakerpolling.domains.Role
import edu.oswego.cs.lakerpolling.domains.User
import edu.oswego.cs.lakerpolling.util.RoleType

class BootStrap {

    def init = { servletContext ->

        if (!User.findByEmail("bastian.tenbergen@oswego.edu")) {
            User user = new User(email: "bastian.tenbergen@oswego.edu")
            user.setRole(new Role(type: RoleType.INSTRUCTOR, master: RoleType.INSTRUCTOR))
            user.save(flush:true)
        }

    }

    def destroy = {
    }
}
