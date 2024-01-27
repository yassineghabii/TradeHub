import { Injectable } from '@angular/core';
import {
  CanActivate,
  ActivatedRouteSnapshot,
  RouterStateSnapshot,
  Router
} from '@angular/router';

@Injectable({
  providedIn: 'root'
})
export class AuthGuard implements CanActivate {

  constructor(private router: Router) { }

  canActivate(
    route: ActivatedRouteSnapshot,
    state: RouterStateSnapshot
  ): boolean {
    if (this.getToken()) {
      // User is authenticated
      return true;
    } else {
      // User is not authenticated. Redirect to login page
      this.router.navigate(['/login']);
      return false;
    }
  }

  getToken(): string | null {
    const token = sessionStorage.getItem('token');
    const cleanedToken = token ? token.replace(/"/g, '') : null; // remove double quotes if they exist
    console.log("token:", cleanedToken); // log the token
    return cleanedToken;
  }
}
