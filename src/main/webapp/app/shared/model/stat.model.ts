export interface IStat {
  id?: number;
  from?: string | null;
  number?: number | null;
  lastupdated?: Date | null;
}

export class Stat implements IStat {
  constructor(public id?: number, public from?: string | null, public number?: number | null, public lastupdated?: Date | null) {}
}
