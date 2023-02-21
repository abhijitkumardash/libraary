import { SelectComponent } from "../select.component";
import { Author, IAuthor } from "../../../entities/book/author.model";
import { Component, OnInit } from "@angular/core";
import { AuthorService } from "../../../entities/book/author.service";

@Component({
  selector: "author-select",
  templateUrl: "../select.component.html",
})
export class AuthorSelectComponent extends SelectComponent<IAuthor> implements OnInit {
  heading = "Author";
  constructor(private authorService: AuthorService) {
    super(Author, authorService);
  }

  ngOnInit(): void {
    this.loadData()
  }
}
