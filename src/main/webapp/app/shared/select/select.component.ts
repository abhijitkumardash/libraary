import { COMMA, ENTER } from "@angular/cdk/keycodes";
import { Directive, ElementRef, Input, ViewChild } from "@angular/core";
import { FormControl } from "@angular/forms";
import { MatChipInputEvent } from "@angular/material/chips";
import { MatAutocompleteSelectedEvent } from "@angular/material/autocomplete";
import { Observable } from "rxjs";
import { map, startWith } from "rxjs/operators";
import { IService } from "../service/iservice";

interface NamedModel {
  name: string;
}

@Directive()
export class SelectComponent<T extends NamedModel>{
  heading: string = "Select"
  type: any;
  itemCtrl = new FormControl('');
  @Input() items: T[] = [];
  allItems: T[] = [];
  filteredItems: Observable<T[]>;
  separatorKeysCodes: number[] = [ENTER, COMMA];

  @Input() disabled = false;
  @ViewChild("selectInput") selectInput!: ElementRef<HTMLInputElement>;

  constructor(type: { new(...args : any[]): T ;}, private itemService: IService<T>) {
    this.type = type;
    this.filteredItems = this.itemCtrl.valueChanges.pipe(
      startWith(null),
      map((item: string | null) => (item ? this._filter(item) : this.allItems.slice()))
    );
  }

  add(event: MatChipInputEvent): void {
    const value = (event.value || "").trim();
    if (value) {
      this.items.push(new this.type(value));
    }

    event.chipInput!.clear();

    this.itemCtrl.setValue(null);
  }


  remove(author: T): void {
    this.items = this.items.filter(f => f.name !== author.name);
  }

  selected(event: MatAutocompleteSelectedEvent): void {
    this.items.push(this.allItems.find(f => f.name === event.option.value) ?? new this.type(event.option.value));
    this.distinct()
    this.selectInput.nativeElement.value = "";
    this.itemCtrl.setValue(null);
  }

  protected getItemDisplayEntry(item: T) {
    return item.name;
  }

  protected loadData() {
    this.itemService.query().subscribe(
      (res) => {
        this.allItems = res.body ?? [];
      }
    );
  }

  private distinct() {
    this.items = this.items.filter((v, i, a) => a.findIndex(t => (t.name === v.name)) === i);
  }

  private _filter(value: string): T[] {
    const filterValue = value.toLowerCase();

    return this.allItems.filter(author => author.name.toLowerCase().includes(filterValue));
  }
}
