import { NgModule } from '@angular/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';
import { CommonModule } from '@angular/common';
import { TypeaheadModule } from 'ngx-bootstrap/typeahead';
import { UiSwitchModule } from 'ngx-toggle-switch';
import { PaginationModule } from 'ngx-bootstrap/pagination';
import {SelectModule} from 'ng2-select';
import { TagInputModule } from 'ngx-chips';
import { BlockUIModule } from 'ng-block-ui';
import { CKEditorModule } from '@ckeditor/ckeditor5-angular';


@NgModule({
    imports: [
        CommonModule,
        RouterModule,
        ReactiveFormsModule,
        FormsModule,
        UiSwitchModule,
        SelectModule,
        TagInputModule,
        CKEditorModule,
        PaginationModule.forRoot(),
        TypeaheadModule.forRoot(),
        BlockUIModule.forRoot()
    ],
    declarations: [
    ],
    providers: [
       
    ],
    exports: [
	SelectModule,
	TypeaheadModule,
	TagInputModule,
	UiSwitchModule,
	PaginationModule,
	BlockUIModule,
	CKEditorModule
   ]
})

export class SharedModule { }