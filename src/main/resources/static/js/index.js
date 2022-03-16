$(document).ready(function() {
    var item, tile, author, publisher, bookLink, bookImg;
    var outputList = document.getElementById("list-output");
    var bookUrl = "https://www.googleapis.com/books/v1/volumes?q=";
    var apiKey = "AIzaSyDtXC7kb6a7xKJdm_Le6_BYoY5biz6s8Lw";
    var placeHldr = '<img src="https://via.placeholder.com/150">';
    var searchData;
  
    //listener del boton search
    $("#search").click(function() {
      outputList.innerHTML = ""; //vaciamos html
      document.body.style.backgroundImage = "url('')";
       searchData = $("#search-box").val();
       //si no damos un input para buscar:
       if(searchData === "" || searchData === null) {
         displayError();
       }
      else {
         
         $.ajax({
            url: bookUrl + searchData,
            dataType: "json",
            success: function(response) {
              console.log(response)
              if (response.totalItems === 0) {
                alert("no hay respuesta, vuelve a intentarlo")
              }
              else {
                $("#title").animate({'margin-top': '5px'}, 1000);
                $(".book-list").css("visibility", "visible");
                displayResults(response);
              }
            },
            error: function () {
              alert("Hubo un error.. <br>"+"Vuelve a intentarlo");
            }
          });

          
        }
        $("#search-box").val(""); //limpieza del search box
     });
  
     
     /*
     * function to display result in index.html
     * @param response
     */
     function displayResults(response) {
      outputList.innerHTML = '<div class="row special-list">';
        for (var i = 0; i < response.items.length; i+=2) {
          item = response.items[i];
          title1 = item.volumeInfo.title;
          author1 = item.volumeInfo.authors;
          publisher1 = item.volumeInfo.publisher;
          bookLink1 = item.volumeInfo.previewLink;
          bookIsbn = item.volumeInfo.industryIdentifiers[1].identifier
          bookImg1 = (item.volumeInfo.imageLinks) ? item.volumeInfo.imageLinks.thumbnail : placeHldr ;
  
          item2 = response.items[i+1];
          title2 = item2.volumeInfo.title;
          author2 = item2.volumeInfo.authors;
          publisher2 = item2.volumeInfo.publisher;
          bookLink2 = item2.volumeInfo.previewLink;
          bookIsbn2 = item2.volumeInfo.industryIdentifiers[1].identifier
          bookImg2 = (item2.volumeInfo.imageLinks) ? item2.volumeInfo.imageLinks.thumbnail : placeHldr ;

          // in production code, item.text should have the HTML entities escaped.
          outputList.innerHTML += '<div class="row mt-4">' + '<div class="col-lg-3 col-md-6 special-grid best-seller">' +
                                  formatOutput(bookImg1, title1, author1, publisher1, bookLink1, bookIsbn) + 

                                  formatOutput(bookImg2, title2, author2, publisher2, bookLink2, bookIsbn2) +
                                  '</div>' ;
  
          console.log(outputList);
        }
        outputList.innerHTML += '</div>' ;
        
        
     }
  
     /*
     * card element formatter using es6 backticks and templates (indivial card)
     * @param bookImg title author publisher bookLink
     * @return htmlCard
     */
     function formatOutput(bookImg, title, author, publisher, bookLink, bookIsbn) {
       // console.log(title + ""+ author +" "+ publisher +" "+ bookLink+" "+ bookImg)
       var viewUrl = 'book.html?isbn='+bookIsbn; //constructing link for bookviewer
       var htmlCard = `    <div class="products-single fix">
                              <div class="box-img-hover">
                                  <div id="imgPortada" class="" ></div>
                                  <img src="${bookImg}"  />
                                  <div class="mask-icon">
                                      <a class="cart" href="#">Agregar a mi biblioteca</a>
                                  </div>
                              </div>
                              <a th:href=@{/libro} href="/libro" class="text-decoration-none link-dark">
                                  <div class="why-text">
                                      
                                      <h3  text="" >${title}</h3>
                                      <h5  > ${author}</h5>
                                      <div class="product-rating mb-2">
                                          <i class="fas fa-star"></i>
                                          <i class="fas fa-star"></i>
                                          <i class="fas fa-star"></i>
                                          <i class="fas fa-star"></i>
                                          <i class="fas fa-star mr-0"></i>
                                      </div>
                                  </div>
                              </a>

                              <form action="/addBook" method="post" >

                  
                              <input type="text" id="autor" name="autor" placeholder="autor"  required>
                                  <input type="text" id="descripcion" name="descripcion" placeholder="Descripcion"  required>
                                  <input type="text" id="fecha" name="fecha" placeholder="fecha"  required>
                                  <input type="text" id="generos" name="generos" placeholder="Generos"  required>
                                  <input type="text" id="ISBN" name="ISBN" placeholder="ISBN"  required>
                                  <input type="text" id="imag" name="imag" placeholder="imag"  required>
                                  <input type="text" id="saga" name="saga" placeholder="saga"  required>
                                  <input type="text" id="titulo" name="titulo" placeholder="titulo"  required>
                                  <input type="text" id="volumen" name="volumen" placeholder="volumen"  required>
                           
                                
                        
                            <button id="sendBook" type="submit" name="submit">Crear</button>
                          
  
                        </form>
                        <br>

                         </div>`

       return htmlCard;
     }
  
     //handling error for empty search box
     function displayError() {
       alert("search term can not be empty!")
     }
     
     
     document.querySelector("#sendBook").onclick = e => {
      e.preventDefault();
      let autor = document.querySelector("#autor");
       let titulo = document.querySelector("#titulo");
       let isbn = document.querySelector("#ISBN");
       let url = document.querySelector("#autor").parentNode.action;
          
       console.log(url, autor, titulo, isbn);
       postBook(titulo, url, "titulo").then(() =>{
       document.getElementById("list-output").appendChild = '<h3>'+ titulo + '</h3>';
   });
  }

      

    function postBook(state, endpoint, name) {
     console.log(name, endpoint, state);
     let fd = new FormData();
     fd.append(name, state);
     return go(endpoint, "POST", fd, {})
   }


  });
  
  
  
