import { Component, ElementRef, OnInit, ViewChild } from "@angular/core";
import { FormControl, FormGroup, Validators } from "@angular/forms";
import { ActivatedRoute } from "@angular/router";
import { BookItemService } from "../../../entities/book-item/bookitem.service";
import { BookService } from "../../../entities/book/book.service";
import { IAuthor } from "../../../entities/book/author.model";
import { IBook } from "../../../entities/book/book.model";
import { ILanguage } from "../../../entities/book/language.model";

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
  languages: ILanguage[] = [];
  isbnForm = new FormGroup({
    isbn: new FormControl("", { validators: [Validators.required, Validators.minLength(13), Validators.maxLength(13)] })
  });
  editForm = new FormGroup({
    title: new FormControl("", { validators: [Validators.required, Validators.maxLength(100)] }),
    subtitle: new FormControl("", { validators: [Validators.maxLength(200)] }),
    description: new FormControl("", { validators: [Validators.required] }),
    publisher: new FormControl("", { validators: [Validators.required] }),
    publicationYear: new FormControl("", {
      validators: [Validators.required, Validators.pattern("^[0-9]*$")]
    }),
    pages: new FormControl("", { validators: [Validators.required, Validators.pattern("^[0-9]*$")]})
  });

  @ViewChild("isbnInput") isbnInput!: ElementRef<HTMLInputElement>;

  constructor(private bookItemService: BookItemService, private bookService: BookService, private route: ActivatedRoute) {}

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
        pages: book?.pages?.toString() ?? ""
      }
    );
    this.coverImage = book?.cover ?? "";
    this.authors = book?.authors ?? [];
    this.languages = book?.languages ?? [];
  }

  private reset() {
    this.coverImage = "";
    this.editForm.reset();
    this.authors = [];
    this.languages = [];
  }

  submit() {
    console.warn(this.authors)
    console.warn(this.languages)
  }
}
