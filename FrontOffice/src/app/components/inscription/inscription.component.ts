import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { AuthService } from '../../services/authservices';
import {Router} from "@angular/router"; // Ensure the path is correct
import { SocialAuthService, SocialUser } from "@abacritt/angularx-social-login";
import {RegisterRequest} from "../../entities/registerRequest";
import {WalletEnum} from "../../entities/Wallet";
import {MatSelectChange} from "@angular/material/select";
import {NotificationService} from "../wallet/NotificationService";

@Component({
  selector: 'app-inscription',
  templateUrl: './inscription.component.html',
  styleUrls: ['./inscription.component.css']
})
export class InscriptionComponent implements OnInit {
  user: SocialUser;
  loggedIn: boolean;
  private authStateSubscription: any;
  profileImage: File = null;
  imagePreview: string;
  message: string = ''; // Ajoutez cette ligne

  registrationForm: FormGroup;
  errorMessage: string;
  static readonly emailPattern = /^[a-zA-Z0-9._-]+\.+[a-zA-Z0-9._-]+@esprit\.tn$/;
  walletTypes = Object.keys(WalletEnum);

  walletEnumKeys() {
    return this.walletTypes.filter(value => isNaN(Number(value)));
  }

  constructor(private router: Router,private formBuilder: FormBuilder, private authService: AuthService ,private socialAuthService: SocialAuthService,private notificationService: NotificationService // Ajouter cette ligne
  ) { }

  ngOnInit() {
    this.initiateGoogleAuth();

    this.registrationForm = this.formBuilder.group({
      email: ['', [Validators.required, Validators.email, Validators.pattern(/^[a-zA-Z0-9._-]+@(?:gmail\.com|gmail\.tn|esen\.tn|esprit\.tn)$/)]],
      pwd_user: ['', [Validators.required, this.validatePassword]],
      confirmPassword: ['', Validators.required],
      firstname: ['', Validators.required],
      lastname: ['', Validators.required],
      cin: ['', [Validators.required, Validators.pattern(/^\d{8}$/)]], // Exactly 8 digits
      address: ['', Validators.required],
      profileImage: [null, Validators.required],
      phonenumber: ['', [Validators.required, Validators.pattern(/^(2[0-9]|5[0-9]|7[0-9])\d{6}$/)]], // Start with specified prefixes and exactly 8 digits
      type: ['', Validators.required],

    },
      {
        validator: this.passwordMatchValidator // Validator pour vérifier si les mots de passe correspondent
      });
  }

  onWalletTypeChange(event: MatSelectChange) {
    this.registrationForm.patchValue({ type: event.value });
  }
  private validatePassword(control) {
    const passwordRegex = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[@$!%*?&])[A-Za-z\d@$!%*?&]{8,}$/;
    if (control.value && !passwordRegex.test(control.value)) {
      return { invalidPassword: true };
    }
    return null;
  }
  passwordMatchValidator(formGroup: FormGroup) {
    const password = formGroup.get('pwd_user').value;
    const confirmPassword = formGroup.get('confirmPassword').value;

    if (password !== confirmPassword) {
      formGroup.get('confirmPassword').setErrors({ passwordMismatch: true });
    } else {
      formGroup.get('confirmPassword').setErrors(null);
    }
  }

  onSubmit() {
    if (this.registrationForm.valid) {
      const confirmationMessage = 'Êtes-vous sûr des données saisies ?'; // Message de confirmation

      const confirmed = confirm(confirmationMessage); // Affichage de la confirmation

      if (confirmed) {
        this.authService.register(this.profileImage, this.registrationForm.value)
          .subscribe(
            () => {
              console.log('Inscription réussie'); // Message de succès dans la console
              this.redirectToLogin();
              this.notificationService.showSuccess('Inscription réussie ! un email vous a été envoyé !'); // Affichage du message de succès
            },
            error => {
              console.error('Erreur lors de l\'inscription :', error); // Message d'erreur dans la console
              this.notificationService.showError('Erreur lors de l\'inscription'); // Affichage du message d'erreur
            }
          );
      }
    }
  }

  redirectToLogin() {
    this.router.navigate(['/login']);
  }
  onImageSelected(event: Event) {
    const file = (event.target as HTMLInputElement).files[0];
    this.profileImage = file; // stockage du fichier pour une utilisation ultérieure

    // Optional: To preview the selected image
    const reader = new FileReader();
    reader.onload = () => {
      // this.imagePreview = reader.result as string;
    };
    reader.readAsDataURL(file);
  }

  initiateGoogleAuth() {
    if (this.authStateSubscription) {
      this.authStateSubscription.unsubscribe();
    }

    this.authStateSubscription = this.socialAuthService.authState.subscribe((user) => {
      this.user = user;
      this.loggedIn = (user != null);

      if (this.loggedIn) {
        this.authService.sendGoogleUserToBackend(user).subscribe(response => {
          console.log(response);

          // Après avoir envoyé l'utilisateur à l'arrière-plan, vérifiez s'il possède un mot de passe
          const userId = sessionStorage.getItem('id');
          this.checkPasswordAndImage(userId);
        });
      }
    });
  }
  private checkPasswordAndImage(userId: string): void {
    if (userId) {
      this.authService.hasPasswordAndImage(Number(userId)).subscribe(hasPwdAndImg => {
        if (hasPwdAndImg) {
          this.router.navigate(['/']); // Si l'utilisateur a un mot de passe, redirigez vers la page d'accueil
        } else {
          this.router.navigate(['/choose']); // Sinon, redirigez vers /choose
        }
      });
    }
  }

  ngOnDestroy() {
    if (this.authStateSubscription) {
      this.authStateSubscription.unsubscribe();
    }
  }
}

