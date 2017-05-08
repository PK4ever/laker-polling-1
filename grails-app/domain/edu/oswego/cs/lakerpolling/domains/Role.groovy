package edu.oswego.cs.lakerpolling.domains

import edu.oswego.cs.lakerpolling.util.RoleType

class Role {

    RoleType type
    RoleType master

    static belongsTo = [user: User]
    static mapping = {}

    static constraints = {
        master validator: { RoleType b, Role self ->
            RoleType a = self.type
            if (a == b) {
                true
            } else {
                if (b == RoleType.ADMIN) {
                    a == RoleType.INSTRUCTOR || a == RoleType.STUDENT
                } else {
                    a == RoleType.STUDENT
                }
            }
        }
    }

}
