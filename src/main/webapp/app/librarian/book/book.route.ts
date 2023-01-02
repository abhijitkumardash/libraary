import {Routes } from "@angular/router";
import {BookComponent} from "./list/book.component";
import {BookDetailComponent} from "./detail/book-detail.component";


export const bookRoute: Routes = [
  {
    path: '',
    component: BookComponent,
    data: {
      defaultSort: 'id,asc',
    },
  }
];
