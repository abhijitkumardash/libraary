import { Component, OnInit } from "@angular/core";
import { AbstractControl, FormControl, FormGroup, ValidationErrors, ValidatorFn, Validators } from "@angular/forms";
import { Category, ICategory } from "../../../../entities/book/category.model";
import { BookService } from "../../../../entities/book/book.service";
import { ISubCategory, SubCategory } from "../../../../entities/book/subcategory.model";

@Component({
  selector: 'category-update',
  templateUrl: './category-update.component.html',
})
export class CategoryUpdateComponent implements OnInit{
  new: ICategory = new Category("", -1)
  categories: ICategory[] = [];
  subcategories: ISubCategory[] = [];
  categoryForm = new FormGroup({
    category: new FormControl<ICategory | null>(null, {validators: [Validators.required]}),
    categoryName: new FormControl("", {validators: [this.requiredIfNew(), this.isDuplicateValidator()]}),
    subcategory: new FormControl("", {validators: [Validators.required, this.isDuplicateSubCategoryValidator()]}),
  })

  constructor(private categoryService: BookService) {}
  ngOnInit(): void {
    this.categoryService.queryCategories().subscribe((res) => {
      this.categories = res.body ?? [];
    })
  }

  onChange() {
    this.categoryForm.get('subcategory')?.setValue("");
    const value = this.categoryForm.get('category')?.value;
    if (value?.id !== -1 && value?.id !== undefined) {
      this.categoryService.querySubCategoriesByCategory(value.id).subscribe(res => {
        this.subcategories = res.body ?? [];
      })
    }
  }

  submit() {
    console.warn(this.categoryForm.value)
    if(this.categoryForm.get("category")?.value?.id !== -1) {
      this.categoryService.createSubCategory({name: this.categoryForm.get("subcategory")?.value ?? undefined, category: this.categoryForm.get("category")?.value ?? undefined}).subscribe()
    } else {
      this.categoryService.createCategory(new Category(this.categoryForm.get("categoryName")?.value ?? "")).subscribe(res => {
        this.categoryService.createSubCategory({name: this.categoryForm.get("subcategory")?.value ?? undefined, category: res.body ?? undefined}).subscribe()
      })
    }
  }

  private isDuplicateValidator(): ValidatorFn {
    return (control: AbstractControl): ValidationErrors | null => {
      const duplicate = this.categories.find(cat => cat.name.toLowerCase() === control.value.toLowerCase()) !== undefined;
      return duplicate ? {duplicate: {value: control.value}} : null;
    };
  }

  private isDuplicateSubCategoryValidator(): ValidatorFn {
    return (control: AbstractControl): ValidationErrors | null => {
      const duplicate = this.subcategories.find(cat => cat.name?.toLowerCase() === control.value.toLowerCase()) !== undefined;
      return duplicate ? {duplicate: {value: control.value}} : null;
    };
  }

  private requiredIfNew() {
    return (control: AbstractControl): ValidationErrors | null => {
      const required = control.parent?.get('category')?.value?.id === -1
      const isEmpty = required ? control.value === "" : false
      console.warn(isEmpty)
      return isEmpty ? {empty: {value: control.value}} : null;
    };
  }
}
