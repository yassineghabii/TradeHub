import { Component, OnInit, ElementRef } from '@angular/core';
import { ROUTES } from '../../sidebar/sidebar.component';
import {Location, LocationStrategy, PathLocationStrategy} from '@angular/common';
import {AuthService} from "../../services/authservices";
import {ChtibaService} from "../../services/chtiba.service";
import {Router} from "@angular/router";

@Component({
    // moduleId: module.id,
    selector: 'navbar-cmp',
    templateUrl: './navbar.component.html',
    styleUrls: ['./navbar.component.css']

})

export class NavbarComponent implements OnInit{
    private listTitles: any[];
    location: Location;
    private toggleButton: any;
    private sidebarVisible: boolean;

    constructor(location: Location,     private authService: AuthService,    private apiService: ChtibaService,    private router: Router , // <- Inject the Router service


    private element: ElementRef) {
      this.location = location;
          this.sidebarVisible = false;
        this.listTitles = ROUTES.filter(listTitle => listTitle);

    }
    public userRole: string | null;
    public isHovering: boolean = false;
    ngOnInit(): void {
        this.userRole = this.authService.getRole().replace(/"/g, '');
    }
    getLogoutLinkStyle(): object {
        return {'color': this.isHovering ? '#0056b3' : '#007bff'};
    }
    getFullName(): string | null {
        const firstName = sessionStorage.getItem('firstname');
        const lastName = sessionStorage.getItem('lastname');
        const cleanedFirstName = firstName ? firstName.replace(/"/g, '') : null;
        const cleanedLastName = lastName ? lastName.replace(/"/g, '') : null;
        return cleanedFirstName && cleanedLastName ? `${cleanedFirstName} ${cleanedLastName}` : null;
    }

    logout(): void {
        const userId = this.authService.getUserId();
        if (userId !== null) {
            this.authService.logout(userId).subscribe(
                response => {
                    // Handle successful logout here
                    sessionStorage.clear(); // clearing the session storage
                    this.router.navigate(['/login']);  // <- Redirect to /login
                },
                error => {
                    console.error('Logout error:', error.message);
                    console.error('Error details:', error.error);
                }
            );
        }
    }
    redirectToRoleBasedPage(): void {
        event.stopPropagation();
        console.log("redirectToRoleBasedPage triggered"); // Add this line
        const role = this.getRole();

        switch (role) {
            case 'client':
                this.router.navigate(['/louer']);
                break;
            case 'admin':
                this.router.navigate(['/user']);
                break;
            default:
                // Optionally: Navigate to some default page
                break;
        }
    }

    getRole(): string | null {
        const role = sessionStorage.getItem('role');
        return role ? role.replace(/"/g, '') : null; // remove double quotes if they exist
    }

    getTitle(){
      var titlee = this.location.prepareExternalUrl(this.location.path());
      if(titlee.charAt(0) === '#'){
          titlee = titlee.slice( 1 );
      }

      for(var item = 0; item < this.listTitles.length; item++){
          if(this.listTitles[item].path === titlee){
              return this.listTitles[item].title;
          }
      }
      return 'Dashboard';
    }

    getImageUrlFromSessionStorage(): string {
        // Récupérer l'ID depuis le session storage
        const userId = sessionStorage.getItem('userId');

        // Construire l'URL de l'image en utilisant l'ID récupéré
        const imageUrl = `http://localhost:8080/users/${userId}/image`;

        return imageUrl;
    }

}
