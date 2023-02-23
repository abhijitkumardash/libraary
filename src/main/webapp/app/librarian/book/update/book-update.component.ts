import { Component, ElementRef, OnInit, ViewChild } from "@angular/core";
import { FormControl, FormGroup, Validators } from "@angular/forms";
import { ActivatedRoute } from "@angular/router";
import { BookItemService } from "../../../entities/book-item/bookitem.service";
import { BookService } from "../../../entities/book/book.service";
import { IAuthor } from "../../../entities/book/author.model";
import { IBook } from "../../../entities/book/book.model";
import { ILanguage } from "../../../entities/book/language.model";
import { FormatType, IBookItem } from "../../../entities/book-item/bookitem.model";
import { MultiCategory } from "../../../entities/book/multi-category.model";
import { ISubCategory } from "../../../entities/book/subcategory.model";
import { MatDialogService } from "../../../shared/dialog/mat-dialog.service";
import { CategoryUpdateComponent } from "../category/update/category-update.component";


@Component({
  selector: "book-update",
  templateUrl: "./book-update.component.html",
  styleUrls: ["./book-update.component.scss"]
})
export class BookUpdateComponent implements OnInit {
  id: string | null = null;
  bookId: number | null = null;
  isSaving = false;
  isLoading = false;
  coverImage: string = "";
  authors: IAuthor[] = [];
  languages: ILanguage[] = [];
  categories: MultiCategory[] = [];
  allSubcategories: ISubCategory[] = [];
  formats = Object.values(FormatType);
  isbnForm = new FormGroup({
    isbn: new FormControl("", { validators: [Validators.required, Validators.minLength(13), Validators.maxLength(13)] })
  });
  editForm = new FormGroup({
    title: new FormControl("", { validators: [Validators.required, Validators.maxLength(100)] }),
    subtitle: new FormControl("", { validators: [Validators.maxLength(200)] }),
    isbn: new FormControl({ value: "", disabled: true }),
    barcode: new FormControl(""),
    label: new FormControl(""),
    description: new FormControl("", { validators: [Validators.required] }),
    publisher: new FormControl("", { validators: [Validators.required] }),
    publicationYear: new FormControl("", {
      validators: [Validators.required, Validators.pattern("^[0-9]*$")]
    }),
    pages: new FormControl("", { validators: [Validators.required, Validators.pattern("^[0-9]*$")]}),
    price: new FormControl("", { validators: [Validators.pattern("^\\d+([,.]\\d{1,2})?$")]}),
    dateOfPurchase: new FormControl(""),
    format: new FormControl("", { validators: [Validators.required] }),
    referenceOnly: new FormControl(false),
    subcategory: new FormControl(),
  });

  @ViewChild("isbnInput") isbnInput!: ElementRef<HTMLInputElement>;

  constructor(private bookItemService: BookItemService, private bookService: BookService, private route: ActivatedRoute, private dialogService: MatDialogService) {}

  previousState(): void {
    window.history.back();
  }

  ngOnInit(): void {
    this.loadCategories();
    this.route.data.subscribe(({ bookItem }) => {
      if (bookItem) {
        this.id = bookItem.id;
        this.loadBookItemToForm(bookItem);
      } else (
        this.editForm.disable()
      );
    });
  }

