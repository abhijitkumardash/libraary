import {Component, OnInit} from '@angular/core';
import {ActivatedRoute} from '@angular/router';
import {BookItem} from '../../../entities/book-item/bookitem.model';

@Component({
  selector: 'book-detail',
  templateUrl: './book-detail.component.html',
  styleUrls: ['./book-detail.component.scss'],
})
export class BookDetailComponent implements OnInit {
  book: BookItem | null = null;

  image = '';

  constructor(private route: ActivatedRoute) {
  }

  ngOnInit(): void {
    this.route.data.subscribe(({book}) => {
      this.book = book;
      this.image = 'data:image/png;base64,' + this.book?.book?.cover;
    });
  }
}
