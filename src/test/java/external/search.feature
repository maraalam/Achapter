Feature: busqueda de libro

    Scenario: Busqueda de un libro por titulo como b
        Given driver baseUrl + '/user/2'
        And input('#username', 'b')
        And input('#password', 'aa')
        When submit().click(".form-signin button")
        Then waitForUrl(baseUrl + '/user/2')
        Given driver baseUrl + '/user/2'
        And input('#busquedaNav', 'harry')
        And delay(500)
        When submit().click("#submitNav")
        Then waitForUrl(baseUrl + '/buscar?type=titulo&query=harry')

    Scenario: Busqueda de un libro por ISBN como b
        Given driver baseUrl + '/user/2'
        And input('#username', 'b')
        And input('#password', 'aa')
        When submit().click(".form-signin button")
        Then waitForUrl(baseUrl + '/user/2')
        Given driver baseUrl + '/user/2'
        Given select('#type_search', 'isbn')
        And input('#busquedaNav', '0439064864')
        And delay(500)
        When submit().click("#submitNav")
        Then waitForUrl(baseUrl + '/buscar?type=isbn&query=0439064864')