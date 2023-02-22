import {NgModule} from '@angular/core';
import {RouterModule} from '@angular/router';

import {SharedModule} from 'app/shared/shared.module';
import {bookRoute} from "./book.route";
import {BookComponent} from "./list/book.component";
import { BookDetailComponent } from './detail/book-detail.component';
import {BookDeleteDialogComponent} from "./delete/book-delete-dialog.component";
import {BookUpdateComponent} from "./update/book-update.component";
import { CategoryUpdateComponent } from "./category/update/category-update.component";

@NgModule({
  imports: [SharedModule, RouterModule.forChild(bookRoute)],
  declarations: [
    BookComponent,
    BookDetailComponent,
    BookDeleteDialogComponent,
    BookUpdateComponent,
    CategoryUpdateComponent,
  ],
})
export class BookModule {
}
