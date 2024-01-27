import { Component } from '@angular/core';

@Component({
  selector: 'app-chooes-us',
  templateUrl: './chooes-us.component.html',
  styleUrls: ['./chooes-us.component.css'],
})
export class ChooesUsComponent {
  servcies: any = [
    {
      icon: 'fa fa-calculator fs-4  text-white',
      heading: 'Estimation en Temps Réel',
      text: 'Recevez une estimation précise et rapide pour vos crédits en quelques clics.',
    },
    {
      icon: 'fa fa-robot fs-4  text-white',
      heading: 'Chatbot Financier',
      text: 'Notre chatbot, expert en finance, répond à vos questions à tout moment.',
    },
    {
      icon: 'fa fa-shield-alt fs-4  text-white',
      heading: 'Sécurité Assurée',
      text: 'Vos données sont protégées grâce à nos technologies de sécurité de pointe.',
    },
  ];

  serviceTwo: any = [
    {
      icon: 'fa fa-check-circle fs-4  text-white',
      heading: 'Précision Garantie',
      text: 'Nos algorithmes avancés garantissent la précision de nos estimations.',
    },
    {
      icon: 'fa fa-cogs fs-4  text-white',
      heading: 'Personnalisation Avancée',
      text: 'Notre plateforme  s\'adapte à vos besoins spécifiques pour une expérience utilisateur optimale.',
    },
    {
      icon: 'fa fa-microphone-alt fs-4  text-white',
      heading: 'Reconnaissance Vocale et Faciale',
      text: 'Notre chatbot est équipé des dernières technologies de reconnaissance vocale et faciale pour une expérience utilisateur immersive et sécurisée.',
    }
    ,
  ];
}
