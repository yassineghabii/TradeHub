import { CUSTOM_ELEMENTS_SCHEMA, NgModule, isDevMode } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { HeaderComponent } from './layout/header/header.component';
import { FooterComponent } from './layout/footer/footer.component';
import { ScrollComponent } from './components/scroll/scroll.component';
import { CarouselComponent } from './components/carousel/carousel.component';
import { AboutusComponent } from './components/aboutus/aboutus.component';
import { AboutUsBoxComponent } from './components/about-us-box/about-us-box.component';
import { ServicesComponent } from './components/services/services.component';
import { ServiceBoxComponent } from './components/service-box/service-box.component';
import { ChooesUsComponent } from './components/chooes-us/chooes-us.component';
import { ChooesUsBoxComponent } from './components/chooes-us-box/chooes-us-box.component';
import { BookMeetingComponent } from './components/book-meeting/book-meeting.component';
import { ClientReviewComponent } from './components/client-review/client-review.component';
import { LatestblogComponent } from './components/latestblog/latestblog.component';
import { LatestblogBoxComponent } from './components/latestblog-box/latestblog-box.component';
import { AboutComponent } from './pages/about/about.component';
import { ServiceComponent } from './pages/service/service.component';
import { DashboardComponent } from './dashboard/dashboard.component';
import { TeamMemberComponent } from './components/team-member/team-member.component';
import { BlogComponent } from './components/blog/blog.component';
import { NgbCarouselModule, NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { ServiceWorkerModule } from '@angular/service-worker';
import { NgxSpinnerModule } from 'ngx-spinner';
import { HttpClientModule } from '@angular/common/http';
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import {LoginComponent} from "./components/login/login.component";
import {ResetComponent} from "./components/reset/reset.component";
import { MatDialogModule } from '@angular/material/dialog';

import {InscriptionComponent} from "./components/inscription/inscription.component";
import {ForgetComponent} from "./components/forget/forget.component";
import {ChatbotComponent} from "./chatbot/chatbot.component";
import {
  SocialLoginModule,
  SocialAuthServiceConfig,
  GoogleSigninButtonModule,
  MicrosoftLoginProvider, AmazonLoginProvider
} from '@abacritt/angularx-social-login';
import {
  GoogleLoginProvider,
  FacebookLoginProvider
} from '@abacritt/angularx-social-login';
import {ChooseComponent} from "./components/choose/choose.component";
import { WalletComponent } from './components/wallet/wallet.component';
import {StripeComponent} from "./components/stripe/stripe.component";
import { PaymentmodalComponent } from './components/paymentmodal/paymentmodal.component';
import { MatIconModule } from '@angular/material/icon';
import {MatOptionModule} from "@angular/material/core";
import {MatSelectModule} from "@angular/material/select";
import {MatInputModule} from "@angular/material/input";
import {MatButtonModule} from "@angular/material/button";
import { ToastrModule } from 'ngx-toastr';
import { CardComponent } from './components/card/card.component';

import { NgxPaginationModule } from 'ngx-pagination';
import { WindowRef } from './services/window-ref.service';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { TopicComponent } from './components/topic/topic.component'; 
import { TopicDetailsComponent } from './components/topic-details/topic-details.component';

import { PowerbiComponent } from './powerbi/powerbi.component';
import { FinancialProfileComponent } from './financial-profile/financial-profile.component';
import { FinancialProfileDisplayComponentComponent } from './financial-profile-display-component/financial-profile-display-component.component';
import { FlaskComponent } from './flask/flask.component';
import { FormationComponent } from './components/formation/formation.component';
import { SupportComponent } from './components/support/support.component';
import { CalendrierComponent } from './components/calendrier/calendrier.component';
import { ReclamationComponent } from './components/reclamation/reclamation/reclamation.component';
import { StockAnalyseComponent } from './components/stock-analyse/stock-analyse.component';
import { FaqListComponent } from './components/faq-list/faq-list.component';
import { FaqDetailsComponent } from './components/faq-details/faq-details.component';


import { TradeComponent } from './components/trade/trade.component';
import {MatCardModule} from "@angular/material/card";
import firebase from "firebase/compat";
import { ProfilleComponent } from './components/profille/profille.component';
import { ChangePasswordModalComponentComponent } from './components/change-password-modal-component/change-password-modal-component.component';
import { ConfirmationModalComponent } from './components/confirmation-modal/confirmation-modal.component';
import { StreamComponent } from './components/stream/stream.component';
import { CarnetOrdresModalContentComponent } from './components/carnet-ordres-modal-content/carnet-ordres-modal-content.component';
import { OrdreachatComponent } from './components/ordreachat/ordreachat.component';
import { OrdreventeComponent } from './components/ordrevente/ordrevente.component';
import { TransComponent } from './components/trans/trans.component';
import GithubAuthProvider = firebase.auth.GithubAuthProvider;
@NgModule({
  declarations: [ChangePasswordModalComponentComponent,
TeamMemberComponent,
    AppComponent,
    HeaderComponent,
    FooterComponent,
    ScrollComponent,
    CarouselComponent,
    AboutusComponent,
    AboutUsBoxComponent,
    ServicesComponent,
    ServiceBoxComponent,
    ChooesUsComponent,
    ChooesUsBoxComponent,
    BookMeetingComponent,
    ClientReviewComponent,
    LatestblogComponent,
    LatestblogBoxComponent,
    AboutComponent,
    ServiceComponent,
    DashboardComponent,
    TeamMemberComponent,
    BlogComponent,
    LoginComponent,
    ResetComponent,
    InscriptionComponent,
    ForgetComponent,
    ChooseComponent,
    WalletComponent,
    StripeComponent,
    PaymentmodalComponent,
    CardComponent,
    TopicComponent,
    TopicDetailsComponent,
    PowerbiComponent,
    FinancialProfileComponent,
    FinancialProfileDisplayComponentComponent,
    ChatbotComponent,
    FlaskComponent,
    FormationComponent,
    SupportComponent,
    CalendrierComponent,
    ReclamationComponent,
    FaqDetailsComponent,
    FaqListComponent,
    StockAnalyseComponent,
    TradeComponent,
    ProfilleComponent,
    ChangePasswordModalComponentComponent,
    ConfirmationModalComponent,
    StreamComponent,
    CarnetOrdresModalContentComponent,
    OrdreachatComponent,
    OrdreventeComponent,
    TransComponent
  ],
    imports: [
        ToastrModule.forRoot(),// Ajoute ToastrModule avec sa méthode forRoot dans les imports
        ToastrModule,// Ajoute ToastrModule dans les imports du module
        BrowserModule, BrowserAnimationsModule,
        ToastrModule.forRoot({positionClass: 'inline'}),
        MatIconModule,
        BrowserAnimationsModule,
        BrowserModule,
        HttpClientModule,
        SocialLoginModule,
        AppRoutingModule,
        NgbModule,
        MatDialogModule,
        NgbCarouselModule,
        ServiceWorkerModule.register('ngsw-worker.js', {
            enabled: !isDevMode(),
            // Register the ServiceWorker as soon as the application is stable
            // or after 30 seconds (whichever comes first).
            registrationStrategy: 'registerWhenStable:30000',
        }),
        NgxSpinnerModule,
        ReactiveFormsModule,
        FormsModule,
        GoogleSigninButtonModule,
        MatIconModule,
        MatOptionModule,
        MatSelectModule,
        MatInputModule,
        MatButtonModule,
        MatCardModule,
        NgxPaginationModule,
        FontAwesomeModule,

    ],
  schemas: [CUSTOM_ELEMENTS_SCHEMA],
  providers: [
    WindowRef,
    {
      provide: 'SocialAuthServiceConfig',
      useValue: {
        autoLogin: false,
        providers: [
          {
            id: GoogleLoginProvider.PROVIDER_ID,
            provider: new GoogleLoginProvider(
              '625157114052-76n1i82aacdqat57l8oktmiqqeqlhnf5.apps.googleusercontent.com'
            )
          },
          {
            id: FacebookLoginProvider.PROVIDER_ID,
            provider: new FacebookLoginProvider('1058975985305530')
          },  {
            id: MicrosoftLoginProvider.PROVIDER_ID,
            provider: new MicrosoftLoginProvider('0611ccc3-9521-45b6-b432-039852002705')
          },
          {
            id: AmazonLoginProvider.PROVIDER_ID,
            provider: new AmazonLoginProvider(
              'amzn1.application-oa2-client.f074ae67c0a146b6902cc0c4a3297935'
            ),
          },
        ],
        onError: (err) => {
          console.error(err);
        }
      } as SocialAuthServiceConfig,
    }
  ],
  bootstrap: [AppComponent],
})
export class AppModule {}
