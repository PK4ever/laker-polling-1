package edu.oswego.cs.lakerpolling.services

import edu.oswego.cs.lakerpolling.BootStrapSpec
import edu.oswego.cs.lakerpolling.domains.AuthToken
import edu.oswego.cs.lakerpolling.domains.Course
import edu.oswego.cs.lakerpolling.domains.Role
import edu.oswego.cs.lakerpolling.domains.User
import edu.oswego.cs.lakerpolling.util.QueryResult
import edu.oswego.cs.lakerpolling.util.RoleType
import grails.web.servlet.mvc.GrailsParameterMap
import org.springframework.beans.factory.annotation.Autowired

import javax.servlet.http.HttpServletRequest
import javax.validation.Valid


class PreconditionServiceSpec extends BootStrapSpec {
@Autowired
PreconditionService service


    void "Test notNull(): 1 - null objects"(){

        given:
        GrailsParameterMap nullMap = null

        List<String> nullParameters = null

        when:
        def  result = service.notNull(nullMap,nullParameters)

        then:
        result.message.equals("Null precondition parameters")



    }

    void "Test notNull(): - 2 invalid objects"(){
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
        testWith([invalidMap: invalidMap, invalidParameters: invalidParameters])
        when:
        QueryResult result = service.notNull(invalidParamMap,invalidParameters)

        then:
        !result.success


    }
    void "Test notNull(): 3 - valid objects"(){
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
        testWith([validMap:validMap, invalidParameters: validParameters])
        when:
        QueryResult result = service.notNull(validParamMap,validParameters)

        then:
        result.success


    }

    void "Test accessToken() : 1 - null objects"(){
        given:
        String accessTokenString = null

        when:
        QueryResult result = service.accessToken(accessTokenString)

        then:
        result.message.equals("Null precondition parameters")

    }

    void "Test accessToken() : 2 - invalid objects"(){
        given:
        testWith(INVALID_STUDENT)
        String accessToeknString = INVALID_STUDENT.authToken.accessToken

        when:
        QueryResult result = AuthToken.withTransaction {service.accessToken(accessToeknString)}

        then:
        !result.success

    }

    void "Test accessToken() : 3 - valid objects"(){
        given:
        testWith(VALID_STUDENT)
        String accessTokenString = VALID_STUDENT.authToken.accessToken

        when:
        QueryResult result = AuthToken.withTransaction {service.accessToken(accessTokenString)}

        then:
        result.success
        result.data.subject == VALID_STUDENT.authToken.subject
        result.data.accessToken==VALID_STUDENT.authToken.accessToken

    }


    void "Test convertToLong(): 1 - valid long"(){
        given:
        String validLong = "12345678"
        String validParam = "Butts"

        testWith([validLong:validLong,validParam: validParam])

        when:
        QueryResult result = service.convertToLong(validLong,validParam)

        then:
        result.success

    }


    void "Test ConvertToLong(): 2 - invalid long"(){
        given:
        String invalidLong = "abcdef"
        String invalidParam = "Butts"

        testWith([invalidLong:invalidLong,invalidParam:invalidParam])

        when:
        QueryResult result = service.convertToLong(invalidLong,invalidParam)

        then:
        !result.success
    }

    void "Test ConvertToLong(): 3 - invalid.b long"(){
        given:
        String invalidLong = "123.22"
        String validParam = "Butts"

        testWith([invalidLong:invalidLong,validParam:validParam])

        when:
        QueryResult result = service.convertToLong(invalidLong,validParam)

        then:
        !result.success
    }

    void "Test ConvertToLong(): 4 - null long"(){
        given:
        String invalidLong = null
        String validParam = "Butts"

        testWith([invalidLong:invalidLong,validParam:validParam])

        when:
        QueryResult result = service.convertToLong(invalidLong,validParam)

        then:
        !result.success
    }

    void "Test ConvertToLong(): 5 - null Param"(){
        given:
        String validLong = "12345678"
        String invalidParam = null

        testWith([validLong:validLong,invalidParam:invalidParam])

        when:
        QueryResult result = service.convertToLong(validLong,invalidParam)

        then:
        !result.success
    }

}
