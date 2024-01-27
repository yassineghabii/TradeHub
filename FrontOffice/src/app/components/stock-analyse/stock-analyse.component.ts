import { Component } from '@angular/core';
import {StockAnalyseService} from "../../services/stock-analyse.service";
import { DomSanitizer, SafeResourceUrl } from '@angular/platform-browser';

@Component({
  selector: 'app-stock-analyse',
  templateUrl: './stock-analyse.component.html',
  styleUrls: ['./stock-analyse.component.css']
})
export class StockAnalyseComponent {

  streamlitUrl: SafeResourceUrl;
  
  constructor(private sanitizer: DomSanitizer) {
    this.streamlitUrl = this.sanitizer.bypassSecurityTrustResourceUrl('http://localhost:8501');
  }
}
