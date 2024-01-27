import {Client} from "./Client";
import {Card} from "./Card";

export class Wallet {
  id_wallet: number;
  user: Client;
  Virtual_balance: number;
  Real_balance: number;
  Real_Currency: string;
  Virtual_Currency: VirtualCurrency;
  createdAt: string;
  isActive: boolean;
  tokenTransaction: string;
  type: WalletEnum;
  card: Card;
}

export enum VirtualCurrency {
  TRD = 'TRD'
}

export enum WalletEnum {
  CROISSANCE = 'CROISSANCE',
  VALEUR = 'VALEUR',
  EQUILIBRE = 'EQUILIBRE',
  REVENU = 'REVENU',
  INDICIEL = 'INDICIEL',
  ALGORITHMIQUE = 'ALGORITHMIQUE'
}
