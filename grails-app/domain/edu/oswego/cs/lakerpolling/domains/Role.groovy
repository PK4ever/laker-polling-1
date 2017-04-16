package edu.oswego.cs.lakerpolling.domains

import edu.oswego.cs.lakerpolling.util.RoleType

class Role {

    RoleType type
    RoleType master

    static belongsTo = [user: User]
    static mapping = {}

}
