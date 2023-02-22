import { SelectComponent } from "../select.component";
import { Component, OnInit } from "@angular/core";
import { ILanguage, Language } from "../../../entities/book/language.model";
import { LanguageService } from "../../../entities/book/language.service";

@Component({
  selector: "language-select",
  templateUrl: "../select.component.html",
})
export class LanguageSelectComponent extends SelectComponent<ILanguage> implements OnInit {
  heading = "Language";
  constructor(private languageService: LanguageService) {
    super(Language, languageService);
  }

  ngOnInit(): void {
    this.loadData()
  }

  protected getItemDisplayEntry(item: ILanguage): string {
    return `${item.flag} ${item.name}`
  }
}
