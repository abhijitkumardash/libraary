import { Component, OnInit } from '@angular/core';
import { BookService } from '../../../entities/book/book.service';
import {ViewChild} from '@angular/core';
import {MatPaginator} from '@angular/material/paginator';
import {MatSort} from '@angular/material/sort';
import { BookItemService } from '../../../entities/book-item/bookitem.service';
import { DomSanitizer } from '@angular/platform-browser';
import { IBookItem } from '../../../entities/book-item/bookitem.model';
import { MatTableDataSource } from '@angular/material/table';


@Component({
  selector: 'jhi-book-list',
  templateUrl: './book.component.html',
})
export class BookComponent implements OnInit {
  displayedColumns: string[] = ['cover', 'title', 'author', 'isbn', 'year', 'pages', 'format', 'status'];
  dataSource: MatTableDataSource<IBookItem> = new MatTableDataSource<IBookItem>([]);
  @ViewChild(MatPaginator) paginator: MatPaginator;
  @ViewChild(MatSort) sort: MatSort;

  constructor(private bookItemService: BookItemService, protected sanitizer: DomSanitizer) {  }

  ngOnInit(): void {
    this.bookItemService.query().subscribe((result) => {
      this.dataSource = new MatTableDataSource<IBookItem>(result.body ? result.body : [])
      
    })
  }

  ngAfterViewInit() {
    this.dataSource.paginator = this.paginator;
    this.dataSource.sort = this.sort;
  }

  applyFilter(event: Event) {
    const filterValue = (event.target as HTMLInputElement).value;
    this.dataSource.filter = filterValue.trim().toLowerCase();

    if (this.dataSource.paginator) {
      this.dataSource.paginator.firstPage();
    }
  }


}
