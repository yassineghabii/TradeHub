import { Component, OnInit } from '@angular/core';

declare const $: any;
declare interface RouteInfo {
    path: string;
    title: string;
    icon: string;
    class: string;
}
/* { path: '/projet d\'investissement', title: 'Projets d\'investissements',  icon:'pe-7s-cash', class: '' },*/
    /*  { path: '/formation', title: 'Formations',  icon:'pe-7s-news-paper', class: '' },*/
  /*    { path: '/support', title: 'Support pédagogique',  icon:'pe-7s-pen', class: '' },*/
 /*     { path: '/dashboard', title: 'Dashboard',  icon: 'pe-7s-graph', class: '' },*/
  /*    { path: '/user', title: 'User Profile',  icon:'pe-7s-user', class: '' },*/
  /*    { path: '/table', title: 'Table List',  icon:'pe-7s-note2', class: '' },*/
  /*    { path: '/typography', title: 'Typography',  icon:'pe-7s-news-paper', class: '' },*/
    /*  { path: '/icons', title: 'Icons',  icon:'pe-7s-science', class: '' },*/
   /*   { path: '/maps', title: 'Maps',  icon:'pe-7s-map-marker', class: '' },*/
  /*    { path: '/notifications', title: 'Notifications',  icon:'pe-7s-bell', class: '' },*/

export const ROUTES: RouteInfo[] = [
    { path: '/admins', title: 'Employés', icon:'pe-7s-users', class: '' },
    { path: '/wallet', title: 'Portefeuilles',  icon:'pe-7s-wallet', class: '' },
    { path: '/cards', title: 'Cartes', icon:'fa fa-credit-card', class: '' },
    { path: '/clients', title: 'Clients', icon:'pe-7s-users', class: '' },
    { path: '/projet Investissement', title: 'Projets D\'Investissements',  icon:'pe-7s-cash', class: '' },
    { path: '/formation', title: 'Formations',  icon:'pe-7s-news-paper', class: '' },
    { path: '/support', title: 'Supports Pédagogiques',  icon:'pe-7s-pen', class: '' },
    { path: '/reclamation', title: 'Reclamations',  icon:'pe-7s-note2', class: '' },

];

@Component({
  selector: 'app-sidebar',
  templateUrl: './sidebar.component.html',
    styleUrls: ['./s.css']

})
export class SidebarComponent implements OnInit {
  menuItems: any[];

  constructor() { }

  ngOnInit() {
    this.menuItems = ROUTES.filter(menuItem => menuItem);
  }
  isMobileMenu() {
      if ($(window).width() > 991) {
          return false;
      }
      return true;
  };
}
