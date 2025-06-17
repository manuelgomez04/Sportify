import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'equipoNombreFilter'
})
export class EquipoNombreFilterPipe implements PipeTransform {
  transform(equipos: any[], filtro: string): any[] {
    if (!equipos) return [];
    if (!filtro) return equipos;
    const filtroLower = filtro.toLowerCase();
    return equipos.filter(e => e.nombre?.toLowerCase().includes(filtroLower));
  }
}
