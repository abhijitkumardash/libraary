import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {User} from '../user-management.model';
import {UserManagementDeleteDialogComponent} from "../delete/user-management-delete-dialog.component";
import {NgbModal} from '@ng-bootstrap/ng-bootstrap';
import {Account} from "../../../core/auth/account.model";
import {AccountService} from 'app/core/auth/account.service';

@Component({
  selector: 'jhi-user-mgmt-detail',
  templateUrl: './user-management-detail.component.html',
})
export class UserManagementDetailComponent implements OnInit {
  user: User | null = null;
  currentAccount: Account | null = null;

  constructor(private route: ActivatedRoute,
              private modalService: NgbModal,
              private router: Router,
              private accountService: AccountService) {}

  ngOnInit(): void {
    this.accountService.identity().subscribe(account => (this.currentAccount = account));
    this.route.data.subscribe(({user}) => {
      this.user = user;
    });
  }

  deleteUser(user: User): void {
    const modalRef = this.modalService.open(UserManagementDeleteDialogComponent, {size: 'lg', backdrop: 'static'});
    modalRef.componentInstance.user = user;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(() => {
      this.router.navigate(['../../'])
    });
  }
}
