import { Injectable } from '@angular/core';
import { MatSnackBar } from '@angular/material/snack-bar';

@Injectable({
  providedIn: 'root'
})
export class NotificationService {

  constructor(private snackBar: MatSnackBar) { }

  showNotification(message: string, action: string = 'Fermer'): void {
    this.snackBar.open(message, action, {
      duration: 3000, // Durée pendant laquelle la notification reste visible (en millisecondes)
      horizontalPosition: 'center', // Position horizontale de la notification ('start', 'center', 'end')
      verticalPosition: 'bottom' // Position verticale de la notification ('top', 'bottom')
    });
  }
}
