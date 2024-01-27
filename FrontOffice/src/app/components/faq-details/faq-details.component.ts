import { Component } from '@angular/core';
import {FAQService} from "../../services/faqservice";
import {ActivatedRoute} from "@angular/router";
import {FAQ} from "../../entities/faq";

@Component({
  selector: 'app-faq-details',
  templateUrl: './faq-details.component.html',
  styleUrls: ['./faq-details.component.css']
})
export class FaqDetailsComponent {
  faq: FAQ | undefined;

  constructor(
    private route: ActivatedRoute,
    private faqService: FAQService
  ) {}

 ngOnInit(): void {
  const id = this.route.snapshot.paramMap.get('id');
  if (id) {
    this.faqService.getFAQById(+id).subscribe((faq) => {
      this.faq = faq;
    });
  }
}
}
