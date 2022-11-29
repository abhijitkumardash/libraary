import { NgModule } from '@angular/core';
import {SharedModule} from "../../shared/shared.module";
import {BookItemDeleteDialogComponent} from "./delete/book-item-delete-dialog.component";

@NgModule({
  imports: [SharedModule],
  declarations: [
    BookItemDeleteDialogComponent,
  ],
})
export class BookItemModule {}
