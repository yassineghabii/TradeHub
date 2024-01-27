import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { AuthService } from '../../services/authservices';
import { Router } from "@angular/router";
import {
  AmazonLoginProvider,
  FacebookLoginProvider,
  MicrosoftLoginProvider,
  SocialAuthService,
  SocialUser
} from "@abacritt/angularx-social-login";
import {NotificationService} from "../wallet/NotificationService";

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css'],
})
export class LoginComponent implements OnInit {
  loginForm: FormGroup;
  errorMessage: string;
  user: SocialUser;
  loggedIn: boolean;
  private authStateSubscription: any;

  constructor(
    private formBuilder: FormBuilder,
    private authService: AuthService,
    private router: Router,
    private service: SocialAuthService,
    private customAuthService: AuthService,
    private notificationService: NotificationService  ) {}

  ngOnInit() {
    this.initializeLoginForm();
    this.initiateGoogleAuth();

  }


  initiateGoogleAuth() {
    if (this.authStateSubscription) {
      this.authStateSubscription.unsubscribe();
    }

    this.authStateSubscription = this.service.authState.subscribe((user) => {
      this.user = user;
      this.loggedIn = (user != null);

      if (this.loggedIn) {
        this.customAuthService.sendGoogleUserToBackend(user).subscribe(response => {
          console.log(response);

          const userId = sessionStorage.getItem('id');
          console.log('Stored User ID:', userId);
          console.log('Stored Token:', sessionStorage.getItem('token'));

          this.checkPasswordAndImage(userId);
        });
      }
    });
  }
  initiateFbAuth() {
    // Assurez-vous de vous désabonner de l'abonnement précédent s'il existe
    if (this.authStateSubscription) {
      this.authStateSubscription.unsubscribe();
    }

    // Déclenchez l'authentification Facebook avec le fournisseur correspondant
    this.service.signIn(FacebookLoginProvider.PROVIDER_ID)
      .then((response) => {
        // Traitez la réponse ici si nécessaire
        console.log('Réponse de l\'authentification Facebook :', response);

        // Votre logique supplémentaire ici après l'authentification
      })
      .catch((error) => {
        // Gérez les erreurs ici
        console.error('Erreur lors de l\'authentification Facebook :', error);
      });

    // Abonnez-vous au changement d'état d'authentification via le service d'authentification sociale
    this.authStateSubscription = this.service.authState.subscribe((user) => {
      this.user = user;
      this.loggedIn = (user != null);

      // Si l'utilisateur est authentifié via Facebook, envoyez les données à votre backend
      if (this.loggedIn && user.provider === FacebookLoginProvider.PROVIDER_ID) {
        this.customAuthService.sendFaceUserToBackend(user).subscribe(response => {
          console.log(response);

          const userId = sessionStorage.getItem('id');
          console.log('Identifiant utilisateur stocké :', userId);
          console.log('Token stocké :', sessionStorage.getItem('token'));

          // Vérifiez si l'utilisateur a une image de profil et un mot de passe enregistrés
          this.checkPasswordAndImage(userId);
        });
      }
    });
  }
  initiateMAuth() {
    if (this.authStateSubscription) {
      this.authStateSubscription.unsubscribe();
    }

    this.authStateSubscription = this.service.authState.subscribe((user) => {
      this.user = user;
      this.loggedIn = (user != null);

      if (this.loggedIn) {
        this.customAuthService.sendMUserToBackend(user).subscribe(response => {
          console.log(response);

          const userId = sessionStorage.getItem('id');
          console.log('Stored User ID:', userId);
          console.log('Stored Token:', sessionStorage.getItem('token'));

          // Check password and image or any other post-login actions here
          this.checkPasswordAndImage(userId);
        });
      }
    });

    // Sign in with Microsoft
    this.service.signIn(MicrosoftLoginProvider.PROVIDER_ID);
  }
  initiateAAuth() {
    if (this.authStateSubscription) {
      this.authStateSubscription.unsubscribe();
    }

    this.authStateSubscription = this.service.authState.subscribe((user) => {
      this.user = user;
      this.loggedIn = (user != null);

      if (this.loggedIn) {
        this.customAuthService.sendAUserToBackend(user).subscribe(response => {
          console.log(response);

          const userId = sessionStorage.getItem('id');
          console.log('Stored User ID:', userId);
          console.log('Stored Token:', sessionStorage.getItem('token'));

          // Check password and image or any other post-login actions here
          this.checkPasswordAndImage(userId);
        });
      }
    });

    // Sign in with Microsoft
    this.service.signIn(AmazonLoginProvider.PROVIDER_ID);
  }

  private initializeLoginForm(): void {
    this.loginForm = this.formBuilder.group({
      email: ['', [Validators.required, Validators.email]],
      password: ['', Validators.required]
    });
  }

  onSubmit(): void {
    if (this.loginForm.valid) {
      this.authService.login(this.loginForm.value).subscribe(
        _ => this.handleLoginSuccess(),
        error => this.handleLoginError(error)
      );
    }
  }

  private handleLoginSuccess(): void {
    this.navigateToRoleBasedRoute();
  }

  private handleLoginError(error: any): void {
    if (error.status === 401) {
      this.errorMessage = "Invalid credentials. Please try again.";
    } else {
      this.errorMessage = "An unexpected error occurred. Please try again later.";
      console.error('Login error:', error);
    }
  }

  private navigateToRoleBasedRoute(): void {
    this.router.navigate(['/']);
  }

  ngOnDestroy() {
    if (this.authStateSubscription) {
      this.authStateSubscription.unsubscribe();
    }
  }

  private checkPasswordAndImage(userId: string): void {
    if (userId) {
      this.customAuthService.hasPasswordAndImage(Number(userId)).subscribe(hasPwdAndImg => {
        if (hasPwdAndImg) {
          this.navigateToRoleBasedRoute();
        } else {
          this.router.navigate(['/choose']);
        }
      });
    }
  }
}
