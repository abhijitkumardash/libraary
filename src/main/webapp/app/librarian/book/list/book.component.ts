import { Component, OnInit } from '@angular/core';
import { Book } from '../../../entities/book/book.model';
import { BookService } from '../../../entities/book/book.service';

@Component({
  selector: 'jhi-book-list',
  templateUrl: './book.component.html',
})
export class BookComponent implements OnInit {
  displayedColumns: string[] = ['id', 'title', 'author', 'year', 'publisher', 'publicationYear', 'pages', 'isbn'];
  data: any | null = null;
  constructor(private bookService: BookService) { }

  ngOnInit(): void {
    this.bookService.query().subscribe((result) => {
      this.data = result.body
      console.log(this.data)
    })
  }
  
  
}
