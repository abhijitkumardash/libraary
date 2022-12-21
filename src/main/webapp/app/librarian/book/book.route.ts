import { Injectable } from "@angular/core";
import {ActivatedRouteSnapshot, Resolve, Routes } from "@angular/router";
import {BookComponent} from "./list/book.component";
import {BookItem, IBookItem} from "../../entities/book-item/bookitem.model";
import {BookItemService} from "../../entities/book-item/bookitem.service";
import { Observable, of } from "rxjs";
import {BookDetailComponent} from "./detail/book-detail.component";

@Injectable({ providedIn: 'root' })
export class BookItemResolve implements Resolve<IBookItem> {
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
    path: ':id/view',
    component: BookDetailComponent,
    resolve: {
      book: BookItemResolve
    }
  }
];
