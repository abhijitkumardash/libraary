import {ILanguage} from "./language.model";
import {ISubCategory} from "./subcategory.model";
import {IAuthors} from "./author.model";

export interface IBook {
  id: number;
  cover: string;
  title?: string;
  subject?: string;
  authors?: IAuthors[];
  isbn?: string;
  publisher?: string;
  publicationYear?: string;
  pages?: number;
  languages?: ILanguage[];
  subcategory?: ISubCategory[];
}

export class Book implements IBook {
  constructor(
    public id: number,
    public title: string,
    public subject: string,
    public isbn: string,
    public publisher: string,
    public publicationYear: string,
    public pages: number
  ) {}

  cover: string;
}
