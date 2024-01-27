import { Routes } from '@angular/router';

import { HomeComponent } from '../../home/home.component';
import { UserComponent } from '../../user/user.component';
import { TablesComponent } from '../../tables/tables.component';
import { TypographyComponent } from '../../typography/typography.component';
import { IconsComponent } from '../../icons/icons.component';
import { MapsComponent } from '../../maps/maps.component';
import { NotificationsComponent } from '../../notifications/notifications.component';
import { UpgradeComponent } from '../../upgrade/upgrade.component';
import { FormationComponent } from 'app/component/formation/formation.component';
import { SupportComponent } from 'app/component/support/support.component';
import { ProjetIComponent } from 'app/component/projet-i/projet-i.component';
import {WalletComponent} from "../../component/wallet/wallet.component";
import {CardsComponent} from "../../component/cards/cards.component";
import {AdminsComponent} from "../../component/admins/admins.component";
import {ClientsComponent} from "../../component/clients/clients.component";
import {LoginComponent} from "../../component/login/login.component";
import {AuthGuard} from "../../services/authGuard";
import {AuthGuardian} from "../../services/authGuardian";
import {ProfileComponent} from "../../component/profile/profile.component";
import {ReclamationComponent} from "../../component/reclamation/reclamation.component";

export const AdminLayoutRoutes: Routes = [
    { path: 'projet Investissement',  data: { role: ['admin'] },  component: ProjetIComponent   ,  canActivate: [AuthGuard, AuthGuardian],
    },
    { path: 'formation',   data: { role: ['admin'] },    component: FormationComponent ,  canActivate: [AuthGuard, AuthGuardian] },
    { path: 'support',     data: { role: ['admin'] },  component: SupportComponent  ,  canActivate: [AuthGuard, AuthGuardian]},
    { path: 'dashboard',     data: { role: ['admin'] },  component: HomeComponent  ,  canActivate: [AuthGuard, AuthGuardian]},
    { path: 'user',          data: { role: ['admin'] },  component: UserComponent  ,  canActivate: [AuthGuard, AuthGuardian]},
    { path: 'table',         data: { role: ['admin'] },  component: TablesComponent  ,  canActivate: [AuthGuard, AuthGuardian]},
    { path: 'typography',    data: { role: ['admin'] },  component: TypographyComponent  ,  canActivate: [AuthGuard, AuthGuardian]},
    { path: 'icons',         data: { role: ['admin'] },  component: IconsComponent ,  canActivate: [AuthGuard, AuthGuardian] },
    { path: 'maps',          data: { role: ['admin'] },  component: MapsComponent ,  canActivate: [AuthGuard, AuthGuardian] },
    { path: 'notifications',  data: { role: ['admin'] }, component: NotificationsComponent  ,  canActivate: [AuthGuard, AuthGuardian]},
    { path: 'upgrade',       data: { role: ['admin'] },  component: UpgradeComponent ,  canActivate: [AuthGuard, AuthGuardian] },
    { path: 'wallet',       data: { role: ['admin'] },  component: WalletComponent ,  canActivate: [AuthGuard, AuthGuardian] },
    { path: 'cards',        data: { role: ['admin'] }, component: CardsComponent  ,  canActivate: [AuthGuard, AuthGuardian]},
    { path: 'admins',      data: { role: ['admin'] },   component: AdminsComponent  ,  canActivate: [AuthGuard, AuthGuardian]},
    { path: 'clients',       data: { role: ['admin'] },  component: ClientsComponent  ,  canActivate: [AuthGuard, AuthGuardian]},
    { path: 'profil',       data: { role: ['admin'] },  component: ProfileComponent  ,  canActivate: [AuthGuard, AuthGuardian]},
    { path: 'reclamation', data: { role: ['admin'] },    component: ReclamationComponent ,  canActivate: [AuthGuard, AuthGuardian] }

];
