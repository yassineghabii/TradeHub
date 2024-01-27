import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import {Router, RouterModule} from '@angular/router';
import { NavbarComponent } from './navbar.component';
import {AuthService} from "../../services/authservices";
import {ChtibaService} from "../../services/chtiba.service";

@NgModule({
    imports: [ RouterModule, CommonModule ],
    declarations: [ NavbarComponent ],
    exports: [ NavbarComponent ]
})

export class NavbarModule {
    getImageUrlFromSessionStorage(): string {
        // Récupérer l'ID depuis le session storage
        const userId = sessionStorage.getItem('id');

        // Construire l'URL de l'image en utilisant l'ID récupéré
        const imageUrl = `http://localhost:8080/users/${userId}/image`;

        return imageUrl;
    }


}























