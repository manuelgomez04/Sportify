import { HttpClient } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-equipos-seguidos',
  templateUrl: './equipos-seguidos.component.html',
  styleUrl: './equipos-seguidos.component.css'
})
export class EquiposSeguidosComponent implements OnInit {

  equipos: any[] = [];
  visibleCards = 4;
  currentIndex = 0;

  constructor(private http: HttpClient) { }
   ngOnInit() {
    this.http.get<any>('/equiposFavoritos?page=0&size=10').subscribe({
      next: resp => {
        this.equipos = resp.equiposFavoritos?.content || [];
      }
    });
  }

  get visibleEquipos() {
    return this.equipos.slice(this.currentIndex, this.currentIndex + this.visibleCards);
  }
  scrollLeft() {
    if (this.currentIndex > 0) {
      this.currentIndex--;
    }
  }
  scrollRight() {
    if (this.currentIndex < this.equipos.length - this.visibleCards) {
      this.currentIndex++;
    }
  }
  get atStart() {
    return this.currentIndex === 0;
  }
  get atEnd() {
    return this.currentIndex >= this.equipos.length - this.visibleCards || this.equipos.length <= this.visibleCards;
  }
  getTransform() {
    return `translateX(-${this.currentIndex * 196}px)`; 
  }

}
