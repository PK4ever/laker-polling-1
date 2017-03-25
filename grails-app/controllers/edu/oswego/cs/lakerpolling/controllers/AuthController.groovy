package edu.oswego.cs.lakerpolling.controllers

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken
import edu.oswego.cs.lakerpolling.domains.AuthToken
import edu.oswego.cs.lakerpolling.domains.User
import edu.oswego.cs.lakerpolling.services.UserService
import edu.oswego.cs.lakerpolling.services.VerifierService
import edu.oswego.cs.lakerpolling.util.Pair
import edu.oswego.cs.lakerpolling.util.QueryResult

class AuthController {
    static responseFormats = ['json', 'xml']

    VerifierService verifierService
    UserService userService

    /**
     * Verifies and takes action based on the given idToken query parameter.
     */
    def auth(String idToken) {
        QueryResult<GoogleIdToken> data = verifierService.getVerifiedResults(idToken)
        if (data.success) {
            GoogleIdToken.Payload payload = data.data.payload
            String subj = payload.getSubject()
            String first = payload.get("given_name").toString()
            String last = payload.get("family_name").toString()
            String imageUrl = payload.get("picture").toString()
            String accessTokenHash = payload.getAccessTokenHash()
            String email = payload.getEmail()

            Optional<Pair<User, AuthToken>> optionalInfo = userService.getMakeOrUpdate(
                    subj, first, last, imageUrl, accessTokenHash, email
            )

            if (optionalInfo.isPresent()) {
                Pair<User, AuthToken> info = optionalInfo.get()
                session.setAttribute("access", info.value.accessToken)
                render(view: 'authUser')
            } else {
                render(view: '../error')
            }
        } else {
            render(view: '../failure', model: [errorCode: data.errorCode, message: data.message])
        }
    }

    def logout() {
        String access = session.getAttribute("access")
        if (access != null) {
            session.removeAttribute("access")
        }
        session.invalidate()
        render(view: 'authUser')
    }

}
