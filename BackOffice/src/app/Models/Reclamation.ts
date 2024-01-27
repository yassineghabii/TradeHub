export class Reclamation {
    id: number;
    date: Date
    email: string;
    description: string;
    status : StatusReclamation;
}


export enum StatusReclamation {
    NEW = 'NEW',
    IN_PROGRESS = 'IN_PROGRESS',
    RESOLVED = 'RESOLVED',
    CLOSED = 'CLOSED',
}

