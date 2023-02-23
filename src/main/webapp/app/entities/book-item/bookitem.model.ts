import {IBook} from "../book/book.model";
import {Dayjs} from "dayjs";

export interface IBookItem {
  id?: string;
  book?: IBook;
  barcode?: string;
  label?: string;
  borrowed?: Dayjs;
  dueDate?: Dayjs;
  price?: number;
  format?: FormatType; // enum
  status?: StatusType; // enum
  dateOfPurchase?: Date;
  publicationDate?: Dayjs;
  referenceOnly?: boolean;
}

export enum   FormatType {
  HARDCOVER = "HARDCOVER",
  PAPERBACK = "PAPERBACK",
  AUDIOBOOK = "AUDIOBOOK",
  EBOOK = "EBOOK",
  NEWSPAPER = "NEWSPAPER",
  MAGAZINE = "MAGAZINE",
  JOURNAL = "JOURNAL",
  CD = "CD",
  DVD = "DVD",
  BLURAY = "BLURAY",
  VINYL = "VINYL",
  IMAGE_ALBUM = "IMAGE ALBUM",
}

export enum StatusType {
  AVAILABLE = "AVAILABLE",
  RESERVED = "RESERVED",
  LOANED = "LOANED",
  LOST = "LOST",
}

export class BookItem implements IBookItem {
  constructor(
    public id?: string,
    public book?: IBook,
    public barcode?: string,
    public label?: string,
    public borrowed?: Dayjs,
    public dueDate?: Dayjs,
    public price?: number,
    public format?: FormatType,
    public status?: StatusType,
    public dateOfPurchase?: Date,
    public publicationDate?: Dayjs,
    public referenceOnly?: boolean,
  ) {}
}

