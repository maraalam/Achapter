const Book = {
    // constructor that creates a book from the openbook api
    from: (obResponse) => {
      console.log("DEBUG: Book.from(...)");
      return new ImplBook(obResponse);
    },
    parseAuthor: (openbook) => {
      console.log("DEBUG: Book.parseAuthor(...)");
      if(typeof(openbook.authors) === "undefined"){
        return "none";
      }else{
        let authors = "";
        openbook.authors.forEach( (author) => {
          authors += author.name + ", ";
        });
        // cut off the last comma and space
        return authors.substring(0, authors.length - 2);
      }
    },
    parseSynopsis: (openbook) => {
      console.log("DEBUG: Book.parseSynopsis(...)");
      if(typeof(openbook.excerpts) === "undefined" || openbook.excerpts.length === 0){
        return "none";
      }else{
        return openbook.excerpts[0].text;
      }
    },
    parseImage: (openbook) => {
      console.log("DEBUG: Book.parseImage(...)");
  
      if(openbook.cover.large !== undefined){
        return openbook.cover.large;
      }
      else if(openbook.cover.medium !== undefined){
        return openbook.cover.medium;
      }
      else if(openbook.cover.small !== undefined){
        return openbook.cover.small;
      }
      else{
        return "/images/bookexample.jpeg";
      }
    },
  };
  
  // obResponse is the response object of the OpenBook Api
  function ImplBook(obResponse){
    console.log("DEBUG: ImplBook(...)");
  
    this.isbn = obResponse.isbn;
    this.title = obResponse.title;
    this.author = Book.parseAuthor(obResponse);
    this.synopsis = Book.parseSynopsis(obResponse);
    this.pages = obResponse.number_of_pages;        // TODO: use this
    this.publishDate = obResponse.publish_date;     // TODO: use this
    this.image = Book.parseImage(obResponse);
    this.wear = 1;
    // TODO: get weight as well
  }