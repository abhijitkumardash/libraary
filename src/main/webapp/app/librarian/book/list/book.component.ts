import {Component, OnInit, ViewChild} from '@angular/core';
import {MatPaginator} from '@angular/material/paginator';
import {MatSort} from '@angular/material/sort';
import {BookItemService} from '../../../entities/book-item/bookitem.service';
import {DomSanitizer} from '@angular/platform-browser';
import {IBookItem} from '../../../entities/book-item/bookitem.model';
import {MatTableDataSource} from '@angular/material/table';
import {ActivatedRoute, Params, Router } from '@angular/router';
import { MatDrawer } from '@angular/material/sidenav';


@Component({
  selector: 'book-list',
  templateUrl: './book.component.html',
  styleUrls: ['./book.component.scss'],
})
export class BookComponent implements OnInit {
  displayedColumns: string[] = ['cover', 'title', 'author', 'isbn', 'year', 'pages', 'format', 'status'];
  dataSource: MatTableDataSource<IBookItem> = new MatTableDataSource<IBookItem>([]);
  selectedBook: IBookItem | null = null;
  @ViewChild(MatPaginator) paginator!: MatPaginator;
  @ViewChild(MatSort) sort!: MatSort;
  @ViewChild('drawer') drawer!: MatDrawer;
  constructor(private bookItemService: BookItemService,
              protected sanitizer: DomSanitizer,
              private router: Router,
              private activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.bookItemService.query().subscribe((result) => {
      this.dataSource = new MatTableDataSource<IBookItem>(result.body ? result.body : [])
    })
  }

  ngAfterViewInit() {
    this.dataSource.paginator = this.paginator;
    this.dataSource.sort = this.sort;
    if (this.activatedRoute.snapshot.queryParamMap.get('id') !== null) {
      this.drawer.toggle();
    }
  }

  applyFilter(event: Event) {
    const filterValue = (event.target as HTMLInputElement).value;
    this.dataSource.filter = filterValue.trim().toLowerCase();

    if (this.dataSource.paginator) {
      this.dataSource.paginator.firstPage();
    }
  }

  detail(bookItem: IBookItem): void {
    if (!this.drawer.opened) {
      this.router.navigate(
        [],
        {
          relativeTo: this.activatedRoute,
          queryParams: {id: bookItem.id},
          queryParamsHandling: 'merge',
        });
      this.selectedBook = bookItem;
      this.drawer.toggle();
    } else {
      this.closeDrawer();
    }
  }

  closeDrawer() {
    this.drawer.close();
    this.router.navigate([], {
      queryParams: {
        'id': null,
      },
      queryParamsHandling: 'merge'
    })
  }
}
