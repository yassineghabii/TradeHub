import { Component, OnInit } from '@angular/core';
import { ChtibaService } from "../../services/chtiba.service";
import { MatDialog } from "@angular/material/dialog";

@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.scss']
})
export class ProfileComponent implements OnInit {
  adminInfo: any;
  showPassword: boolean = false;
  maskedPassword: string;
  newPassword: string = ''; // Nouveau mot de passe
  passwordUpdated: boolean = false; // Variable pour gérer l'affichage du message de succès

  constructor(private chtibaService: ChtibaService) {}

  getUserId(): number | null {
    const userIdStr = sessionStorage.getItem('id');
    return userIdStr ? Number(userIdStr) : null;
  }

  ngOnInit(): void {
    const userId = this.getUserId();
    this.chtibaService.getUserById(userId).subscribe((data: any) => {
      this.adminInfo = {
        lastname: data.lastname,
        firstname: data.firstname,
        cin: data.cin,
        phone_number: data.phone_number,
        address: data.address,
        email: data.email,
        id_admin: data.id_admin,
        pwd_user: data.pwd_user
      };

      // Masquez le mot de passe avec des astérisques
      this.maskedPassword = '********';
    });
  }

  // Fonction pour mettre à jour le mot de passe
  updatePassword(): void {
    const userId = this.getUserId();
    if (this.newPassword) {
      this.chtibaService.updateUserById(userId, { pwd_user: this.newPassword }).subscribe(
          (data: any) => {
            // Mise à jour réussie
            this.adminInfo.pwd_user = '********';
            this.newPassword = '';
            this.passwordUpdated = true; // Succès
          },
          (error: any) => {
            // Gestion de l'erreur
            console.error('Erreur lors de la mise à jour du mot de passe :', error);
            this.passwordUpdated = false; // Échec
          }
      );
    }
  }

}
