// auth-guardian.service.ts

import { Injectable } from '@angular/core';
import { CanActivate, Router, ActivatedRouteSnapshot } from '@angular/router';

@Injectable({
  providedIn: 'root'
})
export class AuthGuardian implements CanActivate {

  constructor(private router: Router) { }

  canActivate(route: ActivatedRouteSnapshot): boolean {
    const expectedRoles: string[] = route.data['role'] || [];
    const userRole = this.getRole();

    if (!userRole || !expectedRoles.length || !expectedRoles.includes(userRole)) {
      console.log("Ne peut pas accéder à la route");
      this.router.navigate(['/login']);
      return false;
    }

    console.log("Peut accéder à la route");
    return true;
  }

  getRole(): string | null {
    const role = sessionStorage.getItem('role');
    const cleanedRole = role ? role.replace(/"/g, '') : null;
    return cleanedRole;
  }
}
