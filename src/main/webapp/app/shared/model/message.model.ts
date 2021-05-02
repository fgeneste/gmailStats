export interface IMessage {
  id?: number;
  from?: string | null;
  object?: string | null;
  corps?: string | null;
  date?: Date | null;
}

export class Message implements IMessage {
  constructor(
    public id?: number,
    public from?: string | null,
    public object?: string | null,
    public corps?: string | null,
    public date?: Date | null
  ) {}
}
