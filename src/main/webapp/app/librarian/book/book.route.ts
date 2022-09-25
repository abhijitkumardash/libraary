
import { Routes } from "@angular/router";
import {BookComponent} from "./list/book.component";

export const bookRoute: Routes = [
  {
    path: '',
    component: BookComponent,
    data: {
      defaultSort: 'id,asc',
    },
  },
];
