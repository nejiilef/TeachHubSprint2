import { Typedevoir } from "./typedevoir";

export interface IDevoirDTO {
    typedevoir:String,
    description:String,
    ponderation:number,
    bareme:number,
    dateLimite:Date,
    statut:String,
    pdf?:File,
    sousGroupes:string;
}
