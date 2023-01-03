import {Component, OnInit, ViewChild} from '@angular/core';
import {BookItemService} from '../../../entities/book-item/bookitem.service';
import {DomSanitizer} from '@angular/platform-browser';
import {IBookItem} from '../../../entities/book-item/bookitem.model';
import {ActivatedRoute, Router } from '@angular/router';
import { MatDrawer } from '@angular/material/sidenav';
import {ASC, DESC} from "../../../config/navigation.constants";
import {SortableComponent} from "../../../shared/sort/component";
import { SortDirection } from '@angular/material/sort';


@Component({
  selector: 'book-list',
  templateUrl: './book.component.html',
  styleUrls: ['./book.component.scss'],
})
export class BookComponent extends SortableComponent<IBookItem> implements OnInit {
  displayedColumns: string[] = ['cover', 'title', 'author', 'isbn', 'year', 'pages', 'format', 'status'];
  selectedBook: IBookItem | null = null;
  @ViewChild('drawer') drawer!: MatDrawer;
  itemsPerPage = 5;
  defaultSortColumn = "status";
  defaultSortDirection = "asc" as SortDirection;
  constructor(private bookItemService: BookItemService,
              protected sanitizer: DomSanitizer,
              protected router: Router,
              protected activatedRoute: ActivatedRoute)
  {
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
  }

  applyFilter(event: Event) {
    const filterValue = (event.target as HTMLInputElement).value;
    this.data.filter = filterValue.trim().toLowerCase();

    if (this.data.paginator) {
      this.data.paginator.firstPage();
    }
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
            sort: `${this.predicate},${this.ascending ? ASC : DESC}`,
          },
          queryParamsHandling: 'merge',
        });
      this.selectedBook = bookItem;
      this.drawer.open();
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
