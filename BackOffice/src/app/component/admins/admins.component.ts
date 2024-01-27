import { Component, OnInit } from '@angular/core';
import { ChtibaService } from '../../services/chtiba.service';
import { ConfirmModalComponent } from '../confirm-modal/confirm-modal.component';
import { MatDialog } from "@angular/material/dialog";
import { Client } from "../../Models/Client";

@Component({
  selector: 'app-admins',
  templateUrl: './admins.component.html',
  styleUrls: ['./admins.component.scss']
})
export class AdminsComponent implements OnInit {
  listAdmins: any[] = [];
  filteredAdmins: any[] = [];
  searchStr: string = '';
  newAdmin: Client = new Client();
  selectedAdmin: Client;

  constructor(private chtibaService: ChtibaService, public dialog: MatDialog) {
  }
  getUserId(): number | null {
    const userIdStr = sessionStorage.getItem('id');
    return userIdStr ? Number(userIdStr) : null;
  }

  ngOnInit(): void {
    const userId = this.getUserId();
    this.getAllAdmins(userId);
  }

  getAllAdmins(userId: number) {
    this.chtibaService.getAllAdmins(userId).subscribe(admins => {
      this.listAdmins = admins;
      this.filteredAdmins = admins;
    });
  }

  searchAdmins() {
    if (this.searchStr) {
      this.filteredAdmins = this.listAdmins.filter(admin =>
          (admin.firstname + ' ' + admin.lastname).toLowerCase().includes(this.searchStr.toLowerCase())
      );
    } else {
      this.filteredAdmins = this.listAdmins;
    }
  }

  openUpdateModal(admin: Client) {
    const dialogRef = this.dialog.open(ConfirmModalComponent, {
      width: '400px',
      data: {
        action: 'mettre à jour',
        client: `${admin.firstname} ${admin.lastname}`,
        mode: 'update',
        admin: admin  // On passe l'objet admin au dialogue
      }
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        // Vous avez les modifications dans result
        this.chtibaService.updateUserById(admin.id, result).subscribe(updatedAdmin => {
          this.getAllAdmins(this.getUserId());
        });
      }
    });
  }

  updateAdmin() {
    this.chtibaService.updateUserById(Number(this.selectedAdmin.id), this.selectedAdmin).subscribe(updatedAdmin => {
      this.getAllAdmins(this.getUserId());
    });
  }

  openAddModal() {
    const dialogRef = this.dialog.open(ConfirmModalComponent, {
      width: '400px',
      data: {
        action: 'ajouter',
        mode: 'add',
      }
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.chtibaService.addAdmin(result).subscribe(addedAdmin => {
          this.getAllAdmins(this.getUserId());
        });
      }
    });
  }

  addAdmin() {
    this.chtibaService.addAdmin(this.newAdmin).subscribe(addedAdmin => {
      this.getAllAdmins(this.getUserId());
    });
  }

  deleteAdmin(adminId: number) {
    const adminToDelete = this.filteredAdmins.find(admin => admin.id === adminId);
    const adminName = adminToDelete ? `${adminToDelete.firstname} ${adminToDelete.lastname}` : "";

    this.dialog.open(ConfirmModalComponent, {
      width: '400px',
      data: {
        action: 'supprimer',
        client: adminName,
        mode: 'delete'
      }
    }).afterClosed().subscribe(confirmResult => {
      if (confirmResult) {
        this.chtibaService.deleteUser(adminId).subscribe(() => {
          this.getAllAdmins(this.getUserId());
        });
      }
    });
  }
}
