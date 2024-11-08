
export interface IDevoir {
    idDevoir:number,
    typedevoir:String,
    description:String,
    ponderation:number,
    bareme:number,
    dateLimite:Date,
    statut:String,
    pdf?:File,
    sousGroupes:any[];
}
