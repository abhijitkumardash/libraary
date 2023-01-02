import {Component, OnInit, Input} from '@angular/core';
import { DomSanitizer } from '@angular/platform-browser';
import {ActivatedRoute} from '@angular/router';
import {BookItem} from '../../../entities/book-item/bookitem.model';
import {BookItemService} from "../../../entities/book-item/bookitem.service";
import '../../../extension/string'

@Component({
  selector: 'book-detail',
  templateUrl: './book-detail.component.html',
  styleUrls: ['./book-detail.component.scss'],
})
export class BookDetailComponent implements OnInit {
  @Input() book: BookItem | null = null;

  id: string | null = null;
  isReadMore = true;

  constructor(private route: ActivatedRoute, private bookService: BookItemService,
              protected sanitizer: DomSanitizer) {}

  ngOnInit(): void {
    this.loadData();
  }

  private loadData() {
    this.id = this.route.snapshot.queryParamMap.get('id');
    if (this.id) {
      this.bookService.find(this.id).subscribe(book => {
        this.book = book;
      });
    }
  }

  showText() {
    this.isReadMore = !this.isReadMore
  }
}
