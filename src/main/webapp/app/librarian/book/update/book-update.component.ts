import { COMMA, ENTER } from "@angular/cdk/keycodes";
import { Component, ElementRef, OnInit, ViewChild } from "@angular/core";
import { FormControl, FormGroup, Validators } from "@angular/forms";
import { ActivatedRoute } from "@angular/router";
import { BookItemService } from "../../../entities/book-item/bookitem.service";
import { BookService } from "../../../entities/book/book.service";
import { AuthorService } from "../../../entities/book/author.service";
import { Author, IAuthor } from "../../../entities/book/author.model";
import { MatChipInputEvent } from "@angular/material/chips";
import { MatAutocompleteSelectedEvent } from "@angular/material/autocomplete";
import { Observable } from "rxjs";
import { map, startWith } from "rxjs/operators";
import { IBook } from "../../../entities/book/book.model";

@Component({
  selector: "book-update",
  templateUrl: "./book-update.component.html",
  styleUrls: ["./book-update.component.scss"]
})
export class BookUpdateComponent implements OnInit {

  id: string | null = null;
  isSaving = false;
  isLoading = false;
  coverImage: string = "";
  authors: IAuthor[] = [];
  allAuthors: IAuthor[] = [];
  filteredAuthors: Observable<IAuthor[]>;
  separatorKeysCodes: number[] = [ENTER, COMMA];
  isbnForm = new FormGroup({
    isbn: new FormControl("", { validators: [Validators.required, Validators.minLength(13), Validators.maxLength(13)] })
  });
  editForm = new FormGroup({
    title: new FormControl("", { validators: [Validators.required, Validators.maxLength(100)] }),
    subtitle: new FormControl("", { validators: [Validators.required, Validators.maxLength(200)] }),
    description: new FormControl("", { validators: [Validators.required] }),
    author: new FormControl("", { validators: [Validators.required] }),
    publisher: new FormControl("", { validators: [Validators.required] }),
    publicationYear: new FormControl("", {
      validators: [Validators.required, Validators.pattern("^[0-9]*$")]
    }),
    pages: new FormControl("", { validators: [Validators.required, Validators.pattern("^[0-9]*$")]})
  });

  @ViewChild("authorInput") authorInput!: ElementRef<HTMLInputElement>;
  @ViewChild("isbnInput") isbnInput!: ElementRef<HTMLInputElement>;

  constructor(private bookItemService: BookItemService, private bookService: BookService,
              private authorService: AuthorService, private route: ActivatedRoute) {
    this.filteredAuthors = this.editForm.get("author")!.valueChanges.pipe(
      startWith(null),
      map((author: string | null) => (author ? this._filter(author) : this.allAuthors.slice()))
    );
  }

  previousState(): void {
    window.history.back();
  }

  ngOnInit(): void {
    this.route.data.subscribe(({ bookItem }) => {
      if (bookItem) {
        this.id = bookItem.id;
        this.editForm.patchValue(bookItem);
      } else (
        this.editForm.disable()
      );
    });
    this.loadData();
  }

  searchForBookData(): void {
    this.isLoading = true;
    this.isbnInput.nativeElement.select();
    this.reset();
    this.bookService.findByIsbn(this.isbnForm.get("isbn")?.value ?? "").subscribe(
      (res) => {
        this.loadBookToForm(res.body);
        this.isLoading = false;
      }, (err) => {
        this.bookService.searchByISBN(this.isbnForm.get("isbn")?.value ?? "").subscribe(
          (res) => {
            this.loadBookToForm(res.body);
            this.isLoading = false;
          },
          (err) => {
            this.isLoading = false;
          }
        );
      }
    );
    this.editForm.enable();
  }

  loadBookToForm(book: IBook | null) {
    this.editForm.patchValue(
      {
        title: book?.title ?? "",
        subtitle: book?.subtitle ?? "",
        description: book?.description ?? "",
        publisher: book?.publisher ?? "",
        publicationYear: book?.publicationYear ?? "",
        pages: book?.pages.toString() ?? ""
      }
    );
    this.coverImage = book?.cover ?? "";
    this.authors = book?.authors ?? [];
  }

  private reset() {
    this.coverImage = "";
    this.editForm.reset();
    this.authors = [];
  }

  add(event: MatChipInputEvent): void {
    const value = (event.value || "").trim();
    if (value) {
      this.authors.push(new Author(this.editForm.get("author")?.value ?? ""));
    }

    event.chipInput!.clear();

    this.editForm.get("author")?.setValue(null);
  }


  remove(author: IAuthor): void {
    this.authors = this.authors.filter(f => f.name !== author.name);
  }

  selected(event: MatAutocompleteSelectedEvent): void {
    if (this.authorInput.nativeElement.value != "") {
      this.authors.push(this.allAuthors.find(f => f.name === event.option.value) ?? new Author(event.option.value));
    }
    this.authorInput.nativeElement.value = "";
    this.editForm.get("author")?.setValue(null);
  }

  private _filter(value: string): IAuthor[] {
    const filterValue = value.toLowerCase();

    return this.allAuthors.filter(author => author.name.toLowerCase().includes(filterValue));
  }

  private loadData() {
    this.authorService.query().subscribe(
      (res) => {
        this.allAuthors = res.body ?? [];
      }
    );
  }
}
