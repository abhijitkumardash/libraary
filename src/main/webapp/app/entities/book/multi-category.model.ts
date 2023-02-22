import { ISubCategory } from "./subcategory.model";

export interface MultiCategory {
  id: number;
  name: string;
  subcategories: ISubCategory[];
}

export class MultiCategory implements MultiCategory {
  constructor(public id: number, public name: string, public subcategories: ISubCategory[]) {}
}
