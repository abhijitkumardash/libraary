export interface Pagination {
  page: number;
  size: number;
  sort: string[];
}

export interface Search {
}

export interface BookSearch extends Search {
  title: string;
}

export interface SearchWithPagination extends Search, Pagination {
}

export interface BookSearchWithPagination extends BookSearch, Pagination {
}
