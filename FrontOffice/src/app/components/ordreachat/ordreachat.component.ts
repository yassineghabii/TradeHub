import { Component, OnInit } from '@angular/core';
import { OrdreAchatAuMarcheDTO } from "../../entities/OrdreAchatAuMarcheDTO";
import { OrdreAchatLimiteDTO } from "../../entities/OrdreAchatLimiteDTO";
import { MariemService } from "../../services/mariem.service";
import {Observable} from "rxjs";
import { faBan  } from '@fortawesome/free-solid-svg-icons'

@Component({
  selector: 'app-ordreachat',
  templateUrl: './ordreachat.component.html',
  styleUrls: ['./ordreachat.component.css']
})
export class OrdreachatComponent implements OnInit {
  faBan = faBan;
  searchTerm: string = '';

  ordresAchatLimite: OrdreAchatLimiteDTO[] = [];
  ordresAchatAuMarche: OrdreAchatAuMarcheDTO[] = [];
  selectedType = 'limite';

  constructor(private mariemService: MariemService) {}
  getFilteredData() {
    return this.ordresAchatLimite.filter(ordre =>
      ordre.symbole.toLowerCase().includes(this.searchTerm.toLowerCase()) ||
      ordre.statut.toLowerCase().includes(this.searchTerm.toLowerCase())
    );
  }
  getFilteredDataA(){
    return this.ordresAchatAuMarche.filter(ordre =>
      ordre.symbole.toLowerCase().includes(this.searchTerm.toLowerCase()) ||
      ordre.statut.toLowerCase().includes(this.searchTerm.toLowerCase())
    );
  }


  ngOnInit() {
    this.mariemService.getOrdresAchatLimiteByClientId(this.getUserId()).subscribe((data: OrdreAchatLimiteDTO[]) => {
      this.ordresAchatLimite = data;
    });

    this.mariemService.getOrdresAchatAuMarcheByClientId(this.getUserId()).subscribe((data: OrdreAchatAuMarcheDTO[]) => {
      this.ordresAchatAuMarche = data;
    });
    this.getSelectedTypeData().subscribe((data) => {
      this.updateOrdreDataBasedOnType(data);
    });

  }
  getSelectedTypeData(): Observable<any> {
    const id = this.getUserId();
    return this.selectedType === 'limite' ?
      this.mariemService.getOrdresAchatLimiteByClientId(id) :
      this.mariemService.getOrdresAchatAuMarcheByClientId(id);
  }

  // Call this method when the selectedType changes
  onTypeChange() {
    this.getSelectedTypeData().subscribe((data) => {
      this.updateOrdreDataBasedOnType(data);
    });
  }

  private updateOrdreDataBasedOnType(data: any) {
    if (this.selectedType === 'limite') {
      this.ordresAchatLimite = data;
    } else {
      this.ordresAchatAuMarche = data;
    }
  }
  annulerOrdreAchat(idOrdre: number) {
    this.mariemService.annulerOrdreA(idOrdre).subscribe(
      response => {
        // Handle the response, e.g., refresh the list or show a message
      },
      error => {
        // Handle errors here
      }
    );
  }

  getUserId(): number | null {
    const userIdStr = sessionStorage.getItem('id');
    return userIdStr ? Number(userIdStr) : null;
  }

}
