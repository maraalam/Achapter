package external;

import com.intuit.karate.junit5.Karate;

class ExternalRunner {
    
    /*@Karate.Test
    Karate testLogin() {
        return Karate.run("login").relativeTo(getClass());
    }*/

    /*@Karate.Test
    Karate testWs() {
        return Karate.run("ws").relativeTo(getClass());
    }  */

    @Karate.Test
    Karate testSearch() {
        return Karate.run("search").relativeTo(getClass());
    }
    
    @Karate.Test
    Karate testBook() {
        return Karate.run("book").relativeTo(getClass());
    } 
}