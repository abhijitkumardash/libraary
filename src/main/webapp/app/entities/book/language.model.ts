export interface ILanguage {
  id: number;
  name: string;
  flag: string;
}

export class Language implements Language {
  constructor(public id: number, public name: string, public flag: string) {}
}
