import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-services',
  templateUrl: './services.component.html',
  styleUrls: ['./services.component.css'],
})
export class ServicesComponent implements OnInit {
  services = [
    {
      icon: 'fas fa-calculator',
      heading: 'Estimation de Crédit en Temps Réel',
      description: 'Obtenez une estimation précise de votre capacité de crédit en quelques clics. Ne laissez pas l\'incertitude vous freiner.'
    },
    {
      icon: 'fas fa-robot',
      heading: 'Chatbot Financier 24/7',
      description: 'Vos questions ne suivent pas un horaire de bureau. Notre chatbot non plus. Obtenez des réponses instantanées à toute heure du jour ou de la nuit.'
    },
    {
      icon: 'fas fa-chart-line',
      heading: 'Conseil d\'Investissement',
      description: 'Maximisez vos retours avec des conseils stratégiques sur vos investissements. Parce que chaque centime compte.'
    },
    {
      icon: 'fas fa-balance-scale',
      heading: 'Optimisation de Portefeuille',
      description: 'Laissez-nous analyser votre portefeuille et suggérer des améliorations pour une meilleure allocation des actifs.'
    },
    {
      icon: 'fas fa-book',
      heading: 'Éducation Financière',
      description: 'Apprenez à votre rythme. Accédez à nos ressources en ligne pour développer vos connaissances financières.'
    },
    {
      icon: 'fas fa-simulation',
      heading: 'Simulation de Scénarios Financiers',
      description: 'Jouez avec des scénarios futurs pour mieux prévoir vos besoins financiers et vous préparer à toutes les éventualités.'
    }
  ];
  constructor() {}

  ngOnInit(): void {}
}
