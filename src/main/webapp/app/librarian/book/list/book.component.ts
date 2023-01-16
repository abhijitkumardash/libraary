import {Component, OnInit, ViewChild} from '@angular/core';
import {BookItemService} from '../../../entities/book-item/bookitem.service';
import {DomSanitizer} from '@angular/platform-browser';
import {IBookItem} from '../../../entities/book-item/bookitem.model';
import {ActivatedRoute, Router} from '@angular/router';
import {MatDrawer} from '@angular/material/sidenav';
import {SORT} from "../../../config/navigation.constants";
import {SortableComponent} from "../../../shared/sort/component";
import {SortDirection} from '@angular/material/sort';
import {debounceTime, distinctUntilChanged, Observable, Subject} from 'rxjs';
import {HttpResponse} from '@angular/common/http';


@Component({
  selector: 'book-list',
  templateUrl: './book.component.html',
  styleUrls: ['./book.component.scss'],
})
export class BookComponent extends SortableComponent<IBookItem> implements OnInit {
  displayedColumns: string[] = ['cover', 'book_title', 'book_authors_name', 'book_isbn',
    'book_publicationYear', 'book_pages', 'format', 'status', 'actions'];
  selectedBook: IBookItem | null = null;
  @ViewChild('drawer') drawer!: MatDrawer;
  itemsPerPage = 5;
  defaultSortColumn = "book_title";
  defaultSortDirection = "asc" as SortDirection;
  search = "";
  debounceSearch: Subject<any> = new Subject<any>();

  constructor(private bookItemService: BookItemService,
              protected sanitizer: DomSanitizer,
              protected router: Router,
              protected activatedRoute: ActivatedRoute) {
    super(activatedRoute, router, bookItemService);
  }

  ngOnInit(): void {
    this.handleNavigation();
  }

  ngAfterViewInit() {
    this.data.paginator = this.paginator;
    this.data.sort = this.sort;
    if (this.activatedRoute.snapshot.queryParamMap.get('id') !== null) {
      this.drawer.toggle();
    }
    this.drawer.closedStart.subscribe({
      next: () => {
        this.closeDrawer()
      }
    })
  }

  onFilterChanged(filter: string): void {
    if (this.debounceSearch.observers.length === 0) {
      this.debounceSearch.pipe(debounceTime(1000), distinctUntilChanged()).subscribe(() => {
        this.reset();
      });
    }
    this.debounceSearch.next(filter);
  }

  detail(bookItem: IBookItem): void {
    if (!this.drawer.opened || bookItem.id !== this.selectedBook?.id) {
      this.router.navigate(
        [],
        {
          relativeTo: this.activatedRoute,
          queryParams: {
            id: bookItem.id,
            page: this.page,
            sort: `${this.predicate},${this.activatedRoute.snapshot.queryParamMap.get(SORT)?.split(",")[1]}`,
          },
          queryParamsHandling: 'merge',
        });
      this.selectedBook = bookItem;
      this.drawer.open();
    } else {
      this.closeDrawer();
    }
  }

  reset(): void {
    this.page = 0;
    this.loadAll();
  }

  protected query(): Observable<HttpResponse<IBookItem[]>> {
    return this.service
      .query({
        page: this.page,
        size: this.itemsPerPage,
        sort: this.sortData(),
        title: this.search,
      })
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
