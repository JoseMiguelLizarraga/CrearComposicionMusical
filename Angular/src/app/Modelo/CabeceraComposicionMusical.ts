import { DetalleComposicionMusical } from './DetalleComposicionMusical';
import { Usuario } from './Usuario';

export class CabeceraComposicionMusical 
{
    id:number;
    visible:boolean; 
    autor:String;
    titulo:String;
    usuario:Usuario;
    listaDetalleComposicionMusical: DetalleComposicionMusical[];
}

