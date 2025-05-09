import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'permission'
})
export class PermissionPipe implements PipeTransform {

  transform(value: boolean, args?: any): any {
    if (value) {
      return '\u2713';
    } else {
      return '\u2718';
    }
  }

}
