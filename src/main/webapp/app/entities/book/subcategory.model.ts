import {ICategory} from "./category.model";

export interface ISubCategory {
  id: number;
  name: string;
  category: ICategory;
}

 export class SubCategory implements ISubCategory {
  constructor(public id: number, public name: string, public category: ICategory) {}
}
