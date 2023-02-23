import { Directive } from "@angular/core";
import { Observable, ReplaySubject } from "rxjs";

@Directive({})
export abstract class UploadComponent {
  coverImage: string = "";
  validImageTypes = ['image/gif', 'image/jpeg', 'image/png'];

  onFileSelected(event: Event) {
    const target = event.target as HTMLInputElement;
    const files = target.files as FileList;
    if (!this.isValidImageType(files[0]['type'])) {
      target.value = '';
      return;
    }
    this.convertFile(files[0]).subscribe(base64 => {
      this.coverImage = base64;
    });
  }

  isValidImageType(fileType: string): boolean {
    return this.validImageTypes.includes(fileType);
  }

  convertFile(file: File): Observable<string> {
    const result = new ReplaySubject<string>(1);
    const reader = new FileReader();
    reader.readAsBinaryString(file);
    reader.onload = (event) => result.next(btoa(event?.target?.result?.toString() ?? ""));
    return result;
  }
}
