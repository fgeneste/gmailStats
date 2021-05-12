import { IMessage } from '@/shared/model/message.model';

export interface IAttachement {
  id?: number;
  fileContentType?: string | null;
  file?: string | null;
  message?: IMessage | null;
}

export class Attachement implements IAttachement {
  constructor(public id?: number, public fileContentType?: string | null, public file?: string | null, public message?: IMessage | null) {}
}
