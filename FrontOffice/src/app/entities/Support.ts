export enum Type {
    ARTICLE = "ARTICLE",
    LIVRE = "LIVRE",
    VIDEO = "VIDEO"
  }
  
export class Support {
    id: any;
    nom: any;
    description:any;
    date_publication:any;
    type: Type;
    medialink:any;

}
