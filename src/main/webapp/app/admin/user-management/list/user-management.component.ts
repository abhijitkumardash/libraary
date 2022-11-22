import {AfterViewInit, Component, OnInit, ViewChild } from '@angular/core';
import { HttpResponse, HttpHeaders } from '@angular/common/http';
import { ActivatedRoute, Router } from '@angular/router';
import { combineLatest } from 'rxjs';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { ASC, DESC, SORT } from 'app/config/navigation.constants';
import { AccountService } from 'app/core/auth/account.service';
import { Account } from 'app/core/auth/account.model';
import { UserManagementService } from '../service/user-management.service';
import { User } from '../user-management.model';
import { UserManagementDeleteDialogComponent } from '../delete/user-management-delete-dialog.component';
import { MatTableDataSource } from '@angular/material/table';
import { MatPaginator, PageEvent } from '@angular/material/paginator';
import { MatSort } from '@angular/material/sort';
import {MatDialogService} from "../../../shared/dialog/mat-dialog.service";

@Component({
  selector: 'jhi-user-mgmt',
  templateUrl: './user-management.component.html',
})
export class UserManagementComponent implements OnInit, AfterViewInit {
  currentAccount: Account | null = null;
  users: MatTableDataSource<User> = new MatTableDataSource<User>([]);
  displayedColumns: string[] = ['firstName', 'lastName', 'email', 'activated', "authorities", "actions"];
  isLoading = false;
  totalItems = 0;
  itemsPerPage = 5;
  page!: number;
  predicate!: string;
  ascending!: boolean;
  defaultSortColumn: string = "email";
  defaultSortOrder: string = "asc";

  @ViewChild(MatPaginator) paginator!: MatPaginator;
  @ViewChild(MatSort) sort!: MatSort;

  constructor(
    private userService: UserManagementService,
    private accountService: AccountService,
    private activatedRoute: ActivatedRoute,
    private router: Router,
    private dialogService: MatDialogService
  ) {}

  ngOnInit(): void {
    this.accountService.identity().subscribe(account => (this.currentAccount = account));
    this.handleNavigation();
  }

  ngAfterViewInit() {
    this.users.paginator = this.paginator;
    this.users.sort = this.sort;
  }

  setActive(user: User, isActivated: boolean): void {
    this.userService.update({ ...user, activated: isActivated }).subscribe(() => this.loadAll());
  }

  trackIdentity(_index: number, item: User): number {
    return item.id!;
  }

  deleteUser(user: User): void {
    const dialog = this.dialogService.openDialog(UserManagementDeleteDialogComponent, {
      data: user
    })
    dialog.closed?.subscribe(reason => {
      if (reason === 'deleted') {
        this.userService.delete(user.login!).subscribe(() => {
          this.loadAll();
        });
      }
    });
  }

  loadAll(): void {
    this.isLoading = true;
    this.userService
      .query({
        page: this.page,
        size: this.itemsPerPage,
        sort: this.sortData(),
      })
      .subscribe({
        next: (res: HttpResponse<User[]>) => {
          this.isLoading = false;
          this.onSuccess(res.body ? res.body : [], res.headers);
        },
        error: () => (this.isLoading = false),
      });
  }

  getData(event: PageEvent) {
    this.page = event.pageIndex;
    this.itemsPerPage = event.pageSize;
    this.loadAll();
  }

  transition(): void {
    this.router.navigate(['./'], {
      relativeTo: this.activatedRoute.parent,
      queryParams: {
        page: this.page,
        sort: `${this.predicate},${this.ascending ? ASC : DESC}`,
      },
    });
  }

  private handleNavigation(): void {
    combineLatest([this.activatedRoute.data, this.activatedRoute.queryParamMap]).subscribe(([data, params]) => {
      const page = params.get('page');
      this.page = +(page ?? 0);
      const sort = (params.get(SORT) ?? data['defaultSort']).split(',');
      this.predicate = sort[0];
      this.ascending = sort[1] === ASC;
      this.loadAll();
    });
  }

  private sortData(): string[] {
    const result = [`${this.sort ? this.sort.active : this.defaultSortColumn},${this.sort ? this.sort.direction : "ASC"}`];
    if (this.predicate !== 'id') {
      result.push('id');
    }
    return result;
  }

  private onSuccess(users: User[], headers: HttpHeaders): void {
    this.totalItems = Number(headers.get('X-Total-Count'));
    this.paginator.length = this.totalItems;
    this.paginator.pageIndex = this.page;
    this.users = new MatTableDataSource<User>(users);
    this.users.sort = this.sort;
  }
}
