import {Component, OnInit} from '@angular/core';
import { FAQ } from 'src/app/entities/faq';
import {FAQService} from "../../services/faqservice";

@Component({
  selector: 'app-faq-list',
  templateUrl: './faq-list.component.html',
  styleUrls: ['./faq-list.component.css']
})
export class FaqListComponent implements OnInit{
  faqs: FAQ[] = [];

  constructor(private faqService: FAQService) {}

  ngOnInit(): void {

    this.faqService.getFAQs().subscribe((faqs) => {
      this.faqs = faqs;
    });
  }


}
