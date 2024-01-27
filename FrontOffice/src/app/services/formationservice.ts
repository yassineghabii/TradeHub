import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class FormationService {
  private API_URL = "http://localhost:8080/formation";
 private API_URL1 = "http://localhost:8080/users";
  constructor(private httpClient: HttpClient) { }

  getAllFormation(){
    return this.httpClient.get(`${this.API_URL}/all`)
  }
  participer(idClient:any,idEvent:any){
    return this.httpClient.put(`${this.API_URL1}/assignFormationToClient/${idClient}/${idEvent}`,null);
  }
  annulerparticiper(idClient:any,idEvent:any){
    return this.httpClient.put(`${this.API_URL1}/unassignFormationFromClient/${idClient}/${idEvent}`,null);
  }
  voteLike(idevent:any,idclient:any){
    return this.httpClient.post(`http://localhost:8080/Vote/VoteLike/${idevent}/${idclient}`,null);
  }
  voteDislike(idevent:any,idclient:any){
    return this.httpClient.post(`http://localhost:8080/Vote/VoteDislike/${idevent}/${idclient}`,null);
  }
}
