import { Component, OnInit } from '@angular/core';
import { ProjetInvestissement } from 'app/Models/ProjetInvestissement';
import { ProjetIService } from 'app/services/projet-i.service';

@Component({
    selector: 'app-projet-i',
    templateUrl: './projet-i.component.html',
    styleUrls: ['./projet-i.component.scss']
})
export class ProjetIComponent implements OnInit {
    Projet:ProjetInvestissement;
    listProjet:any;
    Projetupdate:any;
    ProjetDetails:any;
    loca:any;
    powerBILink: string = "https://app.powerbi.com/reportEmbed?reportId=2727f024-71e0-4e0e-8854-8dccfefc6565&autoAuth=true&ctid=513486ec-6643-4f17-a508-76478311be42";

    constructor(private ProjetService:ProjetIService) {
        this.getAllProjet();
    }

    ngOnInit():void {

        this.Projet = {
            id: null,
            nom: null,
            description: null,
            date_debut:null,
            date_fin:null,
            cout_initial:null,
            localisation:null,
            responsable_projet:null,
            statutProjet:null,
            taux_interet:null
        }
        this.Projetupdate = {
            id: null,
            nom: null,
            description: null,
            date_debut:null,
            date_fin:null,
            cout_initial:null,
            localisation:null,
            responsable_projet:null,
            statutProjet:null,
            taux_interet:null
        }
    }

    getAllProjet() {
        this.ProjetService.getAllProjetI().subscribe(res => {
            this.listProjet = res;

        });
    }

    addProjet(){
        this.ProjetService.addProjetI(this.Projet).subscribe(()=> this.getAllProjet());

    }

    deleteProjet(idProjet:number){
        this.ProjetService.deleteProjet(idProjet).subscribe(()=>this.getAllProjet() ,res=>{this.listProjet=res});
    }

    edit(Event: any){
        this.Projetupdate = Event;
    }
    getActivitydetails(){
        this.ProjetService.getProjet(this.ProjetDetails.id).subscribe(res=>{this.ProjetDetails=res});
    }
    updateProjet(){
        this.ProjetService.updateProjet(this.Projetupdate).subscribe(
            (resp) => {
                console.log(resp);
            },
            (err) => {
                console.log(err);
            }
        );}
    getprojetbyloca(){
        this.ProjetService.getProjetByLoca(this.loca).subscribe(res=>{this.listProjet=res});
    }

}
