import { HttpClient } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-deportes-seguidos',
  templateUrl: './deportes-seguidos.component.html',
  styleUrl: './deportes-seguidos.component.css'
})
export class DeportesSeguidosComponent implements OnInit {
  deportes: any[] = [];
  visibleCards = 4;
  currentIndex = 0;
  constructor(private http: HttpClient) { }

  ngOnInit() {
    this.http.get<any>('/deportesFavoritos?page=0&size=10').subscribe({
      next: resp => {
        this.deportes = resp.deportesFavoritos?.content || [];
      }
    });
  }

  get visibleDeportes() {
    return this.deportes.slice(this.currentIndex, this.currentIndex + this.visibleCards);
  }
  scrollLeft() {
    if (this.currentIndex > 0) {
      this.currentIndex--;
    }
  }
  scrollRight() {
    if (this.currentIndex < this.deportes.length - this.visibleCards) {
      this.currentIndex++;
    }
  }
  get atStart() {
    return this.currentIndex === 0;
  }
  get atEnd() {
    return this.currentIndex >= this.deportes.length - this.visibleCards || this.deportes.length <= this.visibleCards;
  }
  getTransform() {
    return `translateX(-${this.currentIndex * 196}px)`; 
  }
}
