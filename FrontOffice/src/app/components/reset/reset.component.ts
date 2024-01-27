import { Component } from '@angular/core';
import { AuthService } from '../../services/authservices';

@Component({
  selector: 'app-reset',
  templateUrl: './reset.component.html',
  styleUrls: ['./reset.component.css']
})
export class ResetComponent {
  token: string;
  newPassword: string;
  message: string;

  constructor(private authService: AuthService) { }

  onSubmit() {
    this.authService.resetPassword(this.token, this.newPassword).subscribe(
      response => {
        this.message = "Réinitialisation du mot de passe réussie! vous pouvez se connecter a nouvaeu"; // Success message
      },
      error => {
        this.message = "Une erreur s'est produite. Veuillez réessayer."; // Error message
      }
    );
  }
}
