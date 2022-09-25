export interface IBook {
  id: number;
  title?: string;
  subject?: string;
  author?: string;
  isbn?: string;
  publisher?: string;
  publicationYear?: string;
  pages?: number;
}

export class Book implements IBook {
  constructor(
    public id: number,
    public title: string,
    public subject: string,
    public author: string,
    public isbn: string,
    public publisher: string,
    public publicationYear: string,
    public pages: number
  ) {}
}
