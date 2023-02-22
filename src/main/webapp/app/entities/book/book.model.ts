import {ILanguage} from "./language.model";
import {ISubCategory} from "./subcategory.model";
import {IAuthor} from "./author.model";

export interface IBook {
  id?: number;
  cover: string;
  title: string;
  subtitle?: string;
  description?: string;
  authors?: IAuthor[];
  isbn?: string;
  publisher?: string;
  publicationYear?: string;
  pages: number;
  languages?: ILanguage[];
  subCategories?: ISubCategory[];
}

export class Book implements IBook {
  constructor(
  public cover: string,
  public title: string,
  public pages: number,
  public subtitle?: string,
  public description?: string,
  public authors?: IAuthor[],
  public isbn?: string,
  public publisher?: string,
  public publicationYear?: string,
  public languages?: ILanguage[],
  public subCategories?: ISubCategory[],
  public id?: number,
  ) {}
}
