import { FormControl } from '@angular/forms';

/**
 * Phone No Validator
 */
export function PhoneNoValidator(control: FormControl) {
	
	if( control.value.match(/-|_/g).length > 0 ) {
		return { phoneNoInvalid : "Phone number is invalid." };
	}
    return null;
}

export function NoWhitespaceValidator(control: FormControl) {
    let isWhitespace = (control.value || '').trim().length === 0;
    let isValid = !isWhitespace;
    return isValid ? null : { 'whitespace': true }
}