import { Component, Inject } from "@angular/core";
import { MAT_DIALOG_DATA } from "@angular/material/dialog";
import {IBookItem} from "../../../entities/book-item/bookitem.model";

@Component({
  selector: 'book-delete-dialog',
  templateUrl: './book-delete-dialog.component.html',
})
export class BookDeleteDialogComponent {
  constructor(@Inject(MAT_DIALOG_DATA) public bookItem: IBookItem) {}
}
