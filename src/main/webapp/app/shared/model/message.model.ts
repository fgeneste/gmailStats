import { IAttachement } from '@/shared/model/attachement.model';

export interface IMessage {
  id?: number;
  account?: string | null;
  from?: string | null;
  object?: string | null;
  corps?: string | null;
  date?: Date | null;
  stillOnServer?: boolean | null;
  attachements?: IAttachement[] | null;
}

export class Message implements IMessage {
  constructor(
    public id?: number,
    public account?: string | null,
    public from?: string | null,
    public object?: string | null,
    public corps?: string | null,
    public date?: Date | null,
    public stillOnServer?: boolean | null,
    public attachements?: IAttachement[] | null
  ) {
    this.stillOnServer = this.stillOnServer ?? false;
  }
}
