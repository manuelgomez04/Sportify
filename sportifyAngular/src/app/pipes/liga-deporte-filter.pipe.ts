import { Pipe, PipeTransform } from '@angular/core';
import { Liga } from '../models/liga/liga.model';

@Pipe({
  name: 'ligaDeporteFilter'
})
export class LigaDeporteFilterPipe implements PipeTransform {
  transform(ligas: Liga[], deporteFiltro: string): any[] {
    if (!deporteFiltro) return ligas;
    // Algunos objetos pueden tener el campo como 'deporte.nombreNoEspacio' o 'deporte.nombre'
    return ligas.filter(l =>
      l.deporteNoEspacio &&
      (l.deporteNoEspacio === deporteFiltro ||
       l.deporteNoEspacio === deporteFiltro ||
       l.deporteNoEspacio?.toLowerCase().replace(/ /g, '-').normalize('NFD').replace(/[\u0300-\u036f]/g, '') === deporteFiltro)
    );
  }
}
