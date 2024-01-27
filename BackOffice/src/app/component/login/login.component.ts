import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { AuthService } from '../../services/authservices';
import { Router } from "@angular/router";

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css'],
})
export class LoginComponent implements OnInit {
  loginForm: FormGroup;
  errorMessage: string;

  constructor(
    private formBuilder: FormBuilder,
    private authService: AuthService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.initializeLoginForm();
  }

  private initializeLoginForm(): void {
    this.loginForm = this.formBuilder.group({
      id_admin: ['', Validators.required],
      password: ['', Validators.required]
    });
  }

  getRole(): string | null {
    return this.cleanSessionStorageItem('role');
  }

  getName(): string | null {
    return this.cleanSessionStorageItem('full_name');
  }

  private cleanSessionStorageItem(itemName: string): string | null {
    const item = sessionStorage.getItem(itemName);
    return item ? item.replace(/"/g, '') : null;
  }

  onSubmit(): void {
    if (this.loginForm.valid) {
      // Ajoutez des journaux pour le mot de passe et l'id_admin ici
      this.authService.login(this.loginForm.value).subscribe(
          _ => this.handleLoginSuccess(),
          error => this.handleLoginError(error)
      );

    }
  }

  private handleLoginSuccess(): void {
    const role = this.getRole();
    this.navigateToRoleBasedRoute(role);
  }

  private handleLoginError(error: any): void {
    if (error.status === 401) {
      this.errorMessage = "Identifiants invalides. Veuillez réessayer.";
    } else {
      this.errorMessage = "Une erreur inattendue s'est produite. Veuillez réessayer ultérieurement.";
      console.error('Login error:', error);
    }
  }

  private navigateToRoleBasedRoute(role: string | null): void {
    console.log("Role de l'utilisateur:", role); // Ajoutez cette ligne
    switch(role) {
      case 'player':
        this.router.navigate(['/admins']);
        break;
      case 'admin':
        this.router.navigate(['/admins']);
        break;
      default:
        this.router.navigate(['/admins']);
    }
  }
}
