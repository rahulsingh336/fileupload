import { NgModule } from '@angular/core';

import { FileUploadSharedLibsModule, JhiAlertComponent, JhiAlertErrorComponent } from './';

@NgModule({
    imports: [FileUploadSharedLibsModule],
    declarations: [JhiAlertComponent, JhiAlertErrorComponent],
    exports: [FileUploadSharedLibsModule, JhiAlertComponent, JhiAlertErrorComponent]
})
export class FileUploadSharedCommonModule {}
