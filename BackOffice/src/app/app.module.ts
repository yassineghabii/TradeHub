import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { NgModule } from '@angular/core';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import { HttpClientModule } from '@angular/common/http';
import { RouterModule } from '@angular/router';
import { AppRoutingModule } from './app.routing';
import { FooterModule } from './shared/footer/footer.module';
import { SidebarModule } from './sidebar/sidebar.module';
import { AppComponent } from './app.component';
import { AdminLayoutComponent } from './layouts/admin-layout/admin-layout.component';
import { FormationComponent } from './component/formation/formation.component';
import { FullCalendarModule } from '@fullcalendar/angular';
import dayGridPlugin from '@fullcalendar/daygrid';
import interactionPlugin from '@fullcalendar/interaction';
import { SupportComponent } from './component/support/support.component';
import { ProjetIComponent } from './component/projet-i/projet-i.component';
import { WalletComponent } from "./component/wallet/wallet.component";
import { MatDialogModule } from '@angular/material/dialog';
import { ConfirmModalComponent } from './component/confirm-modal/confirm-modal.component';
import { UsersComponent } from './users/users.component';
import {CardsComponent} from "./component/cards/cards.component";
import { AdminsComponent } from './component/admins/admins.component';
import {MatIconModule} from "@angular/material/icon";
import {MatButtonModule} from "@angular/material/button";
import { ClientsComponent } from './component/clients/clients.component';
import {LoginComponent} from "./component/login/login.component";
import { ProfileComponent } from './component/profile/profile.component';
import {MatPaginatorModule} from "@angular/material/paginator";

import { NavbarModule } from './shared/navbar/navbar.module';
import { ReclamationComponent } from './component/reclamation/reclamation.component';

FullCalendarModule.registerPlugins([
  interactionPlugin,
  dayGridPlugin
]);
@NgModule({
  imports: [
    BrowserAnimationsModule,
    FormsModule,
    RouterModule,
    HttpClientModule,
NavbarModule,
    FooterModule,
    SidebarModule,
    AppRoutingModule,
    FullCalendarModule,
    MatDialogModule,
    MatIconModule,
    MatButtonModule,
    ReactiveFormsModule,
      MatPaginatorModule
  ],
  declarations: [
    AppComponent,
    AdminLayoutComponent,
    FormationComponent,
    SupportComponent,
    ProjetIComponent,
    WalletComponent,
    ConfirmModalComponent,
    UsersComponent,
    CardsComponent,
    AdminsComponent,
    ClientsComponent,
      LoginComponent,
      ProfileComponent,
      ReclamationComponent,

  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
