// financial-profile.component.ts
import { Component, OnInit } from '@angular/core';
import { FinancialProfileService } from '../FinancialProfileService';
import { AuthService } from '../services/authservices';
import { FinancialProfile } from '../entities/financial-profile';

@Component({
  selector: 'app-financial-profile-display-component',
  templateUrl: './financial-profile-display-component.component.html',
  styleUrls: ['./financial-profile-display-component.component.css']
})
export class FinancialProfileDisplayComponentComponent implements OnInit {
  financialProfile: FinancialProfile;
  clientId: number;

  constructor(private financialProfileService: FinancialProfileService) {}

  ngOnInit(): void {
    this.getClientId();
  }

  getClientId(): void {
    const clientIdFromSession = sessionStorage.getItem('id_user');

    if (clientIdFromSession) {
      this.clientId = +clientIdFromSession;
      this.loadFinancialProfile(); // Call loadFinancialProfile once clientId is available
    } else {
      console.error("ID du client introuvable dans la session.");
    }
  }

  loadFinancialProfile(): void {
    this.financialProfileService.getFinancialProfileByClientId(this.clientId)
      .subscribe(
        (data: FinancialProfile) => {
          this.financialProfile = data;
          console.log(data);
        },
        error => {
          console.error(error);
        }
      );
  }
}
