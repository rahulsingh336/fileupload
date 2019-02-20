import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { IGroup } from 'app/shared/model/group.model';
import { GroupService } from './group.service';

@Component({
    selector: 'jhi-group-update',
    templateUrl: './group-update.component.html'
})
export class GroupUpdateComponent implements OnInit {
    group: IGroup;
    isSaving: boolean;

    constructor(protected groupService: GroupService, protected activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ group }) => {
            this.group = group;
        });
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.group.id !== undefined) {
            this.subscribeToSaveResponse(this.groupService.update(this.group));
        } else {
            this.subscribeToSaveResponse(this.groupService.create(this.group));
        }
    }

    protected subscribeToSaveResponse(result: Observable<HttpResponse<IGroup>>) {
        result.subscribe((res: HttpResponse<IGroup>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
    }

    protected onSaveSuccess() {
        this.isSaving = false;
        this.previousState();
    }

    protected onSaveError() {
        this.isSaving = false;
    }
}
