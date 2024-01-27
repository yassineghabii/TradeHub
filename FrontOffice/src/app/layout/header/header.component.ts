import { Component, OnDestroy, OnInit } from '@angular/core';
import { Router } from "@angular/router";
import { AuthService } from "../../services/authservices";
import { Subscription } from "rxjs";
import {SocialAuthService} from "@abacritt/angularx-social-login";

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.css']
})
export class HeaderComponent implements OnInit, OnDestroy {
  userName: string  = null;

  private authStateSubscription: Subscription | undefined; // Subscription for the auth state

  constructor(
    private socialAuthService: SocialAuthService,
    private authService: AuthService,
    private router: Router  // <- Inject the Router service
  ) { }
  public isLoading: boolean = false;

  ngOnInit() {
    const userName = this.getName();
  }

  ngOnDestroy() {
    // Unsubscribe from Google Authenticator
    if (this.authStateSubscription) {
      this.authStateSubscription.unsubscribe();
    }
  }

  public isHovering: boolean = false;

  getToken(): boolean {
    return !!sessionStorage.getItem('token');
  }

  getLogoutLinkStyle(): object {
    return { 'color': this.isHovering ? '#0056b3' : '#007bff' };
  }

  logout(): void {
    console.log('Logout method called');

    // Déconnexion du service d'authentification sociale, qui couvrira tous les fournisseurs de connexion sociale.
    // SignOut() est une méthode asynchrone.
    this.socialAuthService.signOut().then(() => {
      console.log('Logged out from social login provider.');
      // Executez toujours le nettoyage de session après la tentative de déconnexion sociale.
      this.performLogoutCleanup();
    }).catch((error) => {
      console.error('Social logout error:', error);
      // Même en cas d'échec du Social SignOut, effectuez le nettoyage de session.
      this.performLogoutCleanup();
    });
  }

  performLogoutCleanup() {
    // Utilisateur potentiellement déconnecté du backend ici, si nécessaire.
    const userId = this.getUserId();
    if (userId !== null) {
      // Inform the server about the logout if there's a userId.
      this.authService.logout(userId).subscribe(
        response => console.log('Logout successful:', response),
        error => console.error('Error during logout:', error),
        () => this.finalizeLogout()
      );
    } else {
      // If userId isn't found, just proceed to the front-end logout process.
      this.finalizeLogout();
    }
  }

  finalizeLogout() {
    // Clear session storage and other cleanup actions.
    sessionStorage.clear();
    console.log('Session storage cleared');

    // Redirect to the login page.
    this.router.navigate(['/login']).then(() => {
      console.log('User is redirected to login page.');
    });
  }
  private navigateToLogin(): void {
    this.router.navigate(['/login']).then(() => {
      // Réinitialisez isLoading ici, si applicable
      this.isLoading = false;
    });
  }



shouldDisplayHeader(): boolean {
    return this.router.url !== '/register';
  }
  shouldDisplayHeader1(): boolean {
    return this.router.url !== '/choose';
  }

  redirectToRoleBasedPage(): void {
    event.stopPropagation();
    console.log("redirectToRoleBasedPage triggered");
  }
  googleSignOut(): void {
    this.socialAuthService.signOut();
  }
  FacebookSignOut(): void {
    this.FacebookSignOut();
  }
  getUserId(): number | null {
    const userIdStr = sessionStorage.getItem('id');
    return userIdStr ? Number(userIdStr) : null;
  }
  getName(): string | null {
    // Check if we've already logged the name
    const hasLoggedName = sessionStorage.getItem('hasLoggedName');

    const name = sessionStorage.getItem('full_name');
    const cleanedName = name ? name.replace(/"/g, '') : null; // remove double quotes if they exist

    if (!hasLoggedName) {
      console.log("full_name:", cleanedName); // log the role
      sessionStorage.setItem('hasLoggedName', 'true'); // Set the flag in session storage
    }

    return cleanedName;
  }

}
