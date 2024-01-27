import { Injectable } from '@angular/core';
import { ToastrService } from 'ngx-toastr';

@Injectable({
  providedIn: 'root'
})
export class NotificationService {

  constructor(private toastr: ToastrService) {}

  showSuccess(message: string): void {
    this.toastr.success(message, 'Succès', {
      timeOut: 3000, // Durée pendant laquelle la notification est affichée (en millisecondes)
      progressBar: true, // Afficher une barre de progression dans la notification
      positionClass: 'toast-top-right' // Position de la notification (en haut à droite)
    });
  }

  showError(message: string): void {
    this.toastr.error(message, 'Erreur', {
      timeOut: 3000,
      progressBar: true,
      positionClass: 'toast-top-right'
    });
  }

  showWarning(message: string): void {
    this.toastr.warning(message, 'Attention', {
      timeOut: 3000,
      progressBar: true,
      positionClass: 'toast-top-right'
    });
  }
}
