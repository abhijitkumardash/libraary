export interface IAuthor {
  id?: number;
  name: string;
}

export class Author implements IAuthor {
  constructor(public name: string, public id?: number) {}
}
