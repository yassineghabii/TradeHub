import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { AboutComponent } from './pages/about/about.component';
import { ServiceComponent } from './pages/service/service.component';
import { DashboardComponent } from './dashboard/dashboard.component';
import { BlogComponent } from './components/blog/blog.component';
import {LoginComponent} from "./components/login/login.component";
import {InscriptionComponent} from "./components/inscription/inscription.component";
import {ForgetComponent} from "./components/forget/forget.component";
import {ResetComponent} from "./components/reset/reset.component";
import {AuthGuard} from "./services/authGuard";
import {ChatbotComponent} from "./chatbot/chatbot.component";
import {ChooseComponent} from "./components/choose/choose.component";
import {WalletComponent} from "./components/wallet/wallet.component";
import {StripeComponent} from "./components/stripe/stripe.component";
import {CardComponent} from "./components/card/card.component";

import { TopicComponent } from './components/topic/topic.component';
import { TopicDetailsComponent } from './components/topic-details/topic-details.component';

import { FormationComponent } from './components/formation/formation.component';
import { SupportComponent } from './components/support/support.component';
import { CalendrierComponent } from './components/calendrier/calendrier.component';
import {ReclamationComponent} from "./components/reclamation/reclamation/reclamation.component";
import { FaqListComponent } from './components/faq-list/faq-list.component';
import { FaqDetailsComponent } from './components/faq-details/faq-details.component';
import { StockAnalyseComponent } from './components/stock-analyse/stock-analyse.component';
import { StockPredictionComponent } from './components/stock-prediction/stock-prediction.component';
import { PowerbiComponent } from './powerbi/powerbi.component';
import { FlaskComponent } from './flask/flask.component';
import { FinancialProfileComponent } from './financial-profile/financial-profile.component';
import { FinancialProfileDisplayComponentComponent } from './financial-profile-display-component/financial-profile-display-component.component';


import {TradeComponent} from "./components/trade/trade.component";
import {ProfilleComponent} from "./components/profille/profille.component";
import {StreamComponent} from "./components/stream/stream.component";
import {OrdreachatComponent} from "./components/ordreachat/ordreachat.component";
import {OrdreventeComponent} from "./components/ordrevente/ordrevente.component";
import {TransComponent} from "./components/trans/trans.component";

const routes: Routes = [
  {
    path: '',
    component: DashboardComponent,
  },
  {
    path: 'topic/:id',
    component: TopicDetailsComponent,
    canActivate: [AuthGuard], 
  },
  {
    path: 'about',
    component: AboutComponent,
    canActivate: [AuthGuard], // Apply the AuthGuard
  },
  {
    path: 'service',
    component: ServiceComponent,
    canActivate: [AuthGuard], // Apply the AuthGuard
  },
  {
    path: 'ordre_achat',
    component: OrdreachatComponent,
    canActivate: [AuthGuard], // Apply the AuthGuard
  },
  {
    path: 'ordre_vente',
    component: OrdreventeComponent,
    canActivate: [AuthGuard], // Apply the AuthGuard
  },
  {
    path: 'trans',
    component: TransComponent,
    canActivate: [AuthGuard], // Apply the AuthGuard
  },
  {
    path: 'alloc',
    component: StreamComponent,
    canActivate: [AuthGuard], // Apply the AuthGuard
  },
  {
    path: 'profil',
    component: ProfilleComponent,
    canActivate: [AuthGuard], // Apply the AuthGuard
  },
  {
    path: 'predict',
    component: BlogComponent,
    canActivate: [AuthGuard], // Apply the AuthGuard
  },
  {
    path: 'login',
    component: LoginComponent,
    // No AuthGuard for the login page
  },
  {
    path: 'register',
    component: InscriptionComponent,
    // No AuthGuard for the registration page
  },
  {
    path: 'forget',
    component: ForgetComponent,
    // No AuthGuard for the forget password page
  },
  {
    path: 'trade',
    component: TradeComponent,
    canActivate: [AuthGuard] // Apply the AuthGuard
  },
  {
    path: 'reset',
    component: ResetComponent,
    // No AuthGuard for the reset password page
  },
  {
    path: 'chatbot',
    component: ChatbotComponent,
    canActivate: [AuthGuard], // Apply the AuthGuard
  },
  {
    path: 'choose',
    component: ChooseComponent,
    canActivate: [AuthGuard], // Apply the AuthGuard
  },
  {
    path: 'stripe',
    component: StripeComponent,
    canActivate: [AuthGuard], // Apply the AuthGuard
  },
  {
    path: 'wallet',
    component: WalletComponent,
    canActivate: [AuthGuard], // Apply the AuthGuard
  },
  {
    path: 'card',
    component: CardComponent,
    canActivate: [AuthGuard], // Apply the AuthGuard
  },
  {
    path: 'topic',
    component: TopicComponent,
    canActivate: [AuthGuard], 
  },
  {
    path: 'formation',
    component: FormationComponent,
   //canActivate: [AuthGuard], // Apply the AuthGuard
  },
  {
    path: 'support',
    component: SupportComponent,
   //canActivate: [AuthGuard], // Apply the AuthGuard
  },
  {
    path: 'economique',
    component: CalendrierComponent,
   //canActivate: [AuthGuard], // Apply the AuthGuard
  },
  
  {
    path: 'StockPrediction',
    component: StockPredictionComponent,
   // canActivate: [AuthGuard], // Apply the AuthGuard
  },
  {
    path: 'reclamation',
    component: ReclamationComponent,
    // canActivate: [AuthGuard], // Apply the AuthGuard
  },
  {
    path: 'StockAnalyse',
    component: StockAnalyseComponent,
    // canActivate: [AuthGuard], // Apply the AuthGuard
  },
{
    path: 'FaqDetail',
    component: FaqDetailsComponent,
    // canActivate: [AuthGuard], // Apply the AuthGuard
  },
  {
    path: 'FaqList',
    component: FaqListComponent,
    // canActivate: [AuthGuard], // Apply the AuthGuard
  }, 
{
  path: 'Power',
  component: PowerbiComponent,
 // canActivate: [AuthGuard], // Apply the AuthGuard
},

{
  path: 'Actua',
  component: FlaskComponent,
  canActivate: [AuthGuard], // Apply the AuthGuard
},

{
  path: 'financial-profile',
  component: FinancialProfileComponent,
  canActivate: [AuthGuard], // Apply the AuthGuard
},
{
  path: 'Mon Profile',
  component: FinancialProfileDisplayComponentComponent,
  canActivate: [AuthGuard], // Apply the AuthGuard
}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule],
})
export class AppRoutingModule {}
