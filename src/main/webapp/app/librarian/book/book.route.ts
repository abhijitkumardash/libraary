import {ActivatedRouteSnapshot, Resolve, Routes } from "@angular/router";
import {BookComponent} from "./list/book.component";
import {BookUpdateComponent} from "./update/book-update.component";
import {BookItem, IBookItem} from "../../entities/book-item/bookitem.model";
import {BookItemService} from "../../entities/book-item/bookitem.service";
import { Observable, of } from "rxjs";
import { Injectable } from "@angular/core";


@Injectable({ providedIn: 'root' })
export class BookResolve implements Resolve<IBookItem> {
  constructor(private service: BookItemService) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IBookItem> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id);
    }
    return of(new BookItem());
  }
}

export const bookRoute: Routes = [
  {
    path: '',
    component: BookComponent,
    data: {
      defaultSort: 'id,asc',
    },
  },
  {
    path: 'new',
    component: BookUpdateComponent
  },
  {
    path: ':id/edit',
    component: BookUpdateComponent,
    resolve: {
      bookItem: BookResolve,
    },
  },
];
