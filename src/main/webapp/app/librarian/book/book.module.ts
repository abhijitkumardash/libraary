import {NgModule} from '@angular/core';
import {RouterModule} from '@angular/router';

import {SharedModule} from 'app/shared/shared.module';
import {bookRoute} from "./book.route";
import {BookComponent} from "./list/book.component";

@NgModule({
  imports: [SharedModule, RouterModule.forChild(bookRoute)],
  declarations: [
    BookComponent,
  ],
})
export class BookModule {
}
