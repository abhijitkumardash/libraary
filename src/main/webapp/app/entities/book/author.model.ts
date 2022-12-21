export interface IAuthors {
  id: number;
  name: string;
}

export class Authors implements IAuthors {
  constructor(public id: number, public name: string) {}
}
