Feature: gestión de bibliotecas

    Scenario: Añadir un libro a la biblioteca
        Given driver baseUrl + '/user/2'
        And input('#username', 'b')
        And input('#password', 'aa')
        When submit().click(".form-signin button")
        Then waitForUrl(baseUrl + '/user/2')
        Given driver baseUrl
        And click('#addToLib')
        Then waitForUrl(baseUrl + '/save/1111')

#    Scenario: Mirar la biblioteca
#        Given driver baseUrl + '/user/2'
#        And input('#username', 'b')
#        And input('#password', 'aa')
#        When submit().click(".form-signin button")
#        Then waitForUrl(baseUrl + '/user/2')
#        And click('.biblioteca')
#        And delay(1500)
#        Then waitForUrl(baseUrl + '/user/2#library')
