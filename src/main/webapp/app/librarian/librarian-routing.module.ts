import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'books',
        loadChildren: () => import('./book/book.module').then(m => m.BookModule),
        data: {
          pageTitle: 'book.home.title',
        },
      }
    ]),
  ],
})
export class LibrarianRoutingModule {}
