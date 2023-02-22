export interface ICategory {
  id?: number;
  name: string;
}

export class Category implements ICategory {
  constructor(public name: string, public id?: number) {}
}
