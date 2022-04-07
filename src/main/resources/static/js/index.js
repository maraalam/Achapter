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
              console.log(response);
              if (response.totalItems === 0) {
                console.log("no hay respuesta, vuelve a intentarlo");
              }
              else {
                $("#title").animate({'margin-top': '5px'}, 1000);
                $(".book-list").css("visibility", "visible");
                displayResults(response);
              }
            },
            error: function () {
              console.log("Hubo un error.. <br>"+"Vuelve a intentarlo");
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
      outputList.innerHTML = '<div class="products-box"> <div class="container"> <div class="row"> <div class="col-lg-12"> <div class="title-all ">' +
                            '<h1>Resultados de la Busqueda</h1>  </div>  </div>  </div> <div class="row special-list"> <div class="col-lg-3 col-md-6 special-grid best-seller" >';
          
          console.log("Respuesta: " + response.items.length);
        for (var i = 0; i < response.items.length ; i+=1) {
          item = response.items[i];
          title1 = item.volumeInfo.title;
          author1 = item.volumeInfo.authors;
          publisher1 = item.volumeInfo.publisher;
          bookLink1 = item.volumeInfo.previewLink;
          bookIsbn = item.volumeInfo.industryIdentifiers[0].identifier
          bookImg1 = (item.volumeInfo.imageLinks) ? item.volumeInfo.imageLinks.thumbnail : placeHldr ;
          bookPageCount = item.volumeInfo.pageCount;
          bookCategories="";
          if(item.volumeInfo.categories!=null){
          for(var j=0; j<item.volumeInfo.categories.length; j+=1 ){
            bookCategories += item.volumeInfo.categories[j].trim();
            if(j != item.volumeInfo.categories.length -1)
              bookCategories+=";";
          }
        }
          console.log(bookCategories);
 
          // in production code, item.text should have the HTML entities escaped.
          outputList.innerHTML +=  formatOutput(bookImg1, title1, author1, publisher1, bookLink1, bookIsbn,bookPageCount, bookCategories) ;
                                //  formatOutput(bookImg2, title2, author2, publisher2, bookLink2, bookIsbn2) +
                                 
  
          console.log("Libros google: " + outputList);
        }
        outputList.innerHTML +='</div> </div>  </div> </div>';
     }
  
     /*
     * card element formatter using es6 backticks and templates (indivial card)
     * @param bookImg title author publisher bookLink
     * @return htmlCard
     */
     function formatOutput(bookImg, title, author, publisher, bookLink, bookIsbn ,bookPageCount, bookCategories) {
       // console.log(title + ""+ author +" "+ publisher +" "+ bookLink+" "+ bookImg)
       

       var viewUrl = 'book.html?isbn='+bookIsbn; //constructing link for bookviewer
       var htmlCard = ` 
       
       <div class="products-single fix">
          <div class="box-img-hover">
              <div id="imgPortada" class="" ></div>
              <div class="col-md-7">
              <img src="${bookImg}"  alt="img" width="100px" height="200px" />
              </div>
          </div>
          <a  class="text-decoration-none link-dark">
              <div class="why-text">
                  
                  <h3 th:text="${title}" >${title}</h3>
                  <h5 th:text="${author}" > ${author}</h5>
                  <div class="product-rating mb-2">
                      <i class="fas fa-star"></i>
                      <i class="fas fa-star"></i>
                      <i class="fas fa-star"></i>
                      <i class="fas fa-star"></i>
                      <i class="fas fa-star mr-0"></i>
                  </div>
                  <button id="sendBook" type="submit" name="submit" onclick="return submitBook();">Guardar En BD</button>
              </div>
          </a>
    </div>
       
  `

      
       return htmlCard;
     }
  
     //handling error for empty search box
     function displayError() {
      Console.log("search term can not be empty!")
     }

 
     
  });

  
  /*

  <div class="col-lg-6">
         <div class="card" style="">
           <div class="row no-gutters">
             <div class="col-md-7">
               <img src="${bookImg}" class="card-img" alt="...">
             </div> <br>
             <div class="col-md-10">
               <div class="card-body">

                 <div class="why-text">
                      <h5 id ="title" class="booktitle" "> ${title} </h5>
                      <h6 id ="author"> ${author}</h6>
                      <h6 id ="publisher"> ${publisher}</h6>
                      <h6 hidden id ="isbn"> ${bookIsbn}</h6>
                      <img  id ="img" src="${bookImg}" class="card-img" alt="..." hidden>
                      <h6 hidden id ="pagecount"> ${bookPageCount}</h6>
                      <h6 hidden id ="categories"> ${bookCategories}</h6>
                      <button id="sendBook" type="submit" name="submit" onclick="return submitBook();">GuardarLista</button>
                      <br>
                      <br>
                  </div>

               </div>
             </div>
           </div>
         </div>
       </div> <br>
  */