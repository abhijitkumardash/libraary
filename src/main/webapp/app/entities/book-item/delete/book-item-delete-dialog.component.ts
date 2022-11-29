import { Component, Inject } from "@angular/core";
import { MAT_DIALOG_DATA } from "@angular/material/dialog";
import { BookItem } from "../bookitem.model";

@Component({
  selector: 'jhi-book-item-delete-dialog',
  templateUrl: './book-item-delete-dialog.component.html',
})
export class BookItemDeleteDialogComponent {
  constructor(@Inject(MAT_DIALOG_DATA) public bookItem: BookItem) {}
}
