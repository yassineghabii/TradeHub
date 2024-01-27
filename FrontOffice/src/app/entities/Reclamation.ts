export interface Reclamation {
    id?: number;
    date?: Date;
    sujet?: string;
    email?: string;
    description?: string;
    status?: StatusReclamation;
    archived?: boolean;
  }
  
  export enum StatusReclamation {
    NEW = 'NEW',
    IN_PROGRESS = 'IN_PROGRESS',
    RESOLVED = 'RESOLVED',
    CLOSED = 'CLOSED',
  }
  
  