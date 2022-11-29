export interface ILanguage {
  id: string;
  name: string;
  flag: string;
}

export class Language implements Language {
  constructor(public id: string, public name: string, public flag: string) {}
}
