import {IBook} from "../book/book.model";

export interface IBookItem {
  id: number;
  book: IBook;
  barcode: string;
  borrowed: date;
  dueDate: date;
  price: number;
  format: FormatType; // enum
  status: StatusType; // enum
  dateOfPurchase: dayjs.Dayjs;
  publicationDate: dayjs.Dayjs;
  referenceOnly: boolean;
}

export enum FormatType {
  HARDCOVER = "HARDCOVER",
  PAPERBACK = "PAPERBACK",
  AUDIOBOOK = "AUDIOBOOK",
  EBOOK = "EBOOK",
  NEWSPAPER = "NEWSPAPER",
  MAGAZINE = "MAGAZINE",
  JOURNAL = "JOURNAL",
}

export enum StatusType {
  AVAILABLE = "AVAILABLE",
  RESERVED = "RESERVED",
  LOANED = "LOANED",
  LOST = "LOST",
}

export class BookItem implements IBookItem {
  constructor(
    public id: number,
    public book: IBook,
    public barcode: string,
    public borrowed: date,
    public dueDate: date,
    public price: number,
    public format: string,
    public status: string,
    public dateofPurchase: dayjs.Dayjs,
    public pubicationDate: dayjs.Dayjs,
    public referenceOnly: boolean,
  ) {}
}

