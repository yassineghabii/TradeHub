import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class SupportService {
  private API_URL = "http://localhost:8080/SupportP";

  constructor(private httpClient: HttpClient) { }
  
  getAllSupport(){
    return this.httpClient.get(`${this.API_URL}/all`)
  }

  getSupportBytype(type : any){
    return  this.httpClient.get(`${this.API_URL}/getpartype/${type}`)
  }
  getSupportBytypevideo(){
    return  this.httpClient.get(`${this.API_URL}/getTypeVideo`)
  }
  getSupportBytypelivre(){
    return  this.httpClient.get(`${this.API_URL}/getTypeLivre`)
  }
  getSupportBytypearticle(){
    return  this.httpClient.get(`${this.API_URL}/getTypeArticle`)
  }

}
