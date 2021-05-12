export interface IConf {
  id?: number;
  key?: string | null;
  value?: string | null;
}

export class Conf implements IConf {
  constructor(public id?: number, public key?: string | null, public value?: string | null) {}
}
