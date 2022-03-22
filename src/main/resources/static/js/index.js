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
        for (var i = 0; i < 2/*response.items.length*/; i+=2) {
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
          outputList.innerHTML += '<div class="row mt-4">' +
                                  formatOutput(bookImg1, title1, author1, publisher1, bookLink1, bookIsbn) + '<br> <br> <br>  <br> <br> <br> <br> <br> <br> <br> <br> <br> <br> <br> <br> <br> '+
                                //  formatOutput(bookImg2, title2, author2, publisher2, bookLink2, bookIsbn2) +
                                  '</div>';
  
          console.log(outputList);
        }
     }
  
     /*
     * card element formatter using es6 backticks and templates (indivial card)
     * @param bookImg title author publisher bookLink
     * @return htmlCard
     */
     function formatOutput(bookImg, title, author, publisher, bookLink, bookIsbn) {
       // console.log(title + ""+ author +" "+ publisher +" "+ bookLink+" "+ bookImg)
       

       var viewUrl = 'book.html?isbn='+bookIsbn; //constructing link for bookviewer
       var htmlCard = `<div class="col-lg-6">
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
                      <button id="sendBook" type="submit" name="submit" onclick="return submitBook();">GuardarLista</button>
                      <br>
                      <br>
                  </div>

               </div>
             </div>
           </div>
         </div>
       </div> <br>`

      
       return htmlCard;
     }
  
     //handling error for empty search box
     function displayError() {
       alert("search term can not be empty!")
     }

 
     
  });

  
  