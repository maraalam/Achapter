const OpenBook = {
    response: {},
    request: (isbn, bookform) => {
        console.log("DEBUG: OpenBook.request(...)");
  
        $.ajax({
            'url': `https://openlibrary.org/api/books?bibkeys=ISBN:${isbn}&jscmd=data&format=json`,
            'type': 'GET',
            'success': (data) => {
                OpenBook.response = data[`ISBN:${isbn}`];
                OpenBook.response.isbn = isbn;
                bookform.autoFill();
            },
            'error': (request, error) => {
                OpenBook.response = error;
            }
        });
    },
  };


  function getbooks(){
    document.getElementById('output').innerHTML=""; //limpia la pagina antes de usarla
    fetch("http://openlibrary.org/search.json?q=" + "harry potter")
    .then(a => a.json())/*convertir la salida json a un objeto (diccionario)*/
    .then(response =>
    {for(var i=o;i<10;i++)
        {"harry potter"+="<h2>" + response.docs[i].title+"</h2>" 
        + response.docs[i].author_name[0] + "<br><img src='http://covers.openlibrary.org/b/isbn/" + response.docs[i].isbn[0] +
        +"-M.jpg'><br>";
        }
    });
}