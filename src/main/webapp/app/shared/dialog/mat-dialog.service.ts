import { Component, Injectable } from '@angular/core';

import { User } from '../user-management.model';
import { UserManagementService } from '../service/user-management.service';
import { MatDialog } from '@angular/material/dialog';
import { UserManagementDeleteDialogComponent } from './user-management-delete-dialog.component';

@Injectable({ providedIn: 'root' })
export class UserManagementDeleteDialogService {
  user?: User;

  constructor(private userService: UserManagementService, public dialog: MatDialog) {}

  openDialog() {
    const dialogRef = this.dialog.open(UserManagementDeleteDialogComponent);

    dialogRef.afterClosed().subscribe(result => {
      this.confirmDelete(result);
    });
  }

  confirmDelete(result: boolean): void {
    if (result) {
      this.userService.delete(this.user?.login!).subscribe(() => {});
    }
  }
}