  loadCategories() {
    this.bookService.queryCategories().subscribe(
      (res) => {
        this.categories = res.body?.map(cat => new MultiCategory(cat.id!, cat.name, [])) ?? [];
        this.categories.forEach((cat, index, array) => this.bookService.querySubCategoriesByCategory(cat.id).subscribe(
          (res) => {
            cat.subcategories = res.body ?? []
            this.allSubcategories.push(...res.body ?? [])
            array[index] = cat
          }
        ));
      }
    )
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
            this.editForm.get("isbn")?.setValue(this.isbnForm.get("isbn")?.value ?? "");
            this.isLoading = false;
          }
        );
      }
    );
    this.editForm.enable();
  }

  loadBookItemToForm(bookItem: IBookItem | null) {
    this.loadBookToForm(bookItem?.book ?? null);
    this.editForm.patchValue(
      {
        price: bookItem?.price?.toString() ?? "",
        dateOfPurchase: bookItem?.dateOfPurchase?.toString() ?? "",
        format: bookItem?.format ?? "",
        referenceOnly: bookItem?.referenceOnly ?? false,
        barcode: bookItem?.barcode ?? "",
        label: bookItem?.label ?? "",
      }
    );
  }

  loadBookToForm(book: IBook | null) {
    this.bookId = book?.id ?? null;
    this.editForm.patchValue(
      {
        title: book?.title ?? "",
        subtitle: book?.subtitle ?? "",
        isbn: book?.isbn ?? "",
        description: book?.description ?? "",
        publisher: book?.publisher ?? "",
        publicationYear: book?.publicationYear ?? "",
        pages: book?.pages?.toString() ?? "",
        subcategory: book?.subCategories?.map(cat => cat.id)
      }
    );
    this.coverImage = book?.cover ?? "";
    this.authors = book?.authors ?? [];
    this.languages = book?.languages ?? [];
  }

  submit() {
    if(this.id === null) {
      this.create();
    } else {
      this.update();
    }
  }

  addNewCategory() {
    const dialog = this.dialogService.openDialog(CategoryUpdateComponent, {})
    dialog.closed?.subscribe(reason => {
      if(reason === 'created') {
        this.loadCategories()
      }
    });
  }

  private reset() {
    this.coverImage = "";
    this.editForm.reset();
    this.authors = [];
    this.languages = [];
  }

  private create() {
    this.isSaving = true;
    let formValue = this.editForm.value;
    this.bookService.create({
      cover: this.coverImage,
      title: formValue.title!,
      pages: Number(formValue.pages!),
      subtitle: formValue.subtitle ?? undefined,
      description: formValue.description ?? undefined,
      authors: this.authors,
      languages: this.languages,
      isbn: formValue.isbn!,
      publisher: formValue.publisher ?? undefined,
      publicationYear: formValue.publicationYear ?? undefined,
      subCategories: this.allSubcategories.filter(cat => formValue.subcategory?.includes(cat.id)),
    }).subscribe(
      res => {
        this.bookItemService.create({
          book: res.body ?? undefined,
          dateOfPurchase: formValue.dateOfPurchase ? new Date(formValue.dateOfPurchase) : undefined,
          format: formValue.format as FormatType,
          price: Number(formValue.price!),
          referenceOnly: formValue.referenceOnly ?? undefined,
          barcode: formValue.barcode ?? undefined,
          label: formValue.label ?? undefined,
        }).subscribe(
          () => {
            this.isSaving = false;
            this.previousState();
          },
          () => {
            this.isSaving = false;
          }
        )
      },
      () => {
        this.isSaving = false;
      }
    )
  }

  private update() {
    this.isSaving = true;
    let formValue = this.editForm.value;
    this.bookService.update({
      cover: this.coverImage,
      title: formValue.title!,
      pages: Number(formValue.pages!),
      subtitle: formValue.subtitle ?? undefined,
      description: formValue.description ?? undefined,
      authors: this.authors,
      languages: this.languages,
      isbn: this.editForm.get("isbn")?.value ?? "",
      publisher: formValue.publisher ?? undefined,
      publicationYear: formValue.publicationYear ?? undefined,
      subCategories: this.allSubcategories.filter(cat => formValue.subcategory?.includes(cat.id)),
      id: this.bookId ?? undefined,
    }).subscribe(
      res => {
        this.bookItemService.update({
          id: this.id ?? undefined,
          book: res.body ?? undefined,
          dateOfPurchase: formValue.dateOfPurchase ? new Date(formValue.dateOfPurchase) : undefined,
          format: formValue.format as FormatType,
          price: Number(formValue.price!.replace(",", ".")),
          referenceOnly: formValue.referenceOnly ?? undefined,
          barcode: formValue.barcode ?? undefined,
          label: formValue.label ?? undefined,
        }).subscribe(
          () => {
            this.isSaving = false;
            this.previousState();
          },
          () => {
            this.isSaving = false;
          }
        )
      }
    )
  }
}
